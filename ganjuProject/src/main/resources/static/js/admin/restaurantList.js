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

// 패스워드 숨기기 / 보이기

let passwordEye = [...document.querySelectorAll('.passwordEye')];
let password = [...document.querySelectorAll('.password')];

passwordEye.forEach(e=>{
    e.addEventListener('click', ()=>{
        if(e.classList.contains('fa-eye')){
            e.classList.add('fa-eye-slash');
            e.classList.remove('fa-eye');
            password[passwordEye.indexOf(e)].type = 'password';
        }else{
            e.classList.remove('fa-eye-slash');
            e.classList.add('fa-eye');
            password[passwordEye.indexOf(e)].type='text';
        } 
    })
})

function recognizeRestaurant(keyId, btn){
    fetch(`/restaurant/recognize/${keyId}`, {
        method: 'PUT',
    }).then(response=>{
        return response.text();
    }).then(data => {
        if(data === 'ok'){
            console.log('승인 성공');
            let btns = [...document.querySelectorAll('.recognize')];
            let btnIndex = btns.indexOf(btn);
            let badge = document.querySelectorAll(".badge.waiting");
            badge[btnIndex].classList.remove("waiting");
            badge[btnIndex].classList.add("success");
            badge[btnIndex].textContent = "승인완료";
            btn.remove();
        }else{
            console.log('승인 실패');
        }
    }).catch(error => {
        console.error('확인 실패', error);
    });
}

function deleteUser(keyId, btn){
    if(confirm("정말 삭제하시겠습니까?")){
        fetch(`/manager/${keyId}`, {
            method: 'DELETE',
        }).then(response=>{
            return response.text();
        }).then(data => {
            if(data === 'ok'){
                console.log('삭제 성공');
                let btns = [...document.querySelectorAll('.refuse')];
                let btnIndex = btns.indexOf(btn);
                let restaurantList = document.querySelectorAll(".resturant-list li");
                restaurantList[btnIndex].remove();
            }else{
                console.log('삭제 실패');
            }
        }).catch(error => {
            console.error('확인 실패', error);
        });
    }
}