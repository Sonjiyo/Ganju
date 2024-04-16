// 리뷰 작성
function reviewCheck(form){
    // orderId, nickname, contents, star가 넘어감
    form.submit();
}

// 별 클릭시 색 변하는 이벤트
const stars = document.querySelectorAll('.star-span span');
const text = document.querySelector('.star-span .star');

stars.forEach( (e, idx) =>{
    e.addEventListener('click', () =>{
        updateStars(idx);
    })
});

// 별을 i 번째 만큼 색 바꿈
function updateStars(idx){
    stars.forEach( (e, i) =>{
        text.value = idx+1;
        if(i <= idx){
            e.style.color = '#FFBE3F';
        }else{
            e.style.color = '#c2c2c2';
        }
    });
}