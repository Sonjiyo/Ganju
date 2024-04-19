let msg = document.createElement('p');
msg.classList.add('msg');
let msgOk = document.createElement('p');
msgOk.classList.add('msg');
msgOk.classList.add('msg-ok');

let restaurantBtnCheck = false;

function restaurantInfo(form) {
    msg.textContent = '';

    if (restaurantBtnCheck) return false;
    //이미지 체크
    if (!form.logo.value.trim()) {
        msg.textContent = "로고 이미지를 넣어주세요";
        document.querySelector('.res-logo').appendChild(msg);
        return false;
    }

    //식당 이름 체크
    if (!form.name.value.trim()) {
        msg.textContent = "식당 이름을 입력해주세요";
        document.querySelector('.res-name').appendChild(msg);
        return false;
    }
    if (form.name.value.trim().match(/[\{\}\[\]\/;:|*~`^\-_+<>@\#$%&\\\=\'\"]/)) {
        msg.textContent = ',.!?() 외의 특수문자는 불가능합니다';
        document.querySelector('.res-name').appendChild(msg);
        return false;
    }

    //전화번호 체크
    if (!form.phone.value.trim()) {
        msg.textContent = '식당 전화번호를 입력해주세요';
        document.querySelector('.res-tel').appendChild(msg);
        return false;
    }
    if (!form.phone.value.match(/^[\d]{2,3}-[\d]{3,4}-[\d]{4}$/)) {
        msg.textContent = "전화번호 형식에 맞게 입력해주세요 000-0000-0000";
        document.querySelector('.res-tel').appendChild(msg);
        return false;
    }
    //식당주소 체크
    if (!form.addressFirst.value.trim()) {
        msg.textContent = '식당 주소를 입력해주세요';
        document.querySelector('.res-address').appendChild(msg);
        return false;
    }

    resturantBtnCheck = true;
    form.submit();
}

//이미지 미리보기
function readURL(input) {
    if (input.files && input.files[0]) {
        let reader = new FileReader();
        reader.onload = function (e) {
            document.querySelector('#logoImg').src = e.target.result;
            document.querySelector('.logo-img').style.background = 'inherit';
        };
        reader.readAsDataURL(input.files[0]);
    } else {
        document.querySelector('#logoImg').src = '';
        document.querySelector('.logo-img').style.background = '#c2c2c2';
    }
}