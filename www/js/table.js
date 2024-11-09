export function generateTable(data) {
    const tableContainer = document.getElementById("table-container");
    let table = tableContainer.querySelector("table");

    if (!table) {
        table = document.createElement("table");
        table.classList.add("data-table");

        const headerRow = document.createElement("tr");
        Object.keys(data[0]).forEach(key => {
            const th = document.createElement("th");
            th.textContent = key;
            headerRow.appendChild(th);
        });
        table.appendChild(headerRow);
        tableContainer.appendChild(table);
    }

    data.forEach(rowData => addRowToTable(rowData, table));
}

function addRowToTable(rowData, table) {
    const row = document.createElement("tr");
    Object.values(rowData).forEach(cellData => {
        const cell = document.createElement("td");
        cell.textContent = cellData;
        row.appendChild(cell);
    });
    table.appendChild(row);
}