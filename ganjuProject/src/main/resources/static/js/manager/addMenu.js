// 옵션 추가 버튼 클릭 시 모달 창 열기
document.querySelector('.add-option').addEventListener('click', function () {
    document.querySelector('.modal').style.display = 'block';
});

// 모달 창 닫기
document.querySelector('.close').addEventListener('click', function () {
    document.querySelector('.modal').style.display = 'none';
});

// 버튼 클릭 이벤트 핸들러 함수
// document.getElementById("addMenuButton").addEventListener("click", async function () {
//     // 새로운 메뉴 데이터 생성 (이 부분은 실제로 사용자로부터 데이터를 수집하는 방법에 따라 달라질 수 있습니다.)
//     const menuName = document.querySelector('.menu-name textarea').value.trim();
//     const menuPrice = document.querySelector('.price textarea').value.trim();
//     const menuInfo = document.querySelector('.menu-info textarea').value.trim();
//     const menuImage = document.querySelector('.imgbox input[type="file"]').files[0].value;
//     const categorySelect = document.getElementById("category");
//     const selectedCategoryID = categorySelect.value;
//     const restaurantId = 1;
//     if (!menuName || !menuPrice || !selectedCategoryID || !menuInfo || !menuImage) {
//         alert("카테고리, 메뉴 이름, 가격, 정보를 입력하거나 메뉴 이미지 등록 해주세요");
//         return;
//     }
//     const menuData = {
//         name: menuName,
//         price: menuPrice,
//         info: menuInfo,
//         image: menuImage,
//         categoryId: selectedCategoryID,
//         restaurantId: restaurantId,
//     };
//     fetch("/menu/add?restaurantId=" + restaurantId, {
//         method: "POST",
//         headers: {
//             "Content-Type": "application/json"
//         },
//         body: JSON.stringify(menuData)
//     }).then(response => {
//         if (response.ok) {
//             alert("메뉴 등록 성공");
//             window.location.href = "/category/main";
//         } else {
//             // 실패했을 때의 처리
//             alert("메뉴 등록에 실패");
//         }
//     }).catch(error => console.log(error));
// });

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
    if (!form.category.value.trim()) {
        msg.textContent = "메뉴 카테고리를 선택해주세요";
        document.querySelector('.option').appendChild(msg);
        return false;
    }

    // 메뉴 이미지 체크
    if (!form.img.value.trim()) {
        msg.textContent = "메뉴 이미지를 넣어주세요";
        document.querySelector('.flex').appendChild(msg);
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
