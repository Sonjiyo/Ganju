function ratingStar() {
    const rating = document.querySelector('.ratings');
    const stars = rating.querySelectorAll('.stars span i');
    const label = rating.querySelector('label');
    const ratingValue = parseFloat(label.textContent);

    // 별 아이콘 초기화
    stars.forEach(star => {
        star.classList.remove('fas', 'fa-star', 'fa-star-half-alt');
        star.classList.add('far', 'fa-star');
        star.style.color = 'var(--light-gray)'; // 기본 별 색상
    });

    // 평점에 따라 별 아이콘 적용
    for (let i = 0; i < stars.length; i++) {
        if ((ratingValue - i) > 1) {
            stars[i].classList.remove('far', 'fa-star-half-alt');
            stars[i].classList.add('fas', 'fa-star');
            stars[i].style.color = 'var(--gold)'; // 채워진 별 색상
        } else if ((ratingValue - i) >= 0.5) {
            stars[i].classList.remove('far', 'fa-star');
            stars[i].classList.add('fas', 'fa-star-half-alt');
            stars[i].style.color = 'var(--gold)'; // 반 채워진 별 색상
            break;
        }
    }
}

document.addEventListener('DOMContentLoaded', () => {
    // 별포 표시 부분 함수
    ratingStar();
});