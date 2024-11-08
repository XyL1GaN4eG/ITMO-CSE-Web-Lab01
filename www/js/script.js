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
const url = "/fcgi-bin/hello-world.jar"

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


// Запускаем функцию при загрузке страницы
window.addEventListener('load', fetchOnLoad);


// Функция для генерации таблицы
function generateTable(data) {
    const tableContainer = document.getElementById("table-container");

    // Проверяем, существует ли таблица, и создаем её, если её нет
    let table = tableContainer.querySelector("table");
    if (!table) {
        table = document.createElement("table");
        table.classList.add("data-table");

        // Создаем заголовок таблицы, используя ключи объекта
        const headerRow = document.createElement("tr");
        Object.keys(data[0]).forEach(key => {
            const th = document.createElement("th");
            th.textContent = key;
            headerRow.appendChild(th);
        });
        table.appendChild(headerRow);

        tableContainer.appendChild(table); // Добавляем таблицу в контейнер, если её еще нет
    }

    // Добавляем строки данных в таблицу
    data.forEach(rowData => addRowToTable(rowData, table));
}

// Функция для добавления одной строки данных в существующую таблицу
function addRowToTable(rowData, table) {
    const row = document.createElement("tr");
    Object.values(rowData).forEach(cellData => {
        const cell = document.createElement("td");
        cell.textContent = cellData;
        row.appendChild(cell);
    });
    table.appendChild(row);
}



// Функция для автоматического запроса при загрузке страницы
async function fetchOnLoad() {
    try {
        let response = await fetch(url, {
            method: "GET"
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // Получаем JSON из ответа
        const responseData = await response.json();
        console.log("Initial load response: ", responseData);

        // Вызываем функцию для создания таблицы из данных
        generateTable(responseData);
    } catch (error) {
        console.error("Fetch error on load: ", error);
    }
}


sendBtn.addEventListener("click", async (event) => {
    event.preventDefault();
    try {
        let x_values = [];
        document.querySelectorAll('#x-values > input:checked').forEach((element) => {
            x_values.push(element.value);
        });

        let obj = {
            x_array: x_values,
            y: yInput.value,
            r: rInput.value
        };
        console.log(JSON.stringify(obj));

        let response = await fetch(url, {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(obj)
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // Предполагаем, что сервер возвращает данные в формате JSON
        const responseData = await response.json();
        generateTable(responseData); // Добавляем данные в таблицу, не перезаписывая её
    } catch (error) {
        console.error("Fetch error: ", error);
    }
});


const resetBtn = document.getElementById('reset-btn'); // Находим кнопку сброса

// Добавляем обработчик события для сброса
resetBtn.addEventListener('click', async () => {

    const tableContainer = document.getElementById("table-container");
    tableContainer.innerHTML = ""; // Очистка контейнера с таблицей

    try {
        // Отправляем DELETE запрос на сервер
        const response = await fetch(url, {
            method: "DELETE", // Метод DELETE
            headers: {
                "Content-Type": "application/json", // Указываем, что отправляем JSON
            },
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
    } catch (error) {
        console.error("Delete request error: ", error);
    }
});


document.body.style.overflow = 'hidden'; // Отключаем прокрутку
