
const menu = document.querySelector(".menu-btn");
const announcement = document.querySelector(".announcement-btn");
const reviews = document.querySelector(".reviews-btn");
const menuContent = document.querySelector('.menu-content');
const announcementContent = document.querySelector('.announcement-content');
const reviewContent = document.querySelector('.review-content');


menu.addEventListener('click', () => {
    menu.style.backgroundColor = '#ff7a2f';
    announcement.style.backgroundColor = 'inherit';
    reviews.style.backgroundColor = 'inherit';

    menu.style.color = '#fff';
    announcement.style.color = '#717171';
    reviews.style.color = '#717171';

    menuContent.style.display = 'block';
    announcementContent.style.display = 'none';
    reviewContent.style.display = 'none';
});

announcement.addEventListener('click', () => {
    menu.style.backgroundColor = 'inherit';
    announcement.style.backgroundColor = '#ff7a2f';
    reviews.style.backgroundColor = 'inherit';

    menu.style.color = '#717171';
    announcement.style.color = '#fff';
    reviews.style.color = '#717171';

    menuContent.style.display = 'none';
    announcementContent.style.display = 'block';
    reviewContent.style.display = 'none';
});

reviews.addEventListener('click', () => {
    menu.style.backgroundColor = 'inherit';
    announcement.style.backgroundColor = 'inherit';
    reviews.style.backgroundColor = '#ff7a2f';

    menu.style.color = '#717171';
    announcement.style.color = '#717171';
    reviews.style.color = '#fff';

    menuContent.style.display = 'none';
    announcementContent.style.display = 'none';
    reviewContent.style.display = 'block';
});

// 신고 버튼 클릭 이벤트
document.querySelector('.report-button').addEventListener('click', () => {
    document.getElementById('reportModal').style.display = 'block';
});

// 신고하기 버튼 클릭 이벤트로 모달 창 닫기 (옵션)
document.querySelector('.modal-submit').addEventListener('click', () => {
    document.getElementById('reportModal').style.display = 'none';
});

// 슬릭 슬라이더
$('.category-content').slick({
    dots: false,
    infinite: false,
    speed: 300,
    slidesToShow: 3
});


// 카테고리를 클릭 했을 때 해당 메뉴 부분으로 가능 기능
document.addEventListener('DOMContentLoaded', function() {
    const categories = document.querySelectorAll('.category');

    categories.forEach(category => {
        category.addEventListener('click', function() {
            const categoryId = this.dataset.categoryId;
            scrollToMenuCategory(categoryId);
        });
    });

    function scrollToMenuCategory(categoryId) {
        const menuCategory = document.querySelector(`.menu-category[data-category-id='${categoryId}']`);
        if (menuCategory) {
            menuCategory.scrollIntoView({ behavior: 'smooth', block: 'start' });
        }
    }
});