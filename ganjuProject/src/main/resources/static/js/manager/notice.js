const addNotice = () => {
    location.href = "/manager/addNotice";
}

function myFunction(boardNumber) {
    const dropdownContent = document.getElementById("myDropdown" + boardNumber);
    const isVisible = dropdownContent.classList.contains("show");
    closeAllDropdowns();
    if (!isVisible) {
        dropdownContent.classList.add("show");
    }
}

window.onclick = function (e) {
    // 만약 클릭된 요소가 dropbtn 이 아니라면 모든 드롭다운을 닫습니다.
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
            const nm = button.closest('.notice_menu');
            const id = button.dataset.id;
            if (id) {
                deleteNotice(id, nm);
            } else {
                console.error("id 정의 되지 않음");
            }
        })
    })
})

function deleteNotice(id, nm) {
    fetch(`/manager/notice/${id}`, {
        method: 'DELETE',
    }).then(response => {
        if(!response.ok) {
            throw new Error('삭제 실패');
        }
        nm.remove();
    }).catch(error => {
        console.error('삭제 실패함', error);
    });
}