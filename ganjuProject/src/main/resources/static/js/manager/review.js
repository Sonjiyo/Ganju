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
        sortReviews('new');
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
        sortReviews('high')
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
        sortReviews('low')
    }
})

document.addEventListener('DOMContentLoaded', function () {
    // 페이지가 로드될 때 최신순으로 정렬
    sortReviews('new');
});

// 각 버튼에 대한 이벤트 리스너 추가
document.querySelector('.newR').addEventListener('click', function () {
    sortReviews('new');
});
document.querySelector('.highR').addEventListener('click', function () {
    sortReviews('high');
});
document.querySelector('.rowR').addEventListener('click', function () {
    sortReviews('low');
});

function sortReviews(order) {
    const reviews = document.querySelector('.review-content');
    const lines = Array.from(reviews.querySelectorAll('.line'));
    lines.sort(function (a, b) {
        if (order === 'new') {
            const dateA = new Date(a.querySelector('.regDate').textContent.split(' ')[0]);
            const dateB = new Date(b.querySelector('.regDate').textContent.split(' ')[0]);
            return dateB - dateA; // 최신순으로 정렬
        } else if (order === 'high') {
            return b.querySelector('.star').querySelectorAll('.fas').length - a.querySelector('.star').querySelectorAll('.fas').length;
        } else if (order === 'low') {
            return a.querySelector('.star').querySelectorAll('.fas').length - b.querySelector('.star').querySelectorAll('.fas').length;
        }
    });
    // 정렬된 리뷰를 다시 화면에 추가
    reviews.innerHTML = '';
    lines.forEach(function (line) {
        reviews.appendChild(line);
    });
}