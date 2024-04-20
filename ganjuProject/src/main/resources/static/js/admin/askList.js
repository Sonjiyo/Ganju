document.addEventListener('DOMContentLoaded', () => {
    buttonOpen();
});
let answerCheck = false;

function buttonOpen(){
    let buttons = [...document.querySelectorAll('.title-right button')];
    let content = [...document.querySelectorAll('.content')];
    buttons.forEach(e=>{
        e.addEventListener('click', ()=>{
            answerCheck = false;
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
function buttonShow(btn){
    let showBtns = [...document.querySelectorAll('.button.show')];
    let btnIndex = showBtns.indexOf(btn);
    document.querySelectorAll('.button.answer')[btnIndex].style.display='block';
    document.querySelectorAll('.last.answer')[btnIndex].style.display='flex';
    btn.style.display='none';
}


function answerBtn(id, btn){
    if(answerCheck) return false;
    let answerBtns = [...document.querySelectorAll('.button.answer')];
    let btnIndex = answerBtns.indexOf(btn);
    let answerText = document.querySelectorAll('.last.answer')[btnIndex].querySelector('textarea').value;
    let td = btn.closest('td');
    fetch(`/board/ask/${id}/${answerText}`, {
        method: 'PUT'
    }).then(response=>{
        return response.text();
    }).then(data => {
        if(data === 'ok'){
            console.log('답변 성공');

            answerCheck=true;
            let lastTr = document.querySelectorAll('.last td:last-child');
            lastTr[btnIndex].innerHTML=answerText;
            document.querySelectorAll('.last')[btnIndex].classList.remove('answer');
            document.querySelectorAll('.button.show')[btnIndex].remove();
            document.querySelectorAll('.button.answer')[btnIndex].remove();
            let badge = document.querySelectorAll('.badge.waiting')[btnIndex];
            badge.classList.add('success');
            badge.textContent = '확인됨';
            badge.classList.remove('waiting');
            td.innerHTML += '' +
                '<button class="button answer-delete" onclick="deleteAnswer('+id+',this)">답변 삭제하기</button>';

        }else{
            console.log('답변 실패');
        }
    }).catch(error => {
        console.error('확인 실패', error);
    });

}

function deleteAsk(id, btn){
    if(confirm("정말 삭제하시겠습니까?")) {
        fetch(`/board/${id}`, {
            method: 'DELETE',
        }).then(response => {
            return response.text();
        }).then(data => {
            if (data === 'ok') {
                console.log('삭제 성공');
                let deleteBtns = [...document.querySelectorAll('.button.delete')];
                let btnIndex = deleteBtns.indexOf(btn);
                let askList = document.querySelectorAll('.ask-list li');
                askList[btnIndex].remove();
            } else {
                console.log('삭제 실패');
            }
        }).catch(error => {
            console.error('확인 실패', error);
        });
    }
}

function deleteAnswer(id, btn) {
    answerCheck = false;
    let answerText=null;
    fetch(`/board/ask/${id}/${answerText}`, {
        method: 'PUT'
    }).then(response=>{
        return response.text();
    }).then(data => {
        if(data === 'ok'){
            console.log('답변 삭제 성공');
            let answerDeleteBtns = [...document.querySelectorAll('.button.answer-delete')];
            let btnIndex = answerDeleteBtns.indexOf(btn);
            let innerData = btn.closest('td').innerHTML;
            let table = btn.closest('table');
            btn.closest('td').innerHTML = '' +
                '<button class="button show" onclick="buttonShow(this)">답변하기</button>\n' +
                '<button class="button answer" onclick="answerBtn(' + id + ', this)">등록하기</button>' +
                '<button class="button delete" onclick="deleteAsk(' + id + ', this)">삭제하기</button>';
            table.querySelector('.last').querySelector('td:last-child').innerHTML =
                '<label>\n' +
                '<textarea></textarea>\n' +
                '</label>';
            table.querySelector('.last').classList.add('answer');
            table.querySelector('.last').style.display='none';
            let badge = document.querySelectorAll('.badge.success')[btnIndex];
            badge.textContent = '확인중';
            badge.classList.add('waiting');
            badge.classList.remove('success');
        }else{
            console.log('답변 삭제 실패');
        }
    }).catch(error => {
        console.error('확인 실패', error);
    });
}