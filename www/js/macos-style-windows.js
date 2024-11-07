document.querySelectorAll('.mac-os-control-wrapper').forEach((contentWrapper) => {

    try {
        // Создаем основной контейнер окна
        const macWindow = document.createElement('div');
        macWindow.classList.add('macos-window');

        // Создаем панель заголовка с кнопками
        const titleBar = document.createElement('div');
        titleBar.classList.add('macos-title-bar');

        // Создаем кнопки
        const controlButtons = document.createElement('div');
        controlButtons.classList.add('macos-control-buttons');

        const closeButton = document.createElement('span');
        closeButton.classList.add('macos-control-button', 'macos-close');
        const minimizeButton = document.createElement('span');
        minimizeButton.classList.add('macos-control-button', 'macos-minimize');
        const fullscreenButton = document.createElement('span');
        fullscreenButton.classList.add('macos-control-button', 'macos-fullscreen');

        controlButtons.append(closeButton, minimizeButton, fullscreenButton);
        titleBar.appendChild(controlButtons);

        if (contentWrapper.parentElement.classList.contains('control-block')) {
            macWindow.style.height = '350px'; // Устанавливаем высоту 350px
        }

        // Создаем контейнер для содержимого
        const contentContainer = document.createElement('div');
        contentContainer.classList.add('macos-content');

        // Перемещаем оригинальный контент в macOS-окно
        contentContainer.innerHTML = contentWrapper.innerHTML;
        contentWrapper.innerHTML = ''; // Очищаем исходный контейнер
        contentWrapper.appendChild(macWindow); // Добавляем "окно" в исходный контейнер

        // Добавляем элементы в macOS-окно
        macWindow.append(titleBar, contentContainer);
    } catch {
        alert("Zavupa")
    }
});