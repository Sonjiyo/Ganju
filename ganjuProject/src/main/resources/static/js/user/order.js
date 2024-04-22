const orderCheck = document.querySelector(".submit.button");

orderCheck.addEventListener("click", () =>{
    location.href = '/user/review/' + orderId; // 동적으로 리뷰 페이지 URL 생성
})

// 환불처리 스크립트
let refundBtnClick = true;
document.getElementById('refundButton').addEventListener('click', function() {
    if(refundBtnClick) {
        const restaurantName = document.querySelector('.restaurant-name');
        const orderId = restaurantName.dataset.orderId;
        if (confirm('정말로 환불하시겠습니까?')) {
            fetch('/user/validRefund', {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                },
                body: JSON.stringify({
                    orderId: orderId, // 서버 사이드에서 전달받은 주문 ID
                }),
            })
                .then(response => response.json())
                .then(data => {
                    console.log(data.success);
                    if (data.success) {
                        alert(data.message);
                        location.href = "/user/main";

                        // 환불 처리 후 페이지 리디렉션 또는 UI 업데이트
                    } else {
                        alert(data.message);
                    }
                })
                .catch(error => {
                    console.error('Error:', error);
                    alert(error.message);
                });
        }
        refundBtnClick = false;
        setTimeout( ()=>{
            refundBtnClick = true;
        }, 3000);
    }
});