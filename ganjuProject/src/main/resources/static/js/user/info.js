/* form 하기 전에 체크 */
let infoCheck = false;
function collectSelectedOptions() {
    const selectedOptions = [];
    document.querySelectorAll('.option').forEach(optionElement => {
        const optionIdInput = optionElement.querySelector('.menuOptionId');
        const optionId = optionIdInput ? optionIdInput.value : null; // input이 존재하면 그 value를 사용, 그렇지 않으면 null
        const selectedInputs = optionElement.querySelectorAll('input:checked');

        selectedInputs.forEach(input => {
            selectedOptions.push({
                optionId: optionId,
                valueId: input.value
            });
        });
    });
    return selectedOptions;
}

function infosubmit(form) {
    const selectedOptions = collectSelectedOptions();
    const quantity = document.getElementById('order-num').innerText;
    const menuId = form.menuId.value;

    // JSON 형태로 서버에 데이터 전송
    fetch('/menu/info', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            menuId: menuId,
            selectedOptions: selectedOptions,
            quantity: quantity,
        }),
    })
        .then(response => response.json())
        .then(data => {
            console.log('Success:', data);
            // 성공적으로 처리되었을 때의 로직
            window.location.href = "/menu/cart";
        })
        .catch((error) => {
            console.error('Error:', error);
            // 에러 처리 로직
            alert('처리 중 에러가 발생했습니다. 메인 화면으로 이동합니다.');
            // 메인 화면으로 리디렉션
            window.location.href = "/menu/main";
        });

    // 기본 폼 제출 방지
    event.preventDefault();
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
    updateTotalPrice();
});

// 플러스 버튼 눌렀을 때
plus.addEventListener('mousedown', e => {
    plusNum(); // 수량 증가
    updateTotalPrice();
});

function updateTotalPrice() {
    let totalPrice = parseInt(document.querySelector('.price').innerText); // 메뉴 기본 가격
    const quantity = parseInt(document.getElementById('order-num').innerText); // 선택된 수량

    // 선택된 옵션의 추가 가격 계산
    document.querySelectorAll('.option input:checked').forEach(input => {
        const priceElement = input.closest('.select').querySelector('.price');
        if (priceElement) {
            const optionPrice = parseInt(priceElement.innerText.replace('원', '').replace('+', ''));
            totalPrice += optionPrice;
        }
    });

    // 총 가격 = (기본 가격 + 옵션 가격) * 수량
    totalPrice *= quantity;

    // "담기" 버튼에 가격 업데이트
    document.querySelector('.submit.button').value = `${totalPrice}원 담기`;
}

// 옵션 선택 시 가격 업데이트
document.querySelectorAll('.option input').forEach(input => {
    input.addEventListener('change', updateTotalPrice);
});

// 초기 페이지 로드 시 총 가격 업데이트
document.addEventListener('DOMContentLoaded', updateTotalPrice);