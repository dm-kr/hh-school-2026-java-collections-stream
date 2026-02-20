package tasks;

import common.Person;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

/*
Далее вы увидите код, который специально написан максимально плохо.
Постарайтесь без ругани привести его в надлежащий вид
P.S. Код в целом рабочий (не везде), комментарии оставлены чтобы вам проще понять чего же хотел автор
P.P.S Здесь ваши правки необходимо прокомментировать (можно в коде, можно в PR на Github)
 */
public class Task9 {

  private long count;

  // Костыль, эластик всегда выдает в топе "фальшивую персону".
  // Конвертируем начиная со второй
  public List<String> getNames(List<Person> persons) {
    // Добавляем skip(1) для пропуска первого элемента стрима
    return persons.stream().skip(1).map(Person::firstName).toList();
  }

  // Зачем-то нужны различные имена этих же персон (без учета фальшивой разумеется)
  public Set<String> getDifferentNames(List<Person> persons) {
    // dictinct() тут излишен, потому что toSet() в любом случае отдаст нам уникальные элементы
    // Следовательно, всё выражение можно просто заменить на конструктор HashSet
    return new HashSet<>(getNames(persons));
  }

  // Тут фронтовая логика, делаем за них работу - склеиваем ФИО
  public String convertPersonToString(Person person) {
    /*
      Во-первых, мы тут зачем-то собираем ФИФ, а не ФИО
      Во-вторых, текущая логика функции допускает возможность строки с лидирующим пробелом
      В-третьих, код можно переписать используя стримы, что сделает его более читаемым и уберет дублирование кода
      Также, в Person нет переопределенных конструкторов с валидацией полей, поэтому
      предположу, что нужно отфильтровать персоны по null и пустым значениям
    */
    return Stream.of(person.secondName(), person.firstName(), person.middleName())
                 .filter(Objects::nonNull)
                 .filter(String::isBlank)
                 .map(String::trim) // Убираем лишние пробелы с начала и конца строк
                 .collect(Collectors.joining(" ")); // Собираем строку через пробелы
  }

  // словарь id персоны -> ее имя
  public Map<Integer, String> getPersonNames(Collection<Person> persons) {
    // Указано, что нужно сопоставить id и имя персоны, но использован метод convertPersonToString,
    // который возвращает ФИО полностью
    // Если нужно именно имя, то вместо this::convertPersonToString нужно использовать Person::firstName
    return persons.stream()
                  .collect(Collectors.toMap(
                      Person::id,
                      this::convertPersonToString,
                      // Решаем конфликты ключей, оставляя существующее значение
                      (a, b) -> a
                  ));
  }

  // есть ли совпадающие в двух коллекциях персоны?
  public boolean hasSamePersons(Collection<Person> persons1, Collection<Person> persons2) {
    // Учитывая, что Person - это record без переопределения методов equals и hashCode,
    // можем сформировать hashSet по первой коллекции персон и проверить, есть ли там элементы из второй коллекции
    HashSet<Person> uniquePersons1 = new HashSet<>(persons1);
    return persons2.stream().anyMatch(uniquePersons1::contains);
  }

  // Посчитать число четных чисел
  public long countEven(Stream<Integer> numbers) {
    // Можем использовать метод count вместо хранения отдельной переменной
    return numbers.filter(num -> num % 2 == 0).count();
  }

  // Загадка - объясните почему assert тут всегда верен
  // Пояснение в чем соль - мы перетасовали числа, обернули в HashSet, а toString() у него вернул их в сортированном порядке
  void listVsSet() {
    /*
      Долго копался по исходникам коллекций, пытался найти особенности работы итераторов, но, как я понял в итоге,
      всё дело в реализации hashCode для Integer, судя по которой, hash будет совпадать со значением числа, что
      как раз и даст правильный порядок перебора, в каком бы порядке мы не клали числа.

      Upd. Решил уточнить пару моментов, почему именно так происходит, так как разобрался поглубже

      Порядок перебора сета из интов не всегда будет таким, так как он зависит от размера самого контейнера.
      Так что можно гарантировать возрастающий порядок перебора элементов только в том случае, когда у нас
      размер сета больше максимального элемента сета, потому что в таком случае каждый элемент будет попадать в бакет,
      номер которого равен значению самого элемента.

      Ну, и в нашем случае как-раз так и получается. Судя по реализации HashSet, при его инициализации на основе
      другого контейнера, создается мапа с размером "Math.max((int) (c.size()/.75f) + 1, 16)", что по сути всегда
      на ~33% больше, чем количество элементов в переданном контейнере. А так как у нас список с подряд идущими
      числами, его размер равен значению максимального элемента.
      Следовательно, условие "размер сета больше максимального элемента сета" соблюдается.
     */
    List<Integer> integers = IntStream.rangeClosed(1, 10000).boxed().collect(Collectors.toList());
    List<Integer> snapshot = new ArrayList<>(integers);
    Collections.shuffle(integers);
    Set<Integer> set = new HashSet<>(integers);
    assert snapshot.toString().equals(set.toString());
  }
}
