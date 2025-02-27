# Гайд по запуску и работе с Docker и Docker Compose для работяг и системных аналитиков

## 1. Запуск Docker

### В консоли:
1. Откройте терминал.
2. Введите команду для проверки установленного Docker:
   ```sh
   docker --version
   ```
3. Если Docker установлен, запустите его:
   ```sh
   sudo systemctl start docker
   ```
4. Проверьте, что Docker запущен:
   ```sh
   docker ps
   ```

### С помощью Docker Desktop:
1. Откройте приложение **Docker Desktop**.
2. Дождитесь, пока иконка Docker в трее станет активной.
3. Проверьте запуск, выполнив в терминале:
   ```sh
   docker ps
   ```

## 2. Запуск Docker Compose

### В консоли:
1. Перейдите в директорию с `docker-compose.yml`:
   ```sh
   cd /путь/к/docker-compose.yml
   ```
2. Запустите контейнеры:
   ```sh
   docker-compose up -d
   ```
3. Проверьте список запущенных контейнеров:
   ```sh
   docker ps
   ```

### В IntelliJ IDEA:
1. Откройте проект.
2. Найдите файл `docker-compose.yml`.
3. Наведите курсор на `docker-compose.yml`, появится стрелка запуска.

![image](https://github.com/user-attachments/assets/84c9943e-c6b0-4b76-9df5-46669cef922c)

5. Нажмите на стрелку и выберите `Run`.

## 3. Ссылки на Postman

1. Запустите **Postman**.
2. Используйте следующие ссылки:
   - Запрос на запуск задачи:
     ```http
     POST http://localhost:8080/api/hash/crack
     ```

     ![image](https://github.com/user-attachments/assets/cc338399-2915-47e6-8277-eea23267c66a)

     В результате будет id задачи.

     ![image](https://github.com/user-attachments/assets/465f28de-2da5-4dc7-a7b1-716acfcb8d33)

   - Вставка полученного id в запрос и проверка статуса задачи:
     ```http
     GET http://localhost:8080/api/hash/status?requestId={id}
     ```
     Результат будет примерно такой
     ```
     {
        "status": "READY",
        "data": [
          "abxy"
        ]
     }
     ```

## 4. Изменение количества воркеров
1. Меняем WORKERS

![image](https://github.com/user-attachments/assets/f7fbc28f-daac-4818-ae94-f2c4817470d7)

2. Меняем replica

![image](https://github.com/user-attachments/assets/6ed7819a-9f14-4ed5-9de4-25043a7dcd4f)

> **Важно!** Перед изменением числа воркеров в `docker-compose.yml` нужно удалить старый контейнер `worker`.

### Очистка контейнера:
### Консоль
1. Остановите контейнер:
   ```sh
   docker stop worker
   ```
2. Удалите контейнер:
   ```sh
   docker rm worker
   ```
3. Перезапустите `docker-compose up -d`, чтобы применились новые настройки.
### Intellij 
1. DELETE контейнера в Intellij Idea:

![image](https://github.com/user-attachments/assets/dedcdb0b-5f43-436c-9f63-a1022a2d6c76)

2. Поменять число WORKERS и реплик и снова сделать деплой.

![image](https://github.com/user-attachments/assets/b4cd3deb-a852-4059-9b34-6ec9ebee8df0)



