package workWithTasks;
/*По поводу Error. Согласен с замечанием. В данном случае ошибка чтения/записи файла относится не просто к ошибкам,
  а к ошибкам во время выполнения программы => RunTimeException подходит лучше всего на мой взгляд.
  Убрал модификатор final из у аргумента message. Честно, ориентировался на тренажёр при создании исключения. Там
  исключение создаётся с "final String message". Но посмотрел другие примеры создания исключений - действительно,
  там отсутствует применение данного модификатора у строки. Тем более final по идее здесь не нужен, т.к. строка сама
  по себе неизменяемая.*/
class ManagerSaveException extends RuntimeException {
    public ManagerSaveException(String message) {
        super(message);
    }
}