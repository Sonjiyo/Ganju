/* form 하기 전에 체크 */

function infosubmit(form) {
    form.submit();
}

// 수량 증감 액션
const minus = document.querySelector('.minus');
const plus = document.querySelector('.plus');
const text = document.querySelector('.count .inner .num .text');

// 수량 증가 함수
function plusNum() {
    let num = parseInt(text.innerHTML);
    if (num < 100) {
        num += 1;
        text.innerHTML = num;
    }
}

// 수량 감소 함수
function minusNum() {
    let num = parseInt(text.innerHTML);
    if (num > 1) {
        num -= 1;
        text.innerHTML = num;
    }
}

// 마이너스 버튼 눌렀을 때
minus.addEventListener('mousedown', e => {
    minusNum(); // 수량 감소
});

// 플러스 버튼 눌렀을 때
plus.addEventListener('mousedown', e => {
    plusNum(); // 수량 증가
});