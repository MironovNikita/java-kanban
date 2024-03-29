

<p align="center">

  <img width="600" height="200" src="https://github.com/MironovNikita/java-kanban/blob/main/res/logo.gif">

</p>

# 

Современный мир заставляет нас держать в голове достаточно большое количество задач, которые нам нужно реализовать. Это могут быть как краткосрочные задачи, так и долгосрочные, состоящие из нескольких подзадач. Для того, чтобы суметь распланировать своё время грамотно, нам и нужен **"Трекер задач"**.

### Описание
Приложение даёт возможность создавать простые задачи и эпики с подзадачами. Данный проект представляет собой бэкенд трекера задач, формирующий модель данных в подобном формате:

![Front](https://github.com/MironovNikita/java-kanban/blob/main/res/front.png)

### Типы задач
- `Task` - простая задача;
- `Subtask` - подзадача;
- `Epic` - большая задача, состоящая из подзадач.

Каждая задача обладает следующими свойствами:
1. **Название** - суть задачи.
2. **Описание** - детали задачи.
3. **ID** - уникальный номер задачи.
4. **Статус** - отображение прогресса:
    + `NEW` - новая задача, к выполнению которой не приступили.
    + `IN_PROGRESS` - задача находится в работе.
    + `DONE` - задача завершена.

#### Для "_подзадач_" и "_эпиков_" справедливы следующие условия:
- для каждой _подзадачи_ известно, в рамках какого "_эпика_" она выполняется;
- для каждого _эпика_ известно, из каких _подзадач_ он состоит;
- условие завершения _эпика_ - исключительно выполнение всех _подзадач_.

Также трекер отображает последние просмотренные пользователем задачи.

![History](https://github.com/MironovNikita/java-kanban/blob/main/res/scr1.png)

### Реализации менеджера задач
Всего в данной программе присутствуют три реализации трекера задач:
1. С хранением состояния трекера в **оперативной памяти** компьютера (_`InMemoryTaskManager`_);
2. С хранением состояния трекера в **файле** с расширением **.csv** (_`FileBackedTaskManager`_);
3. С хранением состояния на **сервере** (_`HttpTaskManager`_).

![Variants](https://github.com/MironovNikita/java-kanban/blob/main/res/scr3.png)

## Примеры работы

**InMemoryTaskManager**\
![InMemoryTaskManager](https://github.com/MironovNikita/java-kanban/blob/main/res/scr1.png)

**FileBackedTaskManager**\
Пример сформированного файла **history.csv**
![InMemoryTaskManager](https://github.com/MironovNikita/java-kanban/blob/main/res/scr2.png)

**HttpTaskManager**\
![HttpTaskManager1](https://github.com/MironovNikita/java-kanban/blob/main/res/scr4.png)\
![HttpTaskManager2](https://github.com/MironovNikita/java-kanban/blob/main/res/scr5.png)\
![HttpTaskManager3](https://github.com/MironovNikita/java-kanban/blob/main/res/scr6.png)

## API сервера HttpTaskManager
API работает так, чтобы все запросы по пути _/tasks/<ресурсы>_ приходили в интерфейс **TaskManager**. Путь для обычных задач — _/tasks/task_, для подзадач — _/tasks/subtask_, для эпиков — _/tasks/epic_. Получить все задачи сразу можно будет по пути _/tasks/_, а получить историю задач по пути _/tasks/history_. 
Для получения данных должны быть **GET**-запросы. Для создания и изменения — **POST**-запросы. Для удаления — **DELETE**-запросы. Задачи передаются в теле запроса в формате JSON. Идентификатор (id) задачи передаётся в качестве параметра запроса (через вопросительный знак).

### KVServer
`KVServer` — это хранилище, где данные хранятся по принципу <ключ-значение>.\
Он умеет: 
- **`GET /register`** — регистрировать клиента и выдавать уникальный токен доступа (аутентификации). Это нужно, чтобы хранилище могло работать сразу с несколькими клиентами.
- **`POST /save/<ключ>?API_TOKEN=`** — сохранять содержимое тела запроса, привязанное к ключу.
- **`GET /load/<ключ>?API_TOKEN=`** — возвращать сохранённые значение по ключу.

### Http-клиент
Для работы с хранилищем разработан Нttp-клиент, который преобразует вызовы методов в Http-запросы. (**KVTaskClient**).
Свойства клиента:
- Конструктор принимает URL к серверу хранилища и регистрируется. При регистрации выдаётся токен (**`API_TOKEN`**), который нужен при работе с сервером.
- Метод **`void put(String key, String json)`** сохраняет состояние менеджера задач через запрос **`POST /save/<ключ>?API_TOKEN=`**.
- Метод **`String load(String key)`** возвращает состояние менеджера задач через запрос **`GET /load/<ключ>?API_TOKEN=`**.

**Схема API**

![API](https://github.com/MironovNikita/java-kanban/blob/main/res/API.png)
## 🚀 Обо мне
Я разрабатывал данный проект на языке Java 11 в рамках курса Яндекс.Практикум "Java-Разработчик". В процессе работы над данным проектом я познакомился с реализацией трекера задач в различных вариантах: работа с оперативной памятью, файлами, а также сервером.



## 🛠 Применяемые технологии
Данный проект разрабатывался на чистой Java 11.


## Автор

- [@MironovNikita](https://github.com/MironovNikita)

