package tasks;

import common.Person;
import common.PersonService;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.mockito.ArgumentMatchers.eq;

import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.when;

import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class Task1Test {

  @InjectMocks
  private Task1 task;

  @Mock
  private PersonService personService;

  @ParameterizedTest
  @MethodSource("generateData")
  public void test(List<Integer> ids) {
    Set<Person> persons = ids.stream()
                             .sorted()
                             .map(id -> new Person(id, "firstName", "secondName", "middleName", Instant.now()))
                             .collect(Collectors.toSet());
    when(personService.findPersons(eq(ids)))
        .thenReturn(persons);
    assertEquals(ids, task.findOrderedPersons(ids).stream().map(Person::id).collect(Collectors.toList()));
  }

  private static Stream<Arguments> generateData() {
    return Stream.of(
        Arguments.of(List.of()),
        Arguments.of(List.of(3, 1, 2)),
        Arguments.of(List.of(1)),
        Arguments.of(List.of(5, 4, 3, 2, 1)),
        Arguments.of(List.of(5, 5, 5))
    );
  }
}
