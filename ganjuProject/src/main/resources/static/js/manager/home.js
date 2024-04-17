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
if (window.handleReceivedCall) {
    // 기존의 handleReceivedCall 함수를 백업
    const originalHandleReceivedCall = window.handleReceivedCall;

    // handleReceivedCall 함수 확장
    window.handleReceivedCall = function (callInfo) {
        // 기존 로직 호출
        originalHandleReceivedCall(callInfo);

        let callCount = document.querySelector('.order-list li:nth-child(1) span');
        let waitCount = document.querySelector('.order-list li:nth-child(2) span');
        let todayCount = document.querySelector('.today-sales p:first-child span');
        let todaySales = document.querySelector('.today-sales p:last-child span');
        if(callInfo.division === "CALL"){
            callCount.textContent = parseInt(callCount.textContent) +1;
        }else{
            waitCount.textContent = parseInt(waitCount.textContent)+1;
            todayCount.textContent = (parseInt(todayCount.textContent) +1)+'건';
            todaySales.textContent = (parseInt(todaySales.textContent) + callInfo.price).toLocaleString()+'원';
        }
    }
}
