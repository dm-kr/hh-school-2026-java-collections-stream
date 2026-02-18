package tasks;

import common.Person;
import common.PersonService;

import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

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
   * Используем HashMap для временного хранения индексов каждого элемента
   * списка, так как для сортировки множества persons нам нужно знать, на
   * какой позиции стояли индексы в изначальном списке, а операция поиска
   * индекса по значению элемента в любых списках имеет сложность O(n),
   * что приводит к итоговой сложности сортировки O(n^2 log n)
   * 
   * С использованием HashMap операция получения индекса имеет сложность
   * O(1), что снижает сложность сортировки до O(n log n)
   */
  public List<Person> findOrderedPersons(List<Integer> personIds) {
    Set<Person> persons = personService.findPersons(personIds);
    HashMap<Integer, Integer> personIndexMap = new HashMap<>();
    Integer index = 0;
    for (Integer id : personIds) {
      personIndexMap.put(id, index);
      index++;
    }
    return persons.stream()
            .sorted(Comparator.comparing(person -> personIndexMap.get(person.id())))
            .toList();
  }
}
