// 날짜 형식 변환
function formatRegDate(regDate) {
    const regDateObj = new Date(regDate);
    const now = new Date();
    const today = new Date(now.getFullYear(), now.getMonth(), now.getDate());
    const regDay = new Date(regDateObj.getFullYear(), regDateObj.getMonth(), regDateObj.getDate());

    // Intl.DateTimeFormat을 사용하여 날짜와 시간을 형식화
    const dateFormatter = new Intl.DateTimeFormat('ko-KR', {
        year: 'numeric',
        month: '2-digit',
        day: '2-digit'
    });
    const timeFormatter = new Intl.DateTimeFormat('ko-KR', {
        hour: '2-digit',
        minute: '2-digit'
    });

    // 오늘 날짜와 등록된 날짜가 같다면 시간만, 다르다면 날짜만 반환
    if (today.getTime() === regDay.getTime()) {
        return timeFormatter.format(regDateObj);
    } else {
        return dateFormatter.format(regDateObj);
    }
}

// 웹소켓 채팅을 활용 하기 위한 전역 변수

// 초기화 코드는 페이지 로드 시 한 번만 실행
const socket = new SockJS('/ws');
const stompClient = Stomp.over(socket);

// home.js에서 메시지 처리를 위한 함수를 전역으로 선언
window.handleReceivedCall = function(message) {
    // 추가적인 메시지 처리 로직...
};

stompClient.connect({}, function(frame) {
    console.log('Connected: ' + frame);
    stompClient.subscribe('/topic/calls', function(callMessage) {
        var callInfo = JSON.parse(callMessage.body);
        // 전역으로 선언된 메시지 처리 함수를 호출
        window.handleReceivedCall(callInfo);
    });
});

// 전역 변수로 설정하여 페이지 전체에서 사용 가능
window.stompClient = stompClient;