document.getElementById("addNoticeButton").addEventListener("click", async function () {
    const noticeTitle = document.querySelector('.notice-title .notice-menu textarea').value.trim();
    const noticeContents = document.querySelector('.notice-contents .notice-menu textarea').value.trim();
    if (!noticeTitle || !noticeContents) {
        alert("제목 또는 내용을 입력해주세요");
        return;
    }
    const noticeData = {
        title: noticeTitle,
        contents: noticeContents,
    };
    fetch("/manager/addNotice", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(noticeData)
    }).then(response => {
        if (response.ok) {
            alert("공지사항 등록 성공");
            window.location.href = "/manager/notice";
        } else {
            alert("공지사항 등록 실패");
        }
    }).catch(error => console.log(error));
});

document.addEventListener("DOMContentLoaded", function () {
    const dateElement = document.querySelector('.line p');
    const today = new Date();
    const year = today.getFullYear();
    const month = String(today.getMonth() + 1).padStart(2, '0');
    const day = String(today.getDate()).padStart(2, '0');
    const formattedDate = `${year}.${month}.${day}`;
    dateElement.textContent = formattedDate;
})