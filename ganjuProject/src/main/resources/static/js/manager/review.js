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

// 리뷰 삭제 버튼
document.addEventListener('DOMContentLoaded', function () {
    const deleteButtons = document.querySelectorAll('.line a i.fa-times');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function () {
            const review = button.closest('.line');
            review.remove();
        })
    })
})

document.addEventListener("DOMContentLoaded", function () {
    const deleteBtn = document.querySelectorAll('.review_deleteBtn');
    deleteBtn.forEach(button => {
        button.addEventListener('click', function () {
            const reviewBtn = button.closest('.line');
            const id = button.dataset.id;
            if (id) {
                deleteReview(id, reviewBtn);
            } else {
                console.error("id 정의 되지 않음");
            }
        })
    })
})

function deleteReview(id, reviewBtn) {
    fetch(`/review/${id}`, {
        method: 'DELETE',
    }).then(response => {
        if (!response.ok) {
            throw new Error('삭제 실패');
        }
        reviewBtn.remove();
        updateRatingAndCount();
    }).catch(error => {
        console.error('삭제 실패함', error);
    });
}0

// 삭제 한 이후 새로운 평점 평균과 리뷰 개수
function updateRatingAndCount() {
    // 새로운 평균 평점과 리뷰 수를 계산합니다.
    const reviewElements = document.querySelectorAll('.review-content .line');
    let totalStars = 0;
    let totalReviews = reviewElements.length;
    reviewElements.forEach(review => {
        const stars = review.querySelectorAll('.star .fas.fa-star').length;
        totalStars += stars;
    });
    const averageRating = totalReviews > 0 ? totalStars / totalReviews : 0;

    // 평균 평점과 리뷰 수를 나타내는 HTML 요소를 업데이트합니다.
    const starAvgElement = document.querySelector('.review-all .big');
    const reviewCountElement = document.querySelector('.review-count .reviewCnt');
    if (starAvgElement && reviewCountElement) {
        starAvgElement.textContent = averageRating.toFixed(1); // 평균 평점 업데이트
        reviewCountElement.textContent = totalReviews + "개"; // 리뷰 수 업데이트
    }
}