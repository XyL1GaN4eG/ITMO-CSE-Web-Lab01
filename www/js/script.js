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
        if (touched) {
            if (isNaN(value) || value < minValue || value > maxValue) {
                errorElement.textContent = "Введите число от " + minValue + " до " + maxValue;
                errorElement.style.display = "block";
                inputElement.classList.add('invalid');
                return false;
            } else {
                errorElement.style.display = "none";
                inputElement.classList.remove('invalid');
                return true;
            }
        } else {
            return false
        }
    };
}

// Общая функция валидации для всех полей
function validateInputs() {
    isYValid = validateVar(yInput, yError, yLowLimit, yHighLimit, yTouched)();
    isRValid = validateVar(rInput, rError, rLowLimit, rHighLimit, rTouched)();
    isXValid = validateX();

    console.log(isXValid, isYValid, isRValid);
    sendBtn.disabled = !(isXValid && isYValid && isRValid); // Блокируем кнопку, если что-то невалидно
}

sendBtn.addEventListener("click", async (event) => {

    let x_values = [];

    document
        .querySelectorAll(
            '#x-values > input:checked'
        )
        .forEach((element)  => {
            x_values.push(
                element.value
            )}
        )

    let obj = {
        x_array: x_values,
        y: yInput.value,
        r: rInput.value
    }

    //todo: удалить консоль лог
    console.log(JSON.stringify(obj))
    let response = await fetch("/fcgi-bin/hello-world.jar", {
        method: "POST",
        body: JSON.stringify(obj)
    })

    console.log(await response.text())

})

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



