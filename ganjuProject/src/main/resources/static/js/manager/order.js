let buttons = [...document.querySelectorAll('.content-summary .right button')];
let content = [...document.querySelectorAll('.info')];
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
function filterList(type, button) {
    // 모든 li 요소에서 "on" 클래스 제거
    let listItems = document.querySelectorAll('.list-select li');
    listItems.forEach(e=> {
        e.classList.remove('on');
    });

    // 클릭된 li 요소에 "on" 클래스 추가
    button.classList.add('on');

    // 모든 주문 리스트 아이템을 가져옴
    let orders = document.querySelectorAll('.order-list li');

    // 선택한 타입에 따라 필터링
    orders.forEach(order=> {
        let orderType = order.querySelector('.order-title').classList[1]; // 클래스명에서 타입 가져오기

        // 전체는 모든 아이템을 보여줌
        if (type === 'all') {
            order.style.display = 'block';
        } else {
            // 선택한 타입과 클래스명이 일치하면 보여주고, 아니면 숨김
            if (orderType === type) {
                order.style.display = 'block';
            } else {
                order.style.display = 'none';
            }
        }
    });
}

//주문 승인 및 거부(삭제)
function deleteOrder(id, btn){
    let list = document.querySelectorAll(".order-list li:has(.wait)");

    fetch(`/order/${id}`, {
        method: 'DELETE',
    }).then(response=>{
        return response.text();
    }).then(data => {
        if(data === 'ok'){
            console.log('삭제 성공');
            let btns = [...document.querySelectorAll('.delete')];
            let btnIndex = btns.indexOf(btn);
            list[btnIndex].remove();
            let waitCount = document.querySelector('.order-state-summary li.wait .state-content span');
            waitCount.textContent = parseInt(waitCount.textContent)-1;
        }else{
            console.log('삭제 실패');
        }
    }).catch(error => {
        console.error('확인 실패', error);
    });

}

function recognizeOrder(id, btn){
    fetch(`/order/${id}`, {
        method: 'PUT',
    }).then(response=>{
        return response.text();
    }).then(data => {
        if(data === 'ok'){
            console.log('승인 성공');
            let btns = [...document.querySelectorAll('.recognize')];
            let btnIndex = btns.indexOf(btn);
            let buttonGroup = document.querySelectorAll(".button-group");
            buttonGroup[btnIndex].remove();
            let titleGroup = [...document.querySelectorAll('.order-title.wait')];
            titleGroup[btnIndex].classList.add('okay');
            titleGroup[btnIndex].classList.remove('wait');
            titleGroup[btnIndex].textContent='승인';

            let waitCount = document.querySelector('.order-state-summary li.wait .state-content span');
            waitCount.textContent = (parseInt(waitCount.textContent)-1)+"";
            let okayCount = document.querySelector('.order-state-summary li.okay .state-content span');
            okayCount.textContent = (parseInt(waitCount.textContent)+1)+"";

        }else{
            console.log('승인 실패');
        }
    }).catch(error => {
        console.error('확인 실패', error);
    });

}
