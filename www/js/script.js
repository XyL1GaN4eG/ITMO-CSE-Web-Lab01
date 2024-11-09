// main.js
import { generateTable } from './table.js';
import { validateInputs, yLowLimit, yHighLimit, rLowLimit, rHighLimit } from './validation.js';
import { setupEventListeners } from './events.js';

const sendBtn = document.getElementById('send-btn');
const yInput = document.getElementById('y-value');
const rInput = document.getElementById('r-value');
const yError = document.getElementById('y-error');
const rError = document.getElementById('r-error');
const xCheckboxes = document.querySelectorAll('#x-values input[type="checkbox"]');
const url = "/fcgi-bin/hello-world.jar";

// Начальная проверка полей, но без отображения ошибок
validateInputs(yInput, yError, rInput, rError, xCheckboxes, false, false);

// Запускаем функцию при загрузке страницы
window.addEventListener('load', fetchOnLoad);

// Функция для автоматического запроса при загрузке страницы
async function fetchOnLoad() {
    try {
        let response = await fetch(url, {
            method: "GET"
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        const responseData = await response.json();
        console.log("Initial load response: ", responseData);

        generateTable(responseData);
    } catch (error) {
        console.error("Fetch error on load: ", error);
    }
}

// Обработчик события для отправки данных
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

        const responseData = await response.json();
        generateTable(responseData);
    } catch (error) {
        console.error("Fetch error: ", error);
    }
});

// Обработчик события для сброса
const resetBtn = document.getElementById('reset-btn');
resetBtn.addEventListener('click', async () => {
    const tableContainer = document.getElementById("table-container");
    tableContainer.innerHTML = ""; // Очистка контейнера с таблицей

    try {
        const response = await fetch(url, {
            method: "DELETE",
            headers: {
                "Content-Type": "application/json",
            },
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }
    } catch (error) {
        console.error("Delete request error: ", error);
    }
});

// Устанавливаем обработчики событий
setupEventListeners(sendBtn, yInput, rInput, xCheckboxes, yError, rError);

// Отключаем прокрутку
document.body.style.overflow = 'hidden';