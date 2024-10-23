document.addEventListener('DOMContentLoaded', () => {

    const sendBtn = document.getElementById('send-btn');
    const yInput = document.getElementById('y-value');
    const rInput = document.getElementById('r-value');
    const yError = document.getElementById('y-error');
    const rError = document.getElementById('r-error');

    // Флаги для отслеживания, взаимодействовал ли пользователь с полем
    let yTouched = false;
    let rTouched = false;

    // Функция для проверки валидности Y
    function validateY() {
        const yValue = parseFloat(yInput.value);
        if ((yTouched || yInput.value !== "") && (isNaN(yValue) || yValue < -5 || yValue > 3)) {
            yError.textContent = "Введите число от -5 до 3";
            yError.style.display = "block"; // Показываем ошибку, если поле некорректно
            yInput.classList.add('invalid');
            return false; // Неправильное значение
        } else {
            yError.style.display = "none"; // Скрываем ошибку
            yInput.classList.remove('invalid');
            return true; // Корректное значение
        }
    }

    // Функция для проверки валидности R
    function validateR() {
        const rValue = parseFloat(rInput.value);
        if ((rTouched || rInput.value !== "") && (isNaN(rValue) || rValue < 1 || rValue > 4)) {
            rError.textContent = "Введите число от 1 до 4";
            rError.style.display = "block"; // Показываем ошибку, если поле некорректно
            rInput.classList.add('invalid');
            return false; // Неправильное значение
        } else {
            rError.style.display = "none"; // Скрываем ошибку
            rInput.classList.remove('invalid');
            return true; // Корректное значение
        }
    }

    // Общая функция валидации для обоих полей
    function validateInputs() {
        const isYValid = validateY();
        const isRValid = validateR();

        // Блокируем или разблокируем кнопку "Отправить" в зависимости от валидации
        sendBtn.disabled = !(isYValid && isRValid); // Кнопка разблокируется, если оба поля валидны
    }

    // Добавляем событие фокусировки на поле Y
    yInput.addEventListener('focus', () => {
        yTouched = true; // Устанавливаем флаг, что поле Y было затронуто
        validateInputs(); // Проверяем значения
    });

    // Добавляем событие фокусировки на поле R
    rInput.addEventListener('focus', () => {
        rTouched = true; // Устанавливаем флаг, что поле R было затронуто
        validateInputs(); // Проверяем значения
    });

    // Добавляем обработчики событий на ввод для Y и R
    yInput.addEventListener('input', validateInputs);
    rInput.addEventListener('input', validateInputs);

    // Начальная проверка для блокировки кнопки при загрузке страницы
    validateInputs();
});
