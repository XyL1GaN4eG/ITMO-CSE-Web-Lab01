// main.js
import {generateTable} from './table.js';
import {validateInputs} from './validation.js';
import {setupEventListeners} from './events.js';

const sendBtn = document.getElementById('send-btn');
const yInput = document.getElementById('y-value');
const rInput = document.getElementById('r-value');
const yError = document.getElementById('y-error');
const rError = document.getElementById('r-error');
const xCheckboxes = document.querySelectorAll('#x-values input[type="checkbox"]');
const url = "/fcgi-bin/hello-world.jar";

validateInputs(yInput, yError, rInput, rError, xCheckboxes, false, false);

window.addEventListener('load', fetchOnLoad);

async function fetchOnLoad() {
    let response = await fetch(url, {
        method: "GET"
    });

    if (!response.ok) {
        throw new Error(`HTTP error! status: ${response.status}`);
    }

    const responseData = await response.json();

    generateTable(responseData);
}

sendBtn.addEventListener("click", async (event) => {
    event.preventDefault();
    let x_values = [];
    document.querySelectorAll('#x-values > input:checked').forEach((element) => {
        x_values.push(element.value);
    });

    let obj = {
        x_array: x_values,
        y: yInput.value,
        r: rInput.value
    };

    let response = await fetch(url, {
        method: "POST",
        headers: {"Content-Type": "application/json"},
        body: JSON.stringify(obj)
    });


    const responseData = await response.json();
    generateTable(responseData);
});

const resetBtn = document.getElementById('reset-btn');
resetBtn.addEventListener('click', async () => {
    const tableContainer = document.getElementById("table-container");
    tableContainer.innerHTML = "";

    await fetch(url, {
        method: "DELETE",
        headers: {
            "Content-Type": "application/json",
        },
    });

});

// Устанавливаем обработчики событий
setupEventListeners(sendBtn, yInput, rInput, xCheckboxes, yError, rError);

// Отключаем прокрутку
document.body.style.overflow = 'hidden';