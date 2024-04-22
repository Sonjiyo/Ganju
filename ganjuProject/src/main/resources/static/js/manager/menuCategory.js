// 카테고리 이동 버튼
document.addEventListener('DOMContentLoaded', function () {
    const menuMenus = document.querySelectorAll('.menu_menu');
    menuMenus.forEach((menu) => {
        menu.addEventListener('click', function (event) {
            const target = event.target;
            if (target.classList.contains('fa-chevron-up')) {
                moveUp(menu);
            } else if (target.classList.contains('fa-chevron-down')) {
                moveDown(menu);
            }
        });
    });

    function moveUp(menu) {
        const categoryId = menu.querySelector('p').dataset.id;
        const previousMenu = menu.previousElementSibling;
        if (previousMenu !== null) {
            const previousCategoryId = previousMenu.querySelector('p').dataset.id;
            swapTurn(categoryId, previousCategoryId);
            menu.parentNode.insertBefore(menu, previousMenu);
        }
    }

    function moveDown(menu) {
        const categoryId = menu.querySelector('p').dataset.id;
        const nextMenu = menu.nextElementSibling;
        if (nextMenu !== null) {
            const nextCategoryId = nextMenu.querySelector('p').dataset.id;
            swapTurn(categoryId, nextCategoryId);
            menu.parentNode.insertBefore(nextMenu, menu);
        }
    }

    function swapTurn(id1, id2) {
        fetch("/category/swapTurn", {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({id1, id2}),
        }).then(response => {
            if (!response.ok) {
                throw new Error('네트워크 오류');
            }
            return response.json();
        }).then(data => {
            console.log('턴 변경 성공 : ', data);
        }).catch(error => {
            console.error('턴 변경 실패 : ', error);
        });
    }
});

// 카테고리 삭제 버튼
document.addEventListener('DOMContentLoaded', function () {
    const deleteButtons = document.querySelectorAll('.menu_menu a i.fa-times');
    deleteButtons.forEach(button => {
        button.addEventListener('click', function () {
            const menuMenu = button.closest('.menu_menu');
            menuMenu.remove();
        });
    });
});

const category = document.querySelector('.edit-category');
const menu = document.querySelector('.edit-menu');
const main = document.querySelector('.main-menu');
const cc = document.querySelector('.category-contents');
const menuc = document.querySelector('.menu-contents');
const mainc = document.querySelector('.main-contents');

category.addEventListener('click', () => {
    category.style.color = '#ff7a2f';
    menu.style.color = '#222';
    main.style.color = '#222';
    cc.style.display = 'block';
    menuc.style.display = 'none';
    mainc.style.display = 'none';
})

menu.addEventListener('click', () => {
    category.style.color = '#222';
    menu.style.color = '#ff7a2f';
    main.style.color = '#222';
    menuc.style.display = 'block';
    cc.style.display = 'none';
    mainc.style.display = 'none';
})

main.addEventListener('click', () => {
    category.style.color = '#222';
    menu.style.color = '#222';
    main.style.color = '#ff7a2f';
    mainc.style.display = 'block';
    cc.style.display = 'none';
    menuc.style.display = 'none';
})

document.addEventListener("DOMContentLoaded", function () {
    const deleteBtn = document.querySelectorAll('.category_deleteBtn');
    deleteBtn.forEach(button => {
        button.addEventListener('click', function () {
            const nava = button.closest('.menu_menu');
            const id = button.dataset.id;
            if (id) {
                deleteCategory(id, nava);
            } else {
                console.error("id 정의 되지 않음");
            }
        })
    })
})

function deleteCategory(id, nava) {
    fetch(`/category/${id}`, {
        method: 'DELETE',
    }).then(response => {
        if (!response.ok) {
            throw new Error('삭제 실패');
        }
        nava.remove();
    }).catch(error => {
        console.error('삭제 실패함', error);
    });
}

// 옵션 추가 버튼 클릭 시 모달 창 열기
document.querySelector('.padding').addEventListener('click', function () {
    document.querySelector('.modal').style.display = 'block';
});

// 모달 창 닫기
document.querySelector('.close').addEventListener('click', function () {
    document.querySelector('.modal').style.display = 'none';
});

// 카테고리 추가
document.getElementById("addCategoryBtn").addEventListener("click", async function () {
    const categoryName = document.querySelector('.modal-content textarea').value.trim();
    if (!categoryName) {
        alert("카테고리 이름을 적어주세요");
        return;
    }
    const categoryData = {
        name: categoryName,
    }
    fetch("/category/add", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"
        },
        body: JSON.stringify(categoryData)
    }).then(response => {
        if (response.ok) {
            alert("카테고리 등록 성공");
            console.log("1 : " + categoryName);
            window.location.href = "/category/main";
        } else {
            alert("카테고리 등록 실패");
        }
    }).catch(error => console.log(error));
});

//맨 위로 올라가는 버튼
const scrollTopBtn = document.querySelector('.scroll-to-top');
window.addEventListener('scroll', () => {
    if (window.scrollY > 100) { // 사용자가 100px 이상 스크롤하면 버튼 표시
        scrollTopBtn.style.display = 'flex';
    } else {
        scrollTopBtn.style.display = 'none';
    }
});

scrollTopBtn.addEventListener('click', () => {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
});

function selectMainMenu(){
    // 대표메뉴 설정
    const checkboxes = document.querySelectorAll('input[type=checkbox][name="mainMenu"]:checked');
    const checkedValues = [];
    checkboxes.forEach((checkbox) => {
        checkedValues.push(parseInt(checkbox.value)); // 문자열을 숫자로 변환
    });

    fetch('/menu/mainMenu', {
        method: 'PUT',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify(checkedValues)
    })
        .then(response => {
            if (response.ok) {
                alert('변경 되었습니다');
            } else {
                throw new Error('변경 실패');
            }
        })
        .catch(error => {
            // 오류 처리
            console.error('There was a problem with your fetch operation:', error);
        });

}
