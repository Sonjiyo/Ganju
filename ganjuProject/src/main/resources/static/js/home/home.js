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