let msg = document.createElement('p');
msg.classList.add('msg');
let check = false;
function askCheck(form) {
    msg.textContent = '';

    if(check) return false;
    //제목 체크
    if (!form.title.value.trim()) {
        msg.textContent = '제목을 입력해주세요';
        document.querySelector('.title').appendChild(msg);
        return false;
    }

    //내용 체크
    if (!form.content.value.trim()) {
        msg.textContent = '내용을 입력해주세요';
        document.querySelector('.content').appendChild(msg);
        return false;
    }

    check = true;
    form.submit();
}