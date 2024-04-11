function acceptReport(keyId, btn){
    fetch(`/board/report/${keyId}`, {
        method: 'PUT',
    }).then(response=>{
        return response.text();
    }).then(data => {
        if(data === 'ok'){
            console.log('승인 성공');
            let btns = [...document.querySelectorAll('.accept')];
            let btnIndex = btns.indexOf(btn);
            let reportList = document.querySelectorAll(".report-list li");
            reportList[btnIndex].remove();
        }else{
            console.log('승인 실패');
        }
    }).catch(error => {
        console.error('확인 실패', error);
    });
}

function rejectReport(keyId, btn){
    fetch(`/board/${keyId}`, {
        method: 'DELETE',
    }).then(response=>{
        return response.text();
    }).then(data => {
        if(data === 'ok'){
            console.log('삭제 성공');
            let btns = [...document.querySelectorAll('.reject')];
            let btnIndex = btns.indexOf(btn);
            let reportList = document.querySelectorAll(".report-list li");
            reportList[btnIndex].remove();
        }else{
            console.log('삭제 실패');
        }
    }).catch(error => {
        console.error('확인 실패', error);
    });
}