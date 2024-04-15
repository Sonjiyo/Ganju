// 숫자 증감

const minus = document.querySelectorAll('.minus');
const plus = document.querySelectorAll('.plus');

// 수량 변경 함수
function updateQuantityAndPrice(textElement, change) {
    const ordersDiv = textElement.closest('.orders');
    const menuPrice = parseInt(ordersDiv.dataset.menuPrice, 10);
    const optionPrice = parseInt(ordersDiv.dataset.optionPrice, 10);
    const totalPriceElement = ordersDiv.querySelector('.total-price');

    // 현재 수량 조절
    let quantity = parseInt(textElement.textContent, 10) + change;
    quantity = Math.max(1, Math.min(quantity, 100)); // 수량은 1 이상 100 이하로 제한
    textElement.textContent = quantity;

    // 새로운 총 가격 계산
    let newTotalPrice = (menuPrice + optionPrice) * quantity;
    totalPriceElement.textContent = '총 가격: '  + newTotalPrice + '원';

    // 새로운 총 가격 계산 후 전체 주문 가격 업데이트
    updateTotalOrderPrice();
}

// 총 가격 실시간 계산 함수
function updateTotalOrderPrice() {
    const orderDivs = document.querySelectorAll('.orders');
    let totalOrderPrice = 0;

    orderDivs.forEach(div => {
        const quantity = parseInt(div.querySelector('.num .text').textContent, 10);
        const menuPrice = parseInt(div.dataset.menuPrice, 10);
        const optionPrice = parseInt(div.dataset.optionPrice, 10);
        totalOrderPrice += (menuPrice + optionPrice) * quantity;
    });

    // 주문하기 버튼의 값을 업데이트
    const submitButton = document.querySelector('.submit.button');
    submitButton.value = `${totalOrderPrice}원 주문하기`;
}

// 'minus'와 'plus' 버튼에 이벤트 리스너 추가
minus.forEach(button => {
    button.addEventListener('click', () => {
        const text = button.closest('.num').querySelector('.text');
        updateQuantityAndPrice(text, -1); // 수량 감소
    });
});

plus.forEach(button => {
    button.addEventListener('click', () => {
        const text = button.closest('.num').querySelector('.text');
        updateQuantityAndPrice(text, 1); // 수량 증가
    });
});

document.addEventListener('DOMContentLoaded', () => {
    updateTotalOrderPrice(); // 페이지 로드 시 전체 주문 가격을 계산하고 버튼에 반영
});


// 메뉴 추가 버튼 클릭 시 메인으로
const menuPlus = document.querySelector(".menu-plus");

menuPlus.addEventListener('click', () =>{
    location.href = '/menu/main';
});