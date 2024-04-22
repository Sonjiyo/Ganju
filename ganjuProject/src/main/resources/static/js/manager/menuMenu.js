function myFunction(menuNumber) {
    const dropdownContent = document.getElementById("myDropdown" + menuNumber);
    const isVisible = dropdownContent.classList.contains("show");
    closeAllDropdowns();
    if (!isVisible) {
        dropdownContent.classList.add("show");
    }
}

window.onclick = function (e) {
    // 만약 클릭된 요소가 dropbtn이 아니라면 모든 드롭다운을 닫습니다.
    if (!e.target.matches('.dropbtn')) {
        closeAllDropdowns();
    }
}

function closeAllDropdowns() {
    const dropdowns = document.querySelectorAll(".dropdown-content");
    dropdowns.forEach(dropdown => {
        if (dropdown.classList.contains("show")) {
            dropdown.classList.remove("show");
        }
    });
}

// 삭제 버튼 클릭 시 해당 메뉴 삭제
document.addEventListener('DOMContentLoaded', function () {
    const deleteButtons = document.querySelectorAll('.delete-button');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function () {
            const navNav = button.closest('.menu_list-edit');
            const id = button.dataset.id;
            if (id) {
                deleteMenu(id, navNav);
            } else {
                console.error("id 정의 되지 않음");
            }
        })
    })
})

function deleteMenu(id, navNav) {
    fetch(`/menu/${id}`, {
        method: 'DELETE',
    }).then(response => {
        if (!response.ok) {
            throw new Error('삭제 실패');
        }
        navNav.remove();
    }).catch(error => {
        console.error('삭제 실패함', error);
    });
}

// 최대 선택 가능한 체크박스 수
const maxAllowed = 5;

// 모든 체크박스 요소를 가져옵니다
const checkboxes = document.querySelectorAll('.checkbox');

// 체크박스의 변경 이벤트를 감지하여 처리합니다
checkboxes.forEach(function (checkbox) {
    checkbox.addEventListener('change', function () {
        // 현재 선택된 체크박스 수를 계산합니다.
        const checkedCount = document.querySelectorAll('.checkbox:checked').length;
        // 만약 선택된 체크박스 수가 최대 허용치를 초과하면, 모든 체크박스를 비활성화합니다
        if (checkedCount >= maxAllowed) {
            checkboxes.forEach(function (checkbox) {
                if (!checkbox.checked) {
                    checkbox.disabled = true;
                }
            });
        } else {
            // 그렇지 않으면 모든 체크박스를 활성화합니다
            checkboxes.forEach(function (checkbox) {
                checkbox.disabled = false;
            });
        }
    });
});