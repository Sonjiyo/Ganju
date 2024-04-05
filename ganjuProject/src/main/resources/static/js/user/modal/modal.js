
// 신고하기 버튼 클릭 이벤트
document.querySelector('.report-button').addEventListener('click', () => {
    action();
    document.getElementById('reportModal').style.display = 'block';
});

// 신고하기 버튼 클릭 이벤트로 모달 창 닫기 (옵션)
document.querySelector('.modal-submit').addEventListener('click', () => {
    document.getElementById('reportModal').style.display = 'none';
    document.body.style.overflow = ''; // 스크롤 활성화
});

// 호출하기 버튼 클릭 이벤트
document.querySelector('.call-button').addEventListener('click', () => {
    action();
    document.getElementById('callModal').style.display = 'block';
});

// 호출하기 모달에서 호출하기 버튼 클릭 이벤트
document.getElementById('submitCall').addEventListener('click', () => {
    // 실제 애플리케이션에서는 이곳에 선택된 옵션을 처리하는 로직을 구현합니다.
    // 예: 선택된 라디오 버튼의 값을 서버로 전송
    console.log('호출 옵션:', document.querySelector('input[name="callOption"]:checked').value);
    document.getElementById('callModal').style.display = 'none'; // 모달 닫기
    document.body.style.overflow = ''; // 스크롤 활성화
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