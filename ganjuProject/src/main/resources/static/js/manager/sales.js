let dateSelect = document.querySelectorAll('.date-select li');
let startValue = document.querySelector('#start-date span');
let endValue = document.querySelector('#end-date span');

/* 시작 날짜 지정 */
startValue.addEventListener('click', ()=>{
    removeOn();
    $('#start-date').daterangepicker({
        startDate: start.format('YYYY-MM-DD')+"",
        singleDatePicker: true,
        showCustomRangeLabelranges:false,
        opens: "left",
        maxDate: maxDate,
        locale: {
            "format": "YYYY-MM-DD",
            "separator": " ~ ",
            "applyLabel": "적용",
            "cancelLabel": "취소",
            "fromLabel": "에서",
            "toLabel": "까지",
            "customRangeLabel": "날짜 선택",
            "weekLabel": "주",
            "daysOfWeek": [
                "일",
                "월",
                "화",
                "수",
                "목",
                "금",
                "토"
            ],
            "monthNames": [
                "1월",
                "2월",
                "3월",
                "4월",
                "5월",
                "6월",
                "7월",
                "8월",
                "9월",
                "10월",
                "11월",
                "12월"
            ],
            "firstDay": 1
        },    
    });
    $('#start-date').on('apply.daterangepicker', function(ev, picker) {
        start = picker.startDate;
        startSpan(start);
        changeOrderData(start, end);
    })
});

/*끝나는 날짜 지정*/
endValue.addEventListener('click', ()=>{
    removeOn();
    $('#end-date').daterangepicker({
        startDate: end.format('YYYY-MM-DD')+"",
        singleDatePicker: true,
        showCustomRangeLabelranges:false,
        opens: "left",
        maxDate: maxDate,
        locale: {
            "format": "YYYY-MM-DD",
            "separator": " ~ ",
            "applyLabel": "적용",
            "cancelLabel": "취소",
            "fromLabel": "에서",
            "toLabel": "까지",
            "customRangeLabel": "날짜 선택",
            "weekLabel": "주",
            "daysOfWeek": [
                "일",
                "월",
                "화",
                "수",
                "목",
                "금",
                "토"
            ],
            "monthNames": [
                "1월",
                "2월",
                "3월",
                "4월",
                "5월",
                "6월",
                "7월",
                "8월",
                "9월",
                "10월",
                "11월",
                "12월"
            ],
            "firstDay": 1
        },
    });
    $('#end-date').on('apply.daterangepicker', function(ev, picker) {
        end = picker.startDate;
        if(start>end){
            alert('시작날짜보다 끝날짜가 앞섭니다.');
            end = start;
        }
        endSpan(end);
        changeOrderData(start, end);
    })
});

let start = moment().subtract(0, 'days');
let end = start;

startSpan(start);
endSpan(end);

/* on을 지우고 넣음 */
dateSelect.forEach(e=>{
    e.addEventListener('click', ()=>{
        removeOn();
        e.classList.add('on');
    })
})
/* 전체 on 지우기*/
function removeOn(){
    dateSelect.forEach(e=>{
        e.classList.remove('on');
    })
}

/* 오늘 날짜로 선택 */
dateSelect[0].addEventListener('click', ()=>{
    start = moment();
    end = start;
    startSpan(start);
    endSpan(end);
    changeOrderData(start, end);
})

/* 일주일 날짜 선택 */
dateSelect[1].addEventListener('click', ()=>{
    start = moment().subtract(6, 'days');
    end = moment();
    startSpan(start);
    endSpan(end);
    changeOrderData(start, end);
})

/* 한달 날짜 선택 */
dateSelect[2].addEventListener('click', ()=>{
    start = moment().subtract(29, 'days');
    end = moment();
    startSpan(start);
    endSpan(end);
    changeOrderData(start, end);
})

function startSpan(start) {
    $('#start-date span').html(start.format('YYYY-MM-DD'));
}
function endSpan(end) {
    if(start>end){
        alert('시작날짜보다 끝날짜가 앞섭니다.');
        end = start;
    }
    $('#end-date span').html(end.format('YYYY-MM-DD'));
}

let maxDate = moment().format('YYYY-MM-DD')+"";

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


function changeOrderData(startDate, endDate){
    fetch('/sales', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            "start" : startDate.format('YYYY-MM-DDTHH:mm:ss.SSS'),
            "end" : endDate.format('YYYY-MM-DDTHH:mm:ss.SSS')
        })
    })
        .then(response => {
            if (!response.ok) {
                throw new Error('Failed to send date range');
            }
            return response.json();
        })
        .then(data => {
            document.querySelector('.total-count span').textContent = data.totalCount +"건";
            document.querySelector('.total-price span').textContent = parseInt(data.totalPrice).toLocaleString() +"원";
            document.querySelector('.sales-list').innerHTML='';
            console.log(data.list);
            data.list.forEach(e => {
                let totalQuantity = 0;
                e.orderMenus.forEach(en=>{
                    totalQuantity += en.quantity;
                })
                let orderMenusHTML = '';
                e.orderMenus.forEach(en => {
                    let optionPrice = 0;
                    en.orderOptions.forEach(ent=>{
                        optionPrice += ent.price;
                    })
                    let totalPrice = (en.price+optionPrice)*en.quantity;
                    let orderOptionsHTML = '';
                    en.orderOptions.forEach(ent => {
                        orderOptionsHTML += '<li>'+ent.optionName + '('+ent.price+')'+'원</li>\n';
                    });

                    orderMenusHTML +=
                        '             <tr>\n' +
                        '                  <td>\n' +
                        '                       <p>' + en.menuName + '</p>\n' +
                        '                       <ol>\n' +
                        '                            <li>가격 : ' + en.price + '원</li>\n' +
                        orderOptionsHTML +
                        '                       </ol>\n' +
                        '                  </td>\n' +
                        '                  <td>' + en.quantity + '</td>\n' +
                        '                  <td>' + totalPrice + '</td>\n' +
                        '             </tr>\n';
                });

                document.querySelector('.sales-list').innerHTML +=
                    '<li>\n' +
                    '    <div class="content-summary">\n' +
                    '         <span class="table-name">' + e.restaurantTableNo + '번 테이블</span>\n' +
                    '               <div class="right">\n' +

                    '<span class="time">' + new Date(e.regDate).toLocaleString('ko-KR', { year: 'numeric', month: '2-digit', day: '2-digit' }).replace(/\./g, '-').replaceAll(' ', '').substring(0, 10) + ' / ' + new Date(e.regDate).toLocaleTimeString('ko-KR', { hour12: false, hour: '2-digit', minute: '2-digit' }) + '</span>\n'+
                    '                     <button><i class="fas fa-chevron-down"></i></button>\n' +
                    '               </div>\n' +
                    '    </div>\n' +
                    '         <table class="info">\n' +
                    '              <tr>\n' +
                    '                   <th>상품이름</th>\n' +
                    '                   <th>수량</th>\n' +
                    '                   <th>금액</th>\n' +
                    '              </tr>\n' +
                    orderMenusHTML +
                    '             <tr class="last">\n' +
                    '                  <td>총 합계</td>\n' +
                    '                  <td>' + totalQuantity + '</td>\n' +
                    '                  <td>' + e.price + '원</td>\n' +
                    '             </tr>\n' +
                    '     </table>\n' +
                    '</li>';
            });
            buttonOpen();
        })
        .catch(error => {
            console.error('실패', error);
        });
}
document.addEventListener('DOMContentLoaded', () => {
    buttonOpen();
});