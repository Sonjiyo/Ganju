document.addEventListener('DOMContentLoaded', () => {
    buttonOpen();
});

function buttonOpen(){
    let buttons = [...document.querySelectorAll('.title-right button')];
    let content = [...document.querySelectorAll('.content')];
    buttons.forEach(e=>{
        e.addEventListener('click', ()=>{
            if(e.classList.contains('on')){
                e.classList.remove('on');
                content[buttons.indexOf(e)].classList.remove('on');
            }else{
                e.classList.add('on');
                content[buttons.indexOf(e)].classList.add('on');
            }
        })
    })
}

function deleteAsk(id, btn){
    fetch(`/board/${id}`, {
        method: 'DELETE',
    }).then(response=>{
        return response.text();
    }).then(data => {
        if(data === 'ok'){
            console.log('삭제 성공');
            btn.closest('li').remove();
        }else{
            console.log('삭제 실패');
        }
    }).catch(error => {
        console.error('확인 실패', error);
    });
}