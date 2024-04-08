let msg = document.createElement('p');
msg.classList.add('msg');
let msgOk = document.createElement('p');
msgOk.classList.add('msg');
msgOk.classList.add('msg-ok');

//상점 등록------------------------------------------------------
function resturantCheck(form){
    msg.textContent='';
    //이미지 체크
    if(!form.logo.value.trim()){
        msg.textContent= "로고 이미지를 넣어주세요";
		document.querySelector('.logo-upload').appendChild(msg);
        return false;
    }

    //식당 이름 체크
    if(!form.name.value.trim()){
        msg.textContent= "식당 이름을 입력해주세요";
		document.querySelector('.resturant-name').appendChild(msg);
        return false;
    }
    if(form.name.value.trim().match(/[\{\}\[\]\/;:|*~`^\-_+<>@\#$%&\\\=\'\"]/)){
        msg.textContent=',.!?() 외의 특수문자는 불가능합니다';
        document.querySelector('.resturant-name').appendChild(msg);
        return false;
    }

    //전화번호 체크
    if(!form.phone.value.trim()){
        msg.textContent='식당 전화번호를 입력해주세요';
        document.querySelector('.resturant-phone').appendChild(msg);
        return false;
    }
    if (!form.phone.value.match(/^[\d]{2,3}-[\d]{3,4}-[\d]{4}$/)) {
		msg.textContent= "전화번호 형식에 맞게 입력해주세요 000-0000-0000";
		document.querySelector('.resturant-phone').appendChild(msg);
		return false;
	}
    //식당주소 체크
    if(!form.addressFirst.value.trim()){
        msg.textContent='식당 주소를 입력해주세요';
        document.querySelector('.resturant-address').appendChild(msg);
        return false;
    }

    //테이블 수 체크
    if(!form.restaurantTable.value.trim()){
        msg.textContent='테이블 수를 입력해주세요';
        document.querySelector('.restaurant-table').appendChild(msg);
        return false;
    }
    if(form.restaurantTable.value<=0){
        msg.textContent='테이블 수는 1이상으로 입력해주세요.';
        document.querySelector('.restaurant-table').appendChild(msg);
        return false;
    }

    
}

//이미지 미리보기
function readURL(input) {
	if (input.files && input.files[0]) {
		let reader = new FileReader();
		reader.onload = function(e) {
			document.querySelector('#logoImage').src = e.target.result;
		};
		reader.readAsDataURL(input.files[0]);
	} else {
		document.querySelector('#logoImage').src = '';
	}
}