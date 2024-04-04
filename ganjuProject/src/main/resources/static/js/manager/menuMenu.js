function myFunction(menuNumber) {
    const dropdownContent = document.getElementById("myDropdown" + menuNumber);
    // 현재 열려있는 드롭다운이 아니라면, 모든 드롭다운을 닫고 클릭된 드롭다운을 엽니다.
    if (!dropdownContent.classList.contains("show")) {
        closeAllDropdowns();
        dropdownContent.classList.add("show");
    } else {
        // 이미 열려 있는 드롭다운이면 그냥 닫습니다.
        dropdownContent.classList.remove("show");
    }
}

window.onclick = function (e) {
    // 만약 클릭된 요소가 dropbtn이 아니라면 모든 드롭다운을 닫습니다.
    if (!e.target.matches('.dropbtn')) {
        closeAllDropdowns();
    }
}

function closeAllDropdowns() {
    const dropdowns = document.getElementsByClassName("dropdown-content");
    for (let i = 0; i < dropdowns.length; i++) {
        const openDropdown = dropdowns[i];
        if (openDropdown.classList.contains('show')) {
            openDropdown.classList.remove('show');
        }
    }
}

// 삭제 버튼 클릭 시 해당 메뉴 삭제
document.addEventListener('DOMContentLoaded', function () {
    const deleteButtons = document.querySelectorAll('.menu_list a delete-button');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function () {
            const navNav = button.closest('.menu_list');
            navNav.remove();
        })
    })
})