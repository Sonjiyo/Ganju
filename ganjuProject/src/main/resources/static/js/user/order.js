const orderCheck = document.querySelector(".submit.button");

orderCheck.addEventListener("click", () =>{
    location.href = '/menu/review/' + orderId; // 동적으로 리뷰 페이지 URL 생성
})

// 환불처리 스크립트
document.getElementById('refundButton').addEventListener('click', function() {
    const restaurantName = document.querySelector('.restaurant-name');
    const orderId = restaurantName.dataset.orderId;
    alert(orderId);
    if (confirm('정말로 환불하시겠습니까?')) {
        fetch('/menu/refund', {
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
                if (data.success) {
                    alert('환불 처리가 완료되었습니다.');
                    // 환불 처리 후 페이지 리디렉션 또는 UI 업데이트
                } else {
                    alert('환불 처리에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('서버와의 통신 중 문제가 발생했습니다.');
            });
    }
});