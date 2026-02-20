package tasks;

import common.Person;
import common.PersonService;
import common.PersonWithResumes;
import common.Resume;

import java.io.Console;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
  Еще один вариант задачи обогащения
  На вход имеем коллекцию персон
  Сервис умеет по personId искать их резюме (у каждой персоны может быть несколько резюме)
  На выходе хотим получить объекты с персоной и ее списком резюме
 */
public class Task8 {
  private final PersonService personService;

  public Task8(PersonService personService) {
    this.personService = personService;
  }

  public Set<PersonWithResumes> enrichPersonsWithResumes(Collection<Person> persons) {
    // Так как на выходе всё равно нужен сет, создаем сет с id персон для передачи в findResumes
    Set<Integer> personIds = persons.stream()
                                    .map(Person::id)
                                    .collect(Collectors.toSet());
    Set<Resume> resumes = personService.findResumes(personIds);
    // Создаем мапу с сетами резюме, сгруппированными по id персоны
    Map<Integer, Set<Resume>> resumeByPersonId = resumes.stream()
                                                        .collect(Collectors.groupingBy(Resume::personId,
                                                                                       Collectors.toSet()));
    // На случай, если у персоны не окажется резюме, указываем Set.of() как дефолтное значение
    return persons.stream()
                  .map(person -> new PersonWithResumes(person, resumeByPersonId
                      .getOrDefault(person.id(), Set.of())))
                  .collect(Collectors.toSet());
  }
}
