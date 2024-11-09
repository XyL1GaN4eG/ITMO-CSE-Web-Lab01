import { validateInputs } from './validation.js';

export function setupEventListeners(sendBtn, yInput, rInput, xCheckboxes, yError, rError) {
    let yTouched = false;
    let rTouched = false;
    sendBtn.disabled = true;

    yInput.addEventListener('input', () => {
        yTouched = true;
        const { isYValid, isRValid, isXValid } = validateInputs(yInput, yError, rInput, rError, xCheckboxes, yTouched, rTouched);
        sendBtn.disabled = !(isYValid && isRValid && isXValid);
    });

    rInput.addEventListener('input', () => {
        rTouched = true;
        const { isYValid, isRValid, isXValid } = validateInputs(yInput, yError, rInput, rError, xCheckboxes, yTouched, rTouched);
        sendBtn.disabled = !(isYValid && isRValid && isXValid);
    });

    xCheckboxes.forEach(checkbox => {
        checkbox.addEventListener('change', () => {
            const { isYValid, isRValid, isXValid } = validateInputs(yInput, yError, rInput, rError, xCheckboxes, yTouched, rTouched);
            sendBtn.disabled = !(isYValid && isRValid && isXValid);
        });
    });
}