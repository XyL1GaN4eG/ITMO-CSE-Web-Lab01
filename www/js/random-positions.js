window.onload = function() {
    const gifs = document.querySelectorAll('.gifs img');
    const gifSize = 200; // Размер гифки (ширина и высота)

    gifs.forEach(gif => {
        let positionFound = false;
        let randomX, randomY;

        while (!positionFound) {
            // Генерация случайных координат
            randomX = Math.random() * (window.innerWidth - gifSize);
            randomY = Math.random() * (window.innerHeight - gifSize);

            // Проверка на пересечение
            positionFound = true;
            gifs.forEach(otherGif => {
                if (otherGif !== gif) {
                    const otherX = parseFloat(otherGif.style.left) || 0;
                    const otherY = parseFloat(otherGif.style.top) || 0;

                    // Проверка на пересечение
                    if (
                        randomX < otherX + gifSize &&
                        randomX + gifSize > otherX &&
                        randomY < otherY + gifSize &&
                        randomY + gifSize > otherY
                    ) {
                        positionFound = false; // Если пересекается, ищем новые координаты
                    }
                }
            });
        }

        // Применение случайных координат к стилям
        gif.style.left = randomX + 'px';
        gif.style.top = randomY + 'px';
    });
};