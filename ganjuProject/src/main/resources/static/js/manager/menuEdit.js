
function deleteImage(id){
    fetch(`/menu/image/${id}`, {
        method: 'DELETE',
    }).then(response=>{
        return response.text();
    }).then(data => {
        if(data === 'ok'){
            document.querySelector('.flex img').src='https://ganju-test.s3.ap-northeast-2.amazonaws.com/noImage.png';
            document.querySelector('#deleteImageButton').style.display='none';
        }else{
            console.log('삭제 실패');
        }
    }).catch(error => {
        console.error('확인 실패', error);
    });
}

document.addEventListener('DOMContentLoaded', function() {
    // 옵션 삭제 버튼에 대한 클릭 이벤트 리스너 등록
    document.querySelectorAll('.remove-option').forEach(button => {
        button.addEventListener('click', function() {
            const optionIndex = this.getAttribute('data-option-index');
            const optionDiv = document.getElementById('option-' + optionIndex);
            // 옵션 요소를 페이지에서 제거
            optionDiv.remove();
        });
    });
});

let msg = document.createElement('p');
msg.classList.add('msg');
let msgOk = document.createElement('p');
msgOk.classList.add('msg');
msgOk.classList.add('msg-ok');

let menuBtnCheck = false;

function menuUpdate(form) {
    msg.textContent = '';
    if (menuBtnCheck) return false;
    // 메뉴 카테고리 체크
    if (!form.category.value.trim()) {
        msg.textContent = "메뉴 카테고리를 선택해주세요";
        document.querySelector('.option').appendChild(msg);
        return false;
    }

    // 메뉴 이름 체크
    if (!form.menuName.value.trim()) {
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
    if (!form.menuInfo.value.trim()) {
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