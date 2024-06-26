
// 신고하기 버튼 클릭 이벤트
let reportBtnClick = true;
document.querySelector('.report-button').addEventListener('click', () => {
    if(reportBtnClick){
        action();
        document.getElementById('reportModal').style.display = 'block';

        reportBtnClick = false;
        setTimeout( ()=>{
            reportBtnClick = true;
        }, 1000);
    }
});

// 신고하기 버튼 클릭 이벤트로 모달 창 닫기 (옵션)
let reportCallBtnClick = true;
document.getElementById('reportCall').addEventListener('click', () => {
    if(reportCallBtnClick) {
        const content = document.getElementById('reportReason').value;

        if (content == '') {
            alert("내용이 없습니다");
            return false;
        } else {
            console.log(content);
            fetch('/user/validUserReport', {
                method: 'POST',
                headers: {
                    'Content-Type': 'text/plain',
                },
                body: content,
            })
                .then(response => response.json())
                .then(data => {
                    console.log("신고 성공");
                })
                .catch((error) => {
                    console.error('Error:', error);
                });

            document.getElementById('reportModal').style.display = 'none';
            document.getElementById('reportReason').value = '';
            document.body.style.overflow = ''; // 스크롤 활성화
        }

        reportCallBtnClick = false;
        setTimeout( ()=>{
            reportCallBtnClick = true;
        }, 1000);
    }
});

// 호출하기 버튼 클릭 이벤트
let callBtnClick = true;
document.querySelector('.call-button').addEventListener('click', () => {
    if(callBtnClick){
        action();
        document.getElementById('callModal').style.display = 'block';

        callBtnClick = false;
        setTimeout( ()=>{
            callBtnClick = true;
        }, 1000)
    }
});

// 호출하기 모달에서 호출하기 버튼 클릭 이벤트
let submitCallBtnClick = true;
document.getElementById('submitCall').addEventListener('click', () => {
    if(submitCallBtnClick) {
        // 실제 애플리케이션에서는 이곳에 선택된 옵션을 처리하는 로직을 구현합니다.
        const selectedOption = document.querySelector('input[name="callOption"]:checked').value;
        console.log('호출 옵션:', selectedOption);

        // WebSocket을 통해 서버로 선택된 옵션 정보 전송
        fetch('/user/validUserCall', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: selectedOption,
        })
            .then(response => response.json())
            .then(data => {
                // fetch 성공 후, 저장된 주문 정보를 WebSocket을 통해 전송
                if (window.stompClient && window.stompClient.connected) {
                    const orderInfo = {
                        id: data.order.id, // 저장된 주문 ID
                        content: data.order.content, // 호출 내용
                        restaurantTableNo: data.order.restaurantTableNo, // 테이블 번호
                        regDate: data.order.regDate, // 등록 날짜
                        division: data.order.division, // 호출인가?
                        restaurantId: data.order.restaurantId
                    };

                    stompClient.send("/app/calls", {}, JSON.stringify(orderInfo));
                }
                console.log("성공");
            })
            .catch((error) => {
                console.error('Error:', error);
            });

        document.getElementById('callModal').style.display = 'none'; // 모달 닫기
        document.body.style.overflow = ''; // 스크롤 활성화

        submitCallBtnClick = false;
        setTimeout( ()=>{
            submitCallBtnClick =true;
        },1000);
    }
});

// 모달 창 밖을 클릭할 때 모달 창 닫기
window.addEventListener('click', e => {

    if (e.target.classList.contains('modal')) {
        e.target.style.display = 'none';
        document.body.style.overflow = ''; // 스크롤 활성화
    }
});

function action(){
    window.scrollTo(0, 0); // 스크롤을 맨 위로 이동
    document.body.style.overflow = 'hidden'; // 스크롤 비활성화
}