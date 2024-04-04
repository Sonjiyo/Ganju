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
        const previousMenu = menu.previousElementSibling;
        if (previousMenu !== null) {
            menu.parentNode.insertBefore(menu, previousMenu);
        }
    }

    function moveDown(menu) {
        const nextMenu = menu.nextElementSibling;
        if (nextMenu !== null) {
            menu.parentNode.insertBefore(nextMenu, menu);
        }
    }
});

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

const addMenuReq = () => {
    location.href = "/category/add";
}