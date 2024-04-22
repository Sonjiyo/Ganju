let msg = document.createElement('p');
msg.classList.add('msg');
let msgOk = document.createElement('p');
msgOk.classList.add('msg');
msgOk.classList.add('msg-ok');

let menuBtnCheck = false;

function menuCheck(form) {
    msg.textContent = '';
    if (menuBtnCheck) return false;
    // 메뉴 카테고리 체크
    if (!form.categoryId.value.trim()) {
        msg.textContent = "메뉴 카테고리를 선택해주세요";
        document.querySelector('.option').appendChild(msg);
        return false;
    }
    //
    // // 메뉴 이미지 체크
    // if (!form.img.value.trim()) {
    //     msg.textContent = "메뉴 이미지를 넣어주세요";
    //     document.querySelector('.flex').appendChild(msg);
    //     return false;
    // }
    // 메뉴 이름 체크
    if (!form.name.value.trim()) {
        msg.textContent = "메뉴 이름을 입력해주세요";
        document.querySelector('.menu-name').appendChild(msg);
        return false;
    }
    // 메뉴 가격 체크
    if (!form.price.value.trim()) {
        msg.textContent = "메뉴 가격을 입력해주세요";
        document.querySelector('.price').appendChild(msg);
        return false;
    }
    // 메뉴 정보 체크
    if (!form.info.value.trim()) {
        msg.textContent = "메뉴 정보를 입력해주세요";
        document.querySelector('.menu-info').appendChild(msg);
        return false;
    }
    menuBtnCheck = true;
    form.submit();
}

function readURL(input) {
    if (input.files && input.files[0]) {
        let reader = new FileReader();
        reader.onload = function (e) {
            document.querySelector('#menuImage').src = e.target.result;
            document.querySelector('.flex').style.background = 'inherit';
        };
        reader.readAsDataURL(input.files[0]);
    } else {
        document.querySelector('#menuImage').src = '';
        document.querySelector('.flex').style.background = '#c2c2c2';
    }
}
