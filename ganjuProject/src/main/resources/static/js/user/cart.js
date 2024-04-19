        // 숫자 증감

const minus = document.querySelectorAll('.minus');
const plus = document.querySelectorAll('.plus');

// 수량 변경 시 session값을 업데이트
function updateValidSessionQuantity(menuId, newQuantity){
    console.log(menuId);
    console.log(newQuantity);
    // 서버에 수량 변경 요청 보내기
    fetch('/updateValidQuantity',{
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            menuId: menuId,
            quantity: newQuantity,
        }),
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
        })
        .catch((error) => {
            console.error('수량 변경 실패:', error);
        });
}
// 수량 변경 함수
function updateQuantityAndPrice(menuId, textElement, change) {
    const ordersDiv = textElement.closest('.orders');
    const menuPrice = parseInt(ordersDiv.dataset.menuPrice, 10);
    const optionPrice = parseInt(ordersDiv.dataset.optionPrice, 10);
    const totalPriceElement = ordersDiv.querySelector('.total-price');

    // 현재 수량 조절
    let quantity = parseInt(textElement.textContent, 10) + change;
    quantity = Math.max(1, Math.min(quantity, 100)); // 수량은 1 이상 100 이하로 제한
    textElement.textContent = quantity;

    updateValidSessionQuantity(menuId, quantity);

    // 새로운 총 가격 계산
    let newTotalPrice = (menuPrice + optionPrice) * quantity;
    totalPriceElement.textContent = '총 가격: '  + newTotalPrice + '원';

    // 새로운 총 가격 계산 후 전체 주문 가격 업데이트
    updateTotalOrderPrice();

    // 버튼 상태 업데이트 로직
    const minusButton = textElement.closest('.num').querySelector('.minus');
    const trashIcon = textElement.closest('.num').querySelector('.fas.fa-trash');
    const plusButton = textElement.closest('.num').querySelector('.plus');

    if (quantity === 1) {
        minusButton.style.display = 'none';
        trashIcon.style.display = 'inline-block';
    } else {
        minusButton.style.display = 'inline-block';
        trashIcon.style.display = 'none';
    }

    if (quantity === 100) {
        plusButton.disabled = true;
        plusButton.classList.add('disabled-button');
    } else {
        plusButton.disabled = false;
        plusButton.classList.remove('disabled-button');
    }
}
// 휴지통 버튼 클릭 이벤트
let faTrash = true;
document.querySelectorAll('.fas.fa-trash').forEach(icon => {
    icon.addEventListener('click', function() {
        if(faTrash) {
            const ordersDiv = icon.closest('.orders');
            const menuId = ordersDiv.dataset.menuId;
            removeOrder(menuId, ordersDiv); // 주문 목록에서 해당 메뉴 제거 함수 호출
            faTrash = false;
            setTimeout( ()=>{
                faTrash = true;
            }, 1000);
        }
    });
});

function removeOrder(menuId, ordersDiv) {
    // 서버에 주문 항목 삭제 요청 보내기
    fetch(`/removeValidOrder?menuId=${menuId}`, { // URL에 menuId를 쿼리 매개변수로 추가
        method: 'POST',
    })
        .then(response => response.json())
        .then(data => {
            console.log(data);
            ordersDiv.remove(); // 페이지에서 해당 주문 항목 삭제
            updateTotalOrderPrice(); // 전체 주문 가격 업데이트
            // 버튼을 비활성화
            document.querySelector('.submit.button').disabled = true;
            document.querySelector('.submit.button').classList.add('disabled-button');
        })
        .catch((error) => {
            console.error('주문 항목 삭제 실패:', error);
        });
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
        const menuId = button.closest('.orders').dataset.menuId;
        updateQuantityAndPrice(menuId, text, -1); // 수량 감소
    });
});

plus.forEach(button => {
    button.addEventListener('click', () => {
        const text = button.closest('.num').querySelector('.text');
        const menuId = button.closest('.orders').dataset.menuId;
        updateQuantityAndPrice(menuId, text, 1); // 수량 증가
    });
});

document.addEventListener('DOMContentLoaded', () => {
    // 전체 주문 가격을 계산하고 버튼에 반영
    updateTotalOrderPrice();

    // 각 주문 항목의 수량에 따라 버튼 상태 초기화
    document.querySelectorAll('.orders').forEach(ordersDiv => {
        const quantityTextElement = ordersDiv.querySelector('.num .text');
        const quantity = parseInt(quantityTextElement.textContent, 10);
        const minusButton = ordersDiv.querySelector('.minus');
        const trashIcon = ordersDiv.querySelector('.fas.fa-trash'); // 휴지통 아이콘 선택자를 적절히 조정하세요.
        const plusButton = ordersDiv.querySelector('.plus');

        // 수량이 1일 경우
        if (quantity === 1) {
            minusButton.style.display = 'none';
            trashIcon.style.display = 'inline-block'; // 휴지통 아이콘 보이게 설정
        } else {
            minusButton.style.display = 'inline-block';
            trashIcon.style.display = 'none'; // 휴지통 아이콘 숨김
        }

        // 수량이 100일 경우
        if (quantity === 100) {
            plusButton.disabled = true;
            plusButton.classList.add('disabled-button'); // 비활성화 스타일 적용
        } else {
            plusButton.disabled = false;
            plusButton.classList.remove('disabled-button'); // 비활성화 스타일 제거
        }
    });

    // 메뉴 추가 버튼 클릭 이벤트
    const menuPlus = document.querySelector(".menu-plus");
    menuPlus.addEventListener('click', () => {
        location.href = '/menu/main';
    });
});