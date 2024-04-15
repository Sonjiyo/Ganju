// Iamport 결제 라이브러리 초기화
var IMP = window.IMP; // 생략 가능
IMP.init("imp43865385"); // 발급받은 "가맹점 식별코드"를 사용

let menuName;
let totalPayMoney;

function orderContent(){
    // 메뉴 이름(1~n개)하고 총 가격만 불러옴
    const orderList = document.querySelectorAll('.orders');
    let menuNames = [];

    orderList.forEach((order) => {
        // 각 주문에서 메뉴 이름 추출
        const menuName = order.querySelector('.title').textContent;
        menuNames.push(menuName);
    });

    menuName = menuNames[0];

    if(menuNames.length > 1){
        menuName = menuNames[0] + " 외 " + (menuNames.length-1) + "개";
    }

// 총 가격 추출
    const totalPayButton = document.querySelector('.submit.button').value;
    totalPayMoney = totalPayButton.split("원")[0];
}

function requestPay() {
    orderContent();
    console.log(menuName); // 메뉴 이름 출력
    console.log(totalPayMoney); // 총 가격 출력
    // 결제 정보 준비
    IMP.request_pay({
        pg: "html5_inicis", // PG사
        pay_method: "card", // 결제 수단
        merchant_uid: "order_" + new Date().getTime(), // 주문번호
        name: menuName, // 결제창에서 보여질 이름
        amount: totalPayMoney, // 결제 금액
        // buyer_email: "iamport@siot.do",
        // buyer_name: "구매자이름",
        // buyer_tel: "010-1234-5678", // 구매자 전화번호
        // buyer_addr: "서울특별시 강남구 삼성동",
        // buyer_postcode: "123-456", // 구매자 우편번호
        // m_redirect_url: "/menu/order" // 모바일 결제 후 리디렉션될 URL
    }, function (rsp) {
        console.log(rsp);
        if (rsp.success) {
            // 결제 성공 시 로직,
            location.href = "/menu/order?imp_uid=" + rsp.imp_uid; // 성공시 리디렉션할 URL
        } else {
            // 결제 실패 시 로직,
            alert("결제에 실패하였습니다. 에러 내용: " + rsp.error_msg);
        }
    });
}