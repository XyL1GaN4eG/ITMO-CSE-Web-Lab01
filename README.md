[![wakatime](https://wakatime.com/badge/github/XyL1GaN4eG/ITMO-CSE-Web-Lab01.svg)](https://wakatime.com/badge/github/XyL1GaN4eG/ITMO-CSE-Web-Lab01)
# Java FastCGI Web Application

## Описание

Этот проект представляет собой веб-приложение на Java, использующее протокол FastCGI для обработки HTTP-запросов. Приложение создано для проверки, попадает ли точка из запроса в заданную область, определяемую графиком. Оно принимает данные от клиента, обрабатывает их и возвращает результаты в формате JSON. Приложение разработано с акцентом на модульность и расширяемость, что позволяет легко добавлять новые функции и команды.

Данный проект является университетской лабораторной работой по дисциплине **Веб-программирование**.

![График областей](www/resources/graph.svg)

## Как работает

1. **Запуск приложения**: Приложение запускается из метода `main`, который инициализирует интерфейс FastCGI и обрабатывает входящие запросы в бесконечном цикле.
2. **Обработка запросов**: При получении запроса приложение определяет HTTP-метод (GET, POST, DELETE) и вызывает соответствующую команду.
3. **Валидация данных**: Входные данные проверяются на корректность, а результаты обработки возвращаются клиенту в формате JSON.
4. **История запросов**: Все обработанные запросы сохраняются в истории, что позволяет отслеживать их и при необходимости извлекать.

## Паттерны проектирования

В проекте использовались следующие паттерны проектирования:

- **Command Pattern**: Для обработки различных HTTP-запросов (POST, GET, DELETE) реализован интерфейс `HttpCommand`, который позволяет инкапсулировать команды и их выполнение.
- **Singleton Pattern**: Класс `History` реализует паттерн Singleton, обеспечивая единственный экземпляр для хранения истории обработанных запросов.
- **Adapter Pattern**: Класс `RequestDataAdapter` используется для преобразования JSON-данных в объект `RequestData`.

## Используемые библиотеки

- **Lombok**: Используется для упрощения кода и автоматической генерации геттеров, сеттеров и других методов.
- **Gson**: Библиотека для работы с JSON, используется для сериализации и десериализации объектов.
- **FastCGI**: Библиотека для работы с протоколом FastCGI, позволяющая обрабатывать HTTP-запросы.
- **Logback**: Для логирования.
