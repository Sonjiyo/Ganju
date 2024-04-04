const newR = document.querySelector('.newR');
const highR = document.querySelector('.highR');
const rowR = document.querySelector('.rowR');

newR.addEventListener('click', () => {
    if (!newR.classList.contains('on')) {
        newR.style.color = '#ff7a2f';
        newR.classList.add('on');
        highR.classList.remove('on');
        rowR.classList.remove('on');
        highR.style.color = '#222';
        rowR.style.color = '#222';
    }
})

highR.addEventListener('click', () => {
    if (!highR.classList.contains('on')) {
        highR.style.color = '#ff7a2f';
        highR.classList.add('on');
        newR.classList.remove('on');
        rowR.classList.remove('on');
        newR.style.color = '#222';
        rowR.style.color = '#222';
    }
})

rowR.addEventListener('click', () => {
    if (!rowR.classList.contains('on')) {
        rowR.style.color = '#ff7a2f';
        rowR.classList.add('on');
        newR.classList.remove('on');
        highR.classList.remove('on');
        newR.style.color = '#222';
        highR.style.color = '#222';
    }
})