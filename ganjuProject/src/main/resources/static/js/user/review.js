// 별 클릭시 색 변하는 이벤트
const stars = document.querySelectorAll('.star-span span');
const starInput = document.querySelector('.star-span .star');
const nameInput = document.querySelector('.nickname .name');
const contentInput = document.querySelector('.review .content');
const nameError = document.getElementById('nameError');
const contentError = document.getElementById('contentError');

const submitButton = document.querySelector('.submit.button'); // 제출 버튼 선택

// 리뷰 작성
function reviewCheck(){
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
        submitButton.disabled = true; // 유효하면 버튼을 비활성화
        submitButton.value = '처리 중...'; // 버튼 텍스트 변경 (옵션)
        document.getElementById('reviewForm').submit();
    } else {
        submitButton.disabled = false; // 유효하지 않으면 버튼을 다시 활성화 (필요한 경우)
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


    // 입력 필드에 대한 입력 이벤트 리스너 추가
    nameInput.addEventListener('input', function() {
        if (nameInput.value.trim() !== '') {
            nameError.style.display = 'none';
        }
    });

    contentInput.addEventListener('input', function() {
        if (contentInput.value.trim() !== '') {
            contentError.style.display = 'none';
        }
    });


    // 리뷰 제출 버튼 클릭 이벤트 리스너 추가
    submitButton.addEventListener('click', function() {
        reviewCheck();
    });
});

