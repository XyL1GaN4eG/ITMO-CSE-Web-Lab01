export const yLowLimit = -5;
export const yHighLimit = 3;
export const rLowLimit = 1;
export const rHighLimit = 4;

export function validateX(xCheckboxes) {
    return Array.from(xCheckboxes).some(checkbox => checkbox.checked);
}

export function validateVar(inputElement, errorElement, minValue, maxValue, touched) {
    const value = parseFloat(inputElement.value);
    if (touched) {
        if (isNaN(value) || value < minValue || value > maxValue) {
            errorElement.textContent = `Введите число от ${minValue} до ${maxValue}`;
            errorElement.style.display = "block";
            inputElement.classList.add('invalid');
            return false;
        } else {
            errorElement.style.display = "none";
            inputElement.classList.remove('invalid');
            return true;
        }
    }
    return false;
}

export function validateInputs(yInput, yError, rInput, rError, xCheckboxes, yTouched, rTouched) {
    const isYValid = validateVar(yInput, yError, yLowLimit, yHighLimit, yTouched);
    const isRValid = validateVar(rInput, rError, rLowLimit, rHighLimit, rTouched);
    const isXValid = validateX(xCheckboxes);

    return { isYValid, isRValid, isXValid };
}