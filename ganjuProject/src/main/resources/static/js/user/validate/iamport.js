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
let requestPayClick = true;
function requestPay() {
    if(requestPayClick) {
        requestPayClick = false;
        const contents = document.getElementById('contents');
        orderContent();
        addValidSession(contents);
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
            // m_redirect_url: "http://ganju.pe.kr:8081/user/payment/verify" // 모바일 결제 후 리디렉션될 URL
            m_redirect_url: "http://localhost:8081/user/payment/verify" // 모바일 결제 후 리디렉션될 URL
        }, function (rsp) {
            if (rsp.success) {
                // 결재 금액과 실제 금액이 같다면
                if (rsp.paid_amount == totalPayMoney) {
                    // 결제 성공 시 로직
                    fetch("/user/validImpUid", {
                        method: "POST",
                        headers: {
                            "Content-Type": "application/json",
                        },
                        body: JSON.stringify({
                            "impUid": rsp.imp_uid, // Iamport 결제 고유 번호
                        }),
                    })
                        .then(response => response.json())
                        .then(data => {
                            console.log(data.success);
                            if (data.success) {
                                // fetch 성공 후, 저장된 주문 정보를 WebSocket을 통해 전송
                                if (window.stompClient && window.stompClient.connected) {
                                    const orderInfo = {
                                        id: data.order.id, // 저장된 주문 ID
                                        restaurantTableNo: data.order.restaurantTableNo, // 테이블 번호
                                        orderMenus: data.order.orderMenus, // 주문한 메뉴 리스트
                                        price: data.order.price, // 총가격
                                        content: data.order.content, // 호출 내용
                                        regDate: data.order.regDate, // 등록 날짜
                                        division: data.order.division, // 호출인가?
                                        uid: data.order.uid // 호출인가?
                                    };

                                    console.log(orderInfo);
                                    stompClient.send("/app/calls", {}, JSON.stringify(orderInfo));
                                }
                                // 서버에서 결제 검증 성공 후 리디렉션할 페이지로 이동
                                location.href = "/user/order/" + data.order.id;
                            } else {
                                // 실패 로직 처리, 예: 사용자에게 알림
                                alert("결제 검증에 실패했습니다: " + data.message);
                                // 필요한 경우 환불 처리 로직 추가
                                validRefund(orderId);
                                location.href = "/user/main";
                            }
                        })
                        .catch(error => {
                            // 오류 처리 로직
                            alert("결제 검증에 실패했습니다. 다시 시도해주세요.");
                            validRefund(orderId);
                            location.href = "/user/main";
                        });
                } else {
                    alert("결제 검증에 실패했습니다. 금액이 맞지 않습니다");
                    validRefund(orderId);
                    location.href = "/user/main";
                }
            } else {
                // 결제 실패 시 로직,
                alert("결제에 실패하였습니다. 에러 내용: " + rsp.error_msg);
                validRefund(orderId);
                location.href = "/user/main";
            }
        });

        setTimeout( ()=>{
            requestPayClick = true;
        }, 3000);
    }
}

// 환불 처리 함수

function validRefund(orderId){
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
            if (data.success) {
                alert(data.message);
                location.href = "/user/main";
                // 환불 처리 후 페이지 리디렉션 또는 UI 업데이트
            } else {
                alert(data.message);
                location.href = "/user/main";
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert(error.message);
            location.href = "/user/main";
        });

    location.href = "/user/main";
}

// 모바일 결재 때문에 session에 가격하고 요구사항 저장하는 비동기 로직 추가
function addValidSession(contents){
    fetch('/user/addValidSession', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({
            "contents": contents.value, // 요청사항
            "totalPrice": totalPayMoney
        }),
    })
        .then(response => response.json())
        .then(data => {
            if (data.success) {
                console.log('session 저장 성공.');
                // 환불 처리 후 페이지 리디렉션 또는 UI 업데이트
            } else {
                console.log('session 저장 실패.');
                alert('session 저장 실패.');
                location.href = "/user/main";
            }
        })
        .catch(error => {
            console.error('Error:', error);
            alert('서버와의 통신 중 문제가 발생했습니다.');
            location.href = "/user/main";
        });
}