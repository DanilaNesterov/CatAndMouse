# Описание

Это приложение для Android на языке Kotlin, которое позволяет кошкам играть в игру с нажатиями на мышей, а хозяева могут отслеживать игровую статистику. Оно построено с использованием современных инструментов разработки Android, таких как Jetpack Compose, Hilt для внедрения зависимостей и Room для хранения данных.

## Функции

- Игра с нажатиями, с динамическими элементами (мыши, которые двигаются по экрану)
- Отслеживание игровой статистики (количество нажатий, точность, счет и продолжительность игры)
- Постоянное хранение статистики игры с помощью Room
- Внедрение зависимостей с использованием Hilt
- Интерфейс, построенный с использованием Jetpack Compose
- Корутины для выполнения фоновых задач
- SplashScreen, отображаемый при запуске приложения

## Скриншоты

![Скриншот 1](https://github.com/user-attachments/assets/d93cddb9-d8cb-42ca-a215-c450d46d607c)  

![Скриншот 2](https://github.com/user-attachments/assets/b9d98bbb-e0f1-47be-b42f-0f90bc3fffd2)  

![Скриншот 3](https://github.com/user-attachments/assets/f9b0561c-24da-4509-b9e7-083194ee699b)  

![Скриншот 4](https://github.com/user-attachments/assets/7ceb9d44-b8c2-4124-8304-03e3bbf7a850)

## Архитектура

Проект использует архитектуру MVVM (Model-View-ViewModel), что обеспечивает разделение ответственности и удобство поддержки.

- **ViewModel:** `GameViewModel` управляет состоянием игры и взаимодействует с репозиторием.
- **Репозиторий:** `GameStatRepository` предоставляет данные из базы данных Room и взаимодействует с ViewModel.
- **UI:** Для создания реактивного интерфейса используется Jetpack Compose.

## Используемые библиотеки=

- **Jetpack Compose**: Для создания современных реактивных пользовательских интерфейсов.
- **Room**: Для управления локальной базой данных.
- **Hilt**: Для внедрения зависимостей.
- **Kotlin Coroutines**: Для управления фоновой работой.

