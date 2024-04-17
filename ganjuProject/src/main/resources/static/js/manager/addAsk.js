let msg = document.createElement('p');
msg.classList.add('msg');

function askCheck(form) {
    msg.textContent = '';

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

    form.submit();
}

function deleteAsk(id, btn){
    fetch(`/board/${id}`, {
        method: 'DELETE',
    }).then(response=>{
        return response.text();
    }).then(data => {
        if(data === 'ok'){
            console.log('삭제 성공');
            let deleteBtns = document.querySelectorAll('.button.delete');
            let btnIndex = deleteBtns.indexOf(btn);
            let askList = document.querySelectorAll('.ask-list li');
            askList[btnIndex].remove();
        }else{
            console.log('삭제 실패');
        }
    }).catch(error => {
        console.error('확인 실패', error);
    });
}