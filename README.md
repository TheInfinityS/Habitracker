# Habit Tracker
Habit tracker следит за прогрессом и формированием привычек.
Для просмтра кода можете выбрать два репозитория:
##[Микросервесный](https://github.com/SKozhomberdiev/HabitTracker1.git) и [Монолитный](https://github.com/TheInfinityS/Habitracker)

В функцианал приложения входит:
+ Авторизация Пользователя**
+ Сброс пароля Пользователя
+ Управление профилем
+ Трекинг созданных привычек


Создать проект можно в [Spring Initializer](https://start.spring.io/). Также стоит указать какие зависимости:
+ **Spring boot starter web**
+ **Spring boot starter thymeleaf**
+ **Spring boot devtools**
+ **Spring boot starter data jpa**
+ **Spring boot starter validation**
____

### Аторизация
Для авторизации необходимы следующие зависимости:
+ [Spring Security](https://spring.io/projects/spring-security)
+ [Spring boot oauth2 client](https://spring.io/guides/tutorials/spring-boot-oauth2/)
+ [Spring boot auth2 resource server](https://www.baeldung.com/spring-security-oauth-resource-server)
+ [Spring security oauth2 jose](https://www.baeldung.com/spring-security-5-oauth2-login)
+ [Spring boot email](https://www.baeldung.com/spring-email)

Для авторизации через google нужно перейти [по ссылке](http://localhost:8080/oauth2/authorization/google), после авторизации произойдёт переадресация на главную страницу, в заголовке запроса которой вы можете получить по полю *Authorization* **jwt token**.

Для логина нужно перейти по ссылке http://localhost:8080/oauth2/login на вход которой принимается **JwtRequest** состоящий из **email** и **пароля**. После чего будет получен **jwt** token через *ResponseEntity* 

Для регистрации нужно перейти по ссылке http://localhost:8080/oauth2/signup на вход которой принимается **SignUpRequest** состоящий из **email**, **имени польщователя** и **пароля**. После чего при успешной регистрации будет возвращён сообщение об успешной регистрации. Также после регистрации нужно подтвердить почту, посредством перехода по ссылке письма которое придёт на почту.

____

### Сброс пароля
Для сброса пароля необходимы следующие зависимости:
+ [Spring boot email](https://www.baeldung.com/spring-email)

Смена пароля осуществляется в три этапа:
1. Запрос на смену пароля по ссылке http://localhost:8080/user/activate/changePassword, после которого на почту придёт письмо на смену пароля
2. Переход по ссылке для активации смены пароля при котором активируется возможность смены пароля
3. И отправка нового пароля по ссылке http://localhost:8080/user/changePassword/{code}. code - это путь после последнего слеша

____

### Управление профилем
Для смены иконки и имени пользователя нужно перейти по ссылке http://localhost:8080/user/update. На вход он приниает файл и строку с новым именем пользователя. Также параметры могут быть нулевыми.

____

### Трекинг привычек
Для удобной работы с привычками необходимы следующие зависимости:
+ Jackson Datatype: JSR310

В основном вся информация уже хранится в сущности *привичек*, пожтому остаётся только начислять повторы на определённую дату. Для этого нужно перейти по ссылке  http://localhost:8080/updateCompleteReps/{id}, на вход приниается количество выполненных повторов. Стоит указать что id в пути это идентификатор привычки к которой (только своей) вы хотите зачислить повторы.

