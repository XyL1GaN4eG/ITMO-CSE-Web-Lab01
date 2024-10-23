document.addEventListener('DOMContentLoaded', () => {
    // Инициализация
    const sendBtn = document.getElementById('send-btn');
    const yInput = document.getElementById('y-value');
    const rInput = document.getElementById('r-value');
    const yError = document.getElementById('y-error');
    const rError = document.getElementById('r-error');
    const xCheckboxes = document.querySelectorAll('#x-values input[type="checkbox"]');
    const yLowLimit = -5;
    const yHighLimit = 3;
    const rLowLimit = 1;
    const rHighLimit = 4;

    let isXValid = false;
    let isYValid = false;
    let isRValid = false;
    let yTouched = false; // Флаг для поля Y
    let rTouched = false; // Флаг для поля R

    // Функция для проверки чекбоксов X
    function validateX() {
        let isChecked = false;
        xCheckboxes.forEach(checkbox => {
            if (checkbox.checked) {
                isChecked = true;
            }
        });
        return isChecked;
    }

    // Универсальная функция для валидации числовых полей
    function validateVar(inputElement, errorElement, minValue, maxValue, touched) {
        return function () {
            const value = parseFloat(inputElement.value);
            if (touched && (isNaN(value) || value < minValue || value > maxValue)) {
                errorElement.textContent = "Введите число от " + minValue + " до " + maxValue;
                errorElement.style.display = "block";
                inputElement.classList.add('invalid');
                return false;
            } else {
                errorElement.style.display = "none";
                inputElement.classList.remove('invalid');
                return true;
            }
        };
    }

    // Общая функция валидации для всех полей
    function validateInputs() {
        isYValid = validateVar(yInput, yError, yLowLimit, yHighLimit, yTouched)();
        isRValid = validateVar(rInput, rError, rLowLimit, rHighLimit, rTouched)();
        isXValid = validateX();

        sendBtn.disabled = !(isRValid && isXValid && isYValid); // Блокируем кнопку, если что-то невалидно
    }

    // Добавляем обработчики событий
    yInput.addEventListener('input', () => {
        yTouched = true; // Флаг, что пользователь начал взаимодействовать с полем Y
        validateInputs();
    });

    rInput.addEventListener('input', () => {
        rTouched = true; // Флаг, что пользователь начал взаимодействовать с полем R
        validateInputs();
    });

    xCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', validateInputs);
    });

    // Начальная проверка полей, но без отображения ошибок
    validateInputs();
});
