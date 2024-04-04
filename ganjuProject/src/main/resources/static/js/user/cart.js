// form 관련
function cartsubmit(form){
    form.submit();
}

// 숫자 증감

const minus = document.querySelectorAll('.minus');
const plus = document.querySelectorAll('.plus');

// 수량 증가 함수
function plusNum(text) {
    let num = parseInt(text.innerHTML);
    if (num < 100) {
        num += 1;
        text.innerHTML = num;
    }
}

// 수량 감소 함수
function minusNum(text) {
    let num = parseInt(text.innerHTML);
    if (num > 1) {
        num -= 1;
        text.innerHTML = num;
    }
}

minus.forEach( e =>{
    e.addEventListener('mousedown', () => {
        // 'text' 요소를 이 버튼과 가장 가까운 상위 요소에서 찾습니다.
        const text = e.closest('.num').querySelector('.text');
        minusNum(text); // 수량 감소
    });
});

plus.forEach( e =>{
    e.addEventListener('mousedown', () => {
        // 'text' 요소를 이 버튼과 가장 가까운 상위 요소에서 찾습니다.
        const text = e.closest('.num').querySelector('.text');
        plusNum(text); // 수량 증가
    });
});


// 메뉴 추가 버튼 클릭 시 메인으로
const menuPlus = document.querySelector(".menu-plus");

menuPlus.addEventListener('click', () =>{
    location.href = '/user/main';
});