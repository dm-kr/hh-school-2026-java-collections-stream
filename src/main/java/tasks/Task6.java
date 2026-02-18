package tasks;

import common.Area;
import common.Person;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/*
Имеются
- коллекция персон Collection<Person>
- словарь Map<Integer, Set<Integer>>, сопоставляющий каждой персоне множество id регионов
- коллекция всех регионов Collection<Area>
На выходе хочется получить множество строк вида "Имя - регион". Если у персон регионов несколько, таких строк так же будет несколько
 */
public class Task6 {

  private static String createDescription(Person person, Area area) {
    return new StringBuilder().append(person.firstName()).append(" - ").append(area.getName()).toString();
  }

  public static Set<String> getPersonDescriptions(Collection<Person> persons,
          Map<Integer, Set<Integer>> personAreaIds,
          Collection<Area> areas) {

    Map<Integer, Area> areaById = areas.stream()
            .collect(Collectors.toMap(Area::getId, a -> a));

    return new HashSet<>(persons.stream()
            .flatMap(person -> personAreaIds.get(person.id()).stream()
                    .map(areaById::get)
                    .map(area -> createDescription(person, area)))
            .collect(Collectors.toSet()));
  }
}
