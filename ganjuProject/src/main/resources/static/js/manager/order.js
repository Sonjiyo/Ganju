/* 내용물 펼치지 */
function buttonOpen(){
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
}

function filterList(type, button) {
    // 모든 li 요소에서 "on" 클래스 제거
    let listItems = document.querySelectorAll('.list-select li');
    listItems.forEach(e=> {
        e.classList.remove('on');
    });

    // 클릭된 li 요소에 "on" 클래스 추가
    button.classList.add('on');

    // 모든 주문 리스트 아이템을 가져옴
    let orders = document.querySelectorAll('.order-list>li');

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
function deleteOrder(id){
    if(confirm("정말 삭제하시겠습니까?")) {
        let list = document.querySelectorAll(".order-list li:has(.wait)");

        fetch('/validRefund', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({
                orderId: id + "", // 서버 사이드에서 전달받은 주문 ID
            }),
        })
            .then(response => response.json())
            .then(data => {
                console.log(data.success);
                if (data.success) {
                    alert('거부 처리가 완료되었습니다.');
                } else {
                    alert('거부 처리에 실패했습니다.');
                }
            })
            .catch(error => {
                console.error('Error:', error);
                alert('서버와의 통신 중 문제가 발생했습니다.');
            });
    }
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


// order.js에서는 home.js에서 선언한 함수를 활용하여 메시지를 처리
if (window.handleReceivedCall) {
    // 기존의 handleReceivedCall 함수를 백업
    const originalHandleReceivedCall = window.handleReceivedCall;

    // handleReceivedCall 함수 확장
    window.handleReceivedCall = function(callInfo) {
        // 기존 로직 호출
        originalHandleReceivedCall(callInfo);

        // order.js에서 추가 로직 구현
        console.log("order.js:", callInfo);
        if(typeof callInfo !== 'object'){
            const element = [...document.querySelectorAll('input.id')];
            let idInput = null;
            element.forEach(e=>{
                if(e.value == callInfo){
                    idInput = e;
                }
            })
            console.log(idInput);
            idInput.closest('li').remove();
            let waitCount = document.querySelector('.order-state-summary li.wait .state-content span');
            waitCount.textContent = parseInt(waitCount.textContent)-1;
        }

        // 이전 주문 정보를 백업
        let innerData = document.querySelector('.order-list').innerHTML;

        // 주문 메뉴에 대한 HTML을 생성할 변수 초기화
        let orderMenusHTML = '';

        // 주문 총 수량을 계산할 변수 초기화
        let totalQuantity = 0;

        // 주문 옵션의 총 가격을 계산할 변수 초기화
        let optionPrice = 0;

        // 주문 정보가 "CALL"이 아닌 경우에만 처리
        if (callInfo.division !== "CALL") {
            // 주문 총 수량을 계산합니다.
            callInfo.orderMenus.forEach(e => {
                totalQuantity += e.quantity;
            })

            // 주문 메뉴를 처리
            callInfo.orderMenus.forEach(e => {
                // 주문 옵션의 총 가격을 계산
                e.orderOptions.forEach(en => {
                    optionPrice += en.price;
                })

                // 주문 메뉴의 총 가격을 계산
                let totalPrice = (e.price + optionPrice) * e.quantity;

                // 주문 옵션에 대한 HTML을 초기화
                let orderOptionsHTML = '';

                // 주문 옵션에 대한 HTML을 생성
                e.orderOptions.forEach(en => {
                    orderOptionsHTML += '<li>'+en.optionName + '('+en.price+')'+'원</li>\n';
                });

                // 주문 메뉴에 대한 HTML을 생성
                orderMenusHTML +=
                    '             <tr>\n' +
                    '                  <td>\n' +
                    '                       <p>' + e.menuName + '</p>\n' +
                    '                       <ol>\n' +
                    '                            <li>가격 : ' + e.price + '원</li>\n' +
                    orderOptionsHTML +
                    '                       </ol>\n' +
                    '                  </td>\n' +
                    '                  <td>' + e.quantity + '</td>\n' +
                    '                  <td>' + totalPrice + '원</td>\n' +
                    '             </tr>\n';
            });
        }

        // 펼처지는 버튼의 HTML을 생성
        let infoBtn = callInfo.division !== "CALL" ? '<button><i class="fas fa-chevron-down"></i></button>\n' : '';

        // 주문 정보 테이블의 HTML을 생성
        let infoTable = callInfo.division !== "CALL" ? '<table class="info">\n' +
            '              <tr>\n' +
            '                   <th>' +
            '상품이름' +
            '<input type="hidden" value="'+callInfo.id+'" class="id">'+
            '</th>\n' +
            '                   <th>수량</th>\n' +
            '                   <th>금액</th>\n' +
            '              </tr>\n' +
            orderMenusHTML +
            '             <tr class="last">\n' +
            '                  <td>총 합계</td>\n' +
            '                  <td>' + totalQuantity + '</td>\n' +
            '                  <td>' + callInfo.price + '원</td>\n' +
            '             </tr>\n' +
            '             <tr class="contents">\n' +
            '                  <td colspan="3">\n' +
            '                      <p class="title">요청 사항</p>\n' +
            '                      <p class="content">' + callInfo.content + '</p>\n' +
            '                  </td>\n' +
            '             </tr>\n' +
            '             <tr class="button-group">\n' +
            '                  <td colspan="3">\n' +
            '                       <button class="button delete" onclick="deleteOrder(' + callInfo.id + ', this)">거부</button>\n' +
            '                       <button class="button recognize" onclick="recognizeOrder(' + callInfo.id + ', this)">승인</button>\n' +
            '                  </td>\n' +
            '             </tr>'+
            '     </table>\n' : '';

        // 주문 정보의 타입에 따라 다른 스타일의 HTML을 생성
        let division = callInfo.division === "CALL" ? '<p class="order-title call">호출</p>' : '<p class="order-title wait">주문</p>';
        let waitContent = callInfo.division === "CALL" ? '<p class="order-subtitle">'+callInfo.content+'</p>\n' : '';

        // 주문 정보의 왼쪽 영역에 대한 HTML을 생성
        let leftInfo =
            ' <div class="left">\n' + division + waitContent +
            '<span class="table-name">' + callInfo.restaurantTableNo + '번 테이블</span>\n' +
            ' </div>'

        // 주문 정보의 시간에 대한 HTML을 생성
        let rightInfo = '<div class="right">\n';

        // 주문 정보의 등록일에 따라 다른 스타일의 시간을 생성
        if (callInfo.regDate.substring(0, 10) === new Date().toISOString().substring(0, 10)) {
            rightInfo += '    <span class="time">' + callInfo.regDate.substring(11, 16) + '</span>\n';
        } else {
            let formattedDate = new Date(callInfo.regDate).toLocaleString('ko-KR', {
                year: 'numeric',
                month: '2-digit',
                day: '2-digit',
            }).replace(/\./g, '-').replaceAll(' ', ''); // 점(.)을 하이픈(-)으로 변경
            formattedDate = formattedDate.substring(0, 10);
            formattedDate += ' / ' + callInfo.regDate.substring(11, 16); // 마지막 하이픈과 '오전' 삭제
            rightInfo += '    <span class="time">' + formattedDate + '</span>\n';
        }

        rightInfo += infoBtn+'</div>';

        // 주문 정보를 화면에 추가
        document.querySelector('.order-list').innerHTML =
            '<li>\n' +
            '    <div class="content-summary">\n' + leftInfo + rightInfo +
            '    </div>\n' + infoTable + '</li>\n' + innerData;

        let waitCount = document.querySelector('.order-state-summary li.wait .state-content span');
        let callCount = document.querySelector('.order-state-summary li.call .state-content span');
        if(callInfo.division === "CALL"){
            callCount.textContent = parseInt(callCount.textContent) +1;
        }else{
            waitCount.textContent = parseInt(waitCount.textContent)+1;
        }

        buttonOpen();
    }
}

document.addEventListener('DOMContentLoaded', () => {
    buttonOpen();
});