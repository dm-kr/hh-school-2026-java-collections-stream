package tasks;

import common.Area;
import common.Person;

import java.util.Collection;
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

  // Посчитал, что вынести создание описания на основе персоны и региона добавит читаемости
  private static String createDescription(Person person, Area area) {
    return person.firstName() + " - " + area.getName();
  }

  public static Set<String> getPersonDescriptions(Collection<Person> persons,
                                                  Map<Integer, Set<Integer>> personAreaIds,
                                                  Collection<Area> areas) {
    // Чтобы ускорить поиск регионов, создаем мапу регионов по их id
    Map<Integer, Area> areaById = areas.stream().collect(Collectors.toMap(Area::getId, a -> a));

    return persons.stream()
                  // Берем Set.of() как дефолтное значение, если персоны нет в переданной мапе
                  .flatMap(person -> personAreaIds.getOrDefault(person.id(), Set.of())
                                                  .stream()
                                                  // Решил использовать два .map подряд для повышения читаемости
                                                  .map(areaById::get)
                                                  .map(area -> createDescription(person, area)))
                  .collect(Collectors.toSet());
  }
}
