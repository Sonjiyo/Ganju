// 별 클릭시 색 변하는 이벤트
const stars = document.querySelectorAll('.star-span span');
const starInput = document.querySelector('.star-span .star');
const nameInput = document.querySelector('.nickname .name');
const contentInput = document.querySelector('.review .content');
const nameError = document.getElementById('nameError');
const contentError = document.getElementById('contentError');


// 리뷰 작성
function reviewCheck(form){
    let isValid = true;
    if (nameInput.value.trim() === '') {
        nameError.style.display = 'block';
        isValid = false;
    } else {
        nameError.style.display = 'none';
    }
    if (contentInput.value.trim() === '') {
        contentError.style.display = 'block';
        isValid = false;
    } else {
        contentError.style.display = 'none';
    }

    if (isValid) {
        document.getElementById('reviewForm').submit();
    }
}

stars.forEach( (e, idx) =>{
    e.addEventListener('click', () =>{
        updateStars(idx);
    })
});

// 별을 i 번째 만큼 색 바꿈
function updateStars(idx){
    stars.forEach((e, i) => {
        starInput.value = idx + 1;
        e.style.color = i <= idx ? '#FFBE3F' : '#c2c2c2';
    });
}


// 페이지 로드 시 초기 별표 설정
document.addEventListener('DOMContentLoaded', function() {
    updateStars(0);
});