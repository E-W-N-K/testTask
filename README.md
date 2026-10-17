# Property View

REST API для хранения, поиска и агрегации информации об отелях. Проект использует Java 21,
Spring Boot 4, Spring Data JPA, Liquibase и H2.

## Запуск

Требуется JDK 21 или новее. Из корня проекта:

```bash
mvn spring-boot:run
```

Приложение запускается на `http://localhost:8092`. По умолчанию используется H2 in-memory,
структура базы создаётся Liquibase при старте.

## API

Все прикладные маршруты имеют префикс `/property-view`:

| Метод | Маршрут | Назначение |
|---|---|---|
| `GET` | `/property-view/hotels` | краткая информация обо всех отелях |
| `GET` | `/property-view/hotels/{id}` | полная информация об отеле |
| `POST` | `/property-view/hotels` | создание отеля |
| `POST` | `/property-view/hotels/{id}/amenities` | добавление удобств |
| `GET` | `/property-view/search` | поиск по `name`, `brand`, `city`, `country`, `amenities` |
| `GET` | `/property-view/histogram/{param}` | группировка по `brand`, `city`, `country` или `amenities` |

Параметры поиска можно комбинировать. Сравнение регистронезависимое, пустые параметры
игнорируются. Повторяющиеся amenities одного отеля не добавляются.

Swagger UI доступен по адресу `http://localhost:8092/swagger-ui.html`, OpenAPI JSON —
`http://localhost:8092/v3/api-docs`.

## Пример создания отеля

```json
{
  "name": "DoubleTree by Hilton Minsk",
  "description": "Hotel in the center of Minsk",
  "brand": "Hilton",
  "address": {
    "houseNumber": 9,
    "street": "Pobediteley Avenue",
    "city": "Minsk",
    "country": "Belarus",
    "postCode": "220004"
  },
  "contacts": {
    "phone": "+375 17 309-80-00",
    "email": "doubletreeminsk.info@hilton.com"
  },
  "arrivalTime": {
    "checkIn": "14:00",
    "checkOut": "12:00"
  }
}
```

`description` и `arrivalTime.checkOut` можно не передавать. Время задаётся в формате `HH:mm`.

## Переключение базы данных

Настройки подключения вынесены в переменные окружения. PostgreSQL JDBC-драйвер уже включён,
поэтому для переключения не требуется менять Java-код или пересобирать схему вручную:

```powershell
$env:DB_URL = "jdbc:postgresql://localhost:5432/property_view"
$env:DB_USERNAME = "property_view"
$env:DB_PASSWORD = "secret"
mvn spring-boot:run
```

Liquibase применит те же миграции к выбранной базе. Для другой реляционной СУБД достаточно
добавить её JDBC-драйвер и задать соответствующий `DB_URL`.

## Проверка

```bash
mvn test
```

Интеграционные тесты запускают полный Spring-контекст с H2 и проверяют API, поиск,
гистограммы, валидацию, обработку ошибок и OpenAPI-документ.
