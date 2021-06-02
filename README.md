#### SpringbootRestApiMicroservice

Учебный проект, основная задача:
* разобраться в конфигурировании проекта при помощи Spring-Boot и пакетов starters;
* построить RestApi микросервис;
* организовать взаимодействие с СУБД Postgres при помощи SpringData (JPA);
* провести тестирование полученных RestApi;
* организовать логирование;
* произвести сборку проекта в jar файл;
##### Дополнительно:

* module-data-cache: добавлено логирование АОП для контроллера GroupController
* module-security: добавлена авторизация на основе SpringSecurity (BCryptPasswordEncoder + DB storage account)
* module-actuator: добавлен SpringActuator + test для endPoint (health,info,beans,env), реализованна собственная метрика /health/customServiceActuator
* module-data-cache: добавлено версионирование схемы БД (Liquibase)
* module-calc-strategy: добавлено документирование API springfox+swagger-ui: http://localhost:17000/swagger-ui/index.html#/
* module-data-cache: добавлено кеширование обьектов student при помощи spring data сache на методах запроса к СУБД
* module-redis: добавлен RecordController RestApi-контролер реализующий хранение данных в redis при помощи spring data redis 

Данный проект разработан с целью сравнения подходов к конфигурированию крассического Spring и Spring-Boot. 
Проект для сравнения подходов: https://github.com/red-yellow-tulip/SpringRestApiMicroservice

