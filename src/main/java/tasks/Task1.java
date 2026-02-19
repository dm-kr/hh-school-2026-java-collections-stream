package tasks;

import common.Person;
import common.PersonService;

import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/*
Задача 1
Метод на входе принимает List<Integer> id людей, ходит за ними в сервис
(он выдает несортированный Set<Person>, внутренняя работа сервиса неизвестна)
нужно их отсортировать в том же порядке, что и переданные id.
Оценить асимптотику работы
 */
public class Task1 {

  private final PersonService personService;

  public Task1(PersonService personService) {
    this.personService = personService;
  }

  /*
   * Используем HashMap для временного хранения Person с доступом по id.
   * Сложность заполнения мапы O(n).
   * Далее используем стримы для маппинга id каждого человека к объекту Person,
   * что тоже имеет сложность O(n).
   * Сборка элеметнов в список тоже O(n), соответственно общая сложность
   * также будет равна O(n).
   */
  public List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = personService.findPersons(personIds);
    HashMap<Integer, Person> personById = new HashMap<>();
    for (Person person : persons) {
      personById.put(person.id(), person);
    }
    return personIds.stream().map(id -> personById.get(id)).collect(Collectors.toList());
  }
}
