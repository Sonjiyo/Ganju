const boardUpdate = (id) => {
    const newTitle = document.getElementById("title").value;
    const newContents = document.getElementById("contents").value;
    if (!newTitle || !newContents) {
        alert("제목이나 내용을 입력해주세요");
        return;
    }
    fetch(`/manager/updateNotice/${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({title: newTitle, contents: newContents}),
    }).then(response => {
        if (response.ok) {
            window.location.href = '/manager/notice';
        } else {
            console.log("실패");
        }
    }).catch(error => console.error('Error : ', error));
}