const orderCheck = document.querySelector(".submit.button");

orderCheck.addEventListener("click", () =>{
    location.href = '/menu/review/' + orderId; // 동적으로 리뷰 페이지 URL 생성
})