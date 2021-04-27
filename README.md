#### SpringbootRestApiMicroservice

Учебный проект, основная задача:
* разобраться в конфигурировании проекта при помощи Spring-Boot и пакетов starters;
* построить RestApi микросервис;
* организовать взаимодействие с СУБД Postgres при помощи SpringData (JPA);
* провести тестирование полученных RestApi;
* организовать логирование;
* произвести сборку проекта в jar файл;
##### Дополнительно:
* добавлено логирование АОП для контроллера GroupController
* добавлена авторизация на основе SpringSecurity (BCryptPasswordEncoder + DB storage account)
* добавлен SpringActuator + test для endPoint (health,info,beans,env), реализованна собственная метрика /health/customServiceActuator
* добавлено версионирование схемы БД (Liquibase)
* добавлено документирование API springfox+swagger-ui: http://localhost:11095/swagger-ui/index.html#/
* добавлено кеширование обьектов student/remoteUser при помощи spring data сache на методах запроса к СУБД
* добавлен RecordController RestApi-контролер реализующий хранение данных в redis при помощи spring data redis 

Данный проект разработан с целью сравнения подходов к конфигурированию крассического Spring и Spring-Boot. 
Проект для сравнения подходов: https://github.com/red-yellow-tulip/SpringRestApiMicroservice

