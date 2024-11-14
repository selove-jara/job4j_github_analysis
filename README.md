# job4j_github_analysis

## Описание проекта
Проект представляет собой Spring Boot приложение, которое автоматически анализирует данные с GitHub, регулярно выгружая репозитории и коммиты пользователей через публичное API GitHub. С помощью аннотаций @Scheduled и @Async сервис периодически собирает информацию о репозиториях и коммитах, сохраняя эти данные в базе для дальнейшего анализа.
## Стек технологий
- **Java 17** - основной язык программирования
- **Spring Boot 3.3.4** - фреймворк для создания приложений
- **PostgreSQL 42.5.1** - база данных
- **Maven 3.8** - система управления зависимостями

## Требования к окружению
Для запуска проекта необходимо установить следующее ПО:
- **Java 17**
- **Maven 3.8**
- **PostgreSQL 42.5.1**

## Запуск проекта
1. Склонируйте репозиторий:
 
2. Создайте базу данных PostgreSQL:
 
3. Настройте доступ к базе данных в файле `application.properties`:

4. Соберите и запустите проект:
    ```
    mvn spring-boot:run
    ```