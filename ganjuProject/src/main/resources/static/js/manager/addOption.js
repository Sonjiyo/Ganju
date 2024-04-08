// 옵션 추가 버튼 클릭 시 모달 창 열기
document.querySelector('.add-option').addEventListener('click', function () {
    document.querySelector('.modal').style.display = 'block';
});

// 모달 창 닫기
document.querySelector('.close').addEventListener('click', function () {
    document.querySelector('.modal').style.display = 'none';
});

// 버튼 클릭 이벤트 핸들러 함수
document.getElementById("addMenuButton").addEventListener("click", async function() {
    // 새로운 메뉴 데이터 생성 (이 부분은 실제로 사용자로부터 데이터를 수집하는 방법에 따라 달라질 수 있습니다.)
    const menuOption = document.querySelector('.option select').value;
    const menuName = document.querySelector('.menu-name textarea').value.trim();
    const menuPrice = document.querySelector('.price textarea').value.trim();
    // const menuOptional = document.querySelector('.option-l select').value;
    if(!menuName || !menuPrice) {
        alert("메뉴 이름과 가격을 입력해주세요");
        return;
    }
    const menuData = {
        //option: menuOption,
        name: menuName,
        price: menuPrice,
        // optional: menuOptional,
    };

   fetch("/menu/add", {
       method: "POST",
       headers: {
           "Content-Type": "application/json"
       },
       body: JSON.stringify(menuData)
   }).then(data =>{
       if(data.ok){
           alert("메뉴가 성공적으로 등록되었습니다");
           window.location.href = "/category/main";
       } else {
           // 실패했을 때의 처리
           alert("메뉴 등록에 실패하였습니다");
       }
   }).catch(error => console.log(error));
});