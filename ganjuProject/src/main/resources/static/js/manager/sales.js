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
    })
});

let start = moment().subtract(0, 'days');
let end = start;

startSpan(start);
endSpan(end);

/* on을 지우고 넣음 */
dateSelect.forEach(e=>{
    e.addEventListener('click', ()=>{
        if(e.classList.contains('on')){
            e.classList.remove('on');
        }else{
            removeOn();
            e.classList.add('on');
        }
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
})

/* 일주일 날짜 선택 */
dateSelect[1].addEventListener('click', ()=>{
    start = moment().subtract(6, 'days');
    end = moment();
    startSpan(start);
    endSpan(end);
})

/* 한달 날짜 선택 */
dateSelect[2].addEventListener('click', ()=>{
    start = moment().subtract(29, 'days');
    end = moment();
    startSpan(start);
    endSpan(end);
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
