# ShareIt

*Учебный проект (Яндекс Практикум).*

Сервис для шеринга (совместного использования) вещей. С помощью сервиса можно не покупать нужную на короткий срок вещь, а взять её в аренду на время. 
Также можно оставлять заявки на вещи, которых пока нет в сервисе, или же сдавать свои вещи в аренду.

Для возможности дальнейшего масштабирования и дополнительной валидации запросов сервис разделен на 2 модуля: server (отвечающий за основную бизнес-логику) на порту 9090 и gateway (отвечающий за прием запросов) на порту 8080.

## Стек технологий

> - SpringBoot
> - PostgreSql
> - Spring Data JPA
> - Hibernate ORM
> - REST Api
> - Maven
> - Docker
 
## Запуск приложения

Для запуска приложения, находясь в корневой папке проекта, введите в командную строку:

```
docker compose up -d
```

## Пользователи

### Добавление

> POST /users 

> Request Body:
> 
> {
> 
>       name: "username",
> 
>       email: "userEmail"
> 
> }

Добавляет в базу данных пользователя.

### Обновление

> PATCH /users/{userId}

> Request Body:
>
> {
>
>       name: "username",
>
>       email: "userEmail"
>
> }

Обновляет информацию о пользователе в базе данных.

### Просмотр

> GET /users/{userId}

Передает информацию о пользователе.

### Удаление

> DELETE /users/{userId}

Удаляет пользователя из базы данных по идентификатору.

## Вещи

### Добавление

> POST /items

> Request Header "X-Sharer-User-Id": id владельца вещи

> Request Body:
>
> {
>
>       name: "item name",
>
>       description: "description",
>       
>       available: "true",
> 
>       requestId: "requestId"
>
> }

Добавляет в базу данных вещь.

### Обновление

> PATCH /items/{itemId}

> Request Header "X-Sharer-User-Id": id владельца вещи

> Request Body:
>
> {
>
>       name: "item name",
>
>       description: "description",
>       
>       available: "true"
>
> }

Обновляет информацию о вещи в базе данных.

### Просмотр

> GET /items/{itemId}
> 
> > Request Header "X-Sharer-User-Id": id пользователя

Передает информацию о вещи (для стороннего пользователя - всю, кроме информации о бронировании).

> GET /items
>
> > Request Header "X-Sharer-User-Id": id владельца вещи

Передает информацию о всех вещах пользователя.

### Поиск

> GET /items/search?text="searchText"

Поиск вещи по текстовому запросу.

## Запросы

### Добавление

> POST /requests

> Request Header "X-Sharer-User-Id": id автора запроса

> Request Body:
>
> {
>
>       description: "description"
>
> }

Добавляет в базу данных запрос на вещь.

### Просмотр

> GET /requests
>
> > Request Header "X-Sharer-User-Id": id автора запроса

Передает информацию о всех запросах пользователя.

> GET /requests/all
>
> > Request Header "X-Sharer-User-Id": id автора запроса

Получение списка запросов от других пользователей.

> GET /requests/{requestId}

Получение информации о конкретном запросе.

## Бронирование

### Добавление

> POST /bookings

> Request Header "X-Sharer-User-Id": id автора бронирования

> Request Body:
>
> {
>
>       itemId: {itemId},
>       
>       start: {YYYY-MM-DDTHH:mm:ss},
> 
>       end: {YYYY-MM-DDTHH:mm:ss},
>
> }

Добавляет в базу данных запрос на бронирование вещи.

### Просмотр

> GET /bookings?state={state} (param not required)

> Request Header "X-Sharer-User-Id": id автора бронирования

Получение информации о всех бронированиях пользователя, возможно фильтрация по состоянию бронирования.

> GET /bookings/owner?state={state} (param not required)

> Request Header "X-Sharer-User-Id": id пользователя

Получение информации о всех бронированиях всех вещей пользователя, возможно фильтрация по состоянию бронирования.

> GET /bookings/{bookingId}

> Request Header "X-Sharer-User-Id": id автора бронирования или владельца вещи

Получение информации о конкретном бронировании.

### Подтверждение

> PATCH /bookings/{bookingId}?approved={isApproved}

> Request Header "X-Sharer-User-Id": id владельца вещи
 
Подтверждение или отклонение запроса на бронирование.

## Комментарии

### Добавление

> POST /items/{itemId}/comments

> Request Header "X-Sharer-User-Id": id автора комментария

> Request Body:
>
> {
>
>       text: {text}
>
> }

Добавляет в базу данных комментарий к вещи.