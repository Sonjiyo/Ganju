// 옵션 추가 버튼 클릭 시 모달 창 열기
document.querySelector('.add-option').addEventListener('click', function () {
    document.querySelector('.modal').style.display = 'block';
});

// 모달 창 닫기
document.querySelector('.close').addEventListener('click', function () {
    document.querySelector('.modal').style.display = 'none';
});

function getCategoryID() {
    const categorySelect = document.getElementById("category"); // 카테고리 선택 요소 가져오기
    const selectedCategoryID = categorySelect.value; // 선택한 카테고리의 값(여기서는 ID) 가져오기
    return selectedCategoryID;
}

// 버튼 클릭 이벤트 핸들러 함수
document.getElementById("addMenuButton").addEventListener("click", async function () {
    // 새로운 메뉴 데이터 생성 (이 부분은 실제로 사용자로부터 데이터를 수집하는 방법에 따라 달라질 수 있습니다.)
    const menuName = document.querySelector('.menu-name textarea').value.trim();
    const menuPrice = document.querySelector('.price textarea').value.trim();
    const restaurantId = 1;
    const selectedCategoryID = getCategoryID();
    if (!menuName || !menuPrice || !selectedCategoryID) {
        alert("카테고리 또는 메뉴 이름 및 가격을 입력해주세요");
        return;
    }
    const menuData = {
        name: menuName,
        price: menuPrice,
        categoryId: selectedCategoryID,
        restaurantId: restaurantId,
    };
    fetch("/menu/add?restaurantId=" + restaurantId, {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(menuData)
    }).then(response => {
        if (response.ok) {
            alert("메뉴가 성공적으로 등록되었습니다");
            window.location.href = "/category/main";
        } else {
            // 실패했을 때의 처리
            alert("메뉴 등록에 실패하였습니다");
        }
    }).catch(error => console.log(error));
});