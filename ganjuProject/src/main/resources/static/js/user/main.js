const buttons = document.querySelectorAll(".tab-btn");
const contents = document.querySelectorAll(".tab-content");
const menusContainer = document.querySelector('.menus-container');
const categoryContent = document.querySelector('.category-content');

// 메인페이지 메뉴 슬릭 슬라이더
$(document).ready(function(){
    var $slider = $('.restaurant-image-slider').slick({
        infinite: true,
        slidesToShow: 1,
        slidesToScroll: 1,
        autoplay: true,
        autoplaySpeed: 2000,
        arrows: false, // 화살표 버튼 숨김
        dots: false, // 도트(하단 네비게이션) 숨김
    });

    // 슬라이드 카운터 업데이트
    $slider.on('init reInit afterChange', function (event, slick, currentSlide) {
        var i = (currentSlide ? currentSlide : 0) + 1;
        $('.slider-counter .current').text(i);
        $('.slider-counter .total').text(slick.slideCount);
    });

    // 초기 슬라이드 카운터 업데이트
    $slider.slick('setPosition');
});


// 화면 맨 위로 올라가는 버튼 관련
const scrollTopBtn = document.querySelector('.scroll-to-top');

/* 식당 평점 별 부분*/
function ratingStar() {
    const rating = document.querySelector('.ratings');
    const stars = rating.querySelectorAll('.stars span i');
    const label = rating.querySelector('label');
    const ratingValue = parseFloat(label.textContent);

    // 별 아이콘 초기화
    stars.forEach(star => {
        star.classList.remove('fas', 'fa-star', 'fa-star-half-alt');
        star.classList.add('far', 'fa-star');
        star.style.color = 'var(--light-gray)'; // 기본 별 색상
    });

    // 평점에 따라 별 아이콘 적용
    for (let i = 0; i < stars.length; i++) {
        if ((ratingValue - i) > 1) {
            stars[i].classList.remove('far', 'fa-star-half-alt');
            stars[i].classList.add('fas', 'fa-star');
            stars[i].style.color = 'var(--gold)'; // 채워진 별 색상
        } else if ((ratingValue - i) >= 0.5) {
            stars[i].classList.remove('far', 'fa-star');
            stars[i].classList.add('fas', 'fa-star-half-alt');
            stars[i].style.color = 'var(--gold)'; // 반 채워진 별 색상
            break;
        }
    }
}

// 슬라이더 부분
const slider = document.querySelector('.category-content');
let isDown = false;
let startX;
let scrollLeft;

slider.addEventListener('mousedown', (e) => {
    isDown = true;
    slider.classList.add('active');
    startX = e.pageX - slider.offsetLeft;
    scrollLeft = slider.scrollLeft;
});

slider.addEventListener('mouseleave', () => {
    isDown = false;
    slider.classList.remove('active');
});

slider.addEventListener('mouseup', () => {
    isDown = false;
    slider.classList.remove('active');
});

slider.addEventListener('mousemove', (e) => {
    if (!isDown) return;
    e.preventDefault();
    const x = e.pageX - slider.offsetLeft;
    const walk = (x - startX) * 3; //scroll-fast
    slider.scrollLeft = scrollLeft - walk;
});


// 카테고리 버튼 색상 변경 이벤트
function setActiveCategory() {
    const categories = document.querySelectorAll('.category');
    const menuContainers = document.querySelectorAll('.menu-category');

    let currentActiveIndex = 0;
    menuContainers.forEach((container, index) => {
        const containerTop = container.getBoundingClientRect().top;
        if (containerTop - window.innerHeight / 2 < 0) {
            currentActiveIndex = index;
        }
    });
    categories.forEach((category, index) => {
        if (index === currentActiveIndex) {
            category.classList.add('active');
            ensureCategoryVisible(category);
        } else {
            category.classList.remove('active');
        }
    });
    // 카테고리 클릭 이벤트
    categories.forEach(category => {
        category.addEventListener('click', categoryClickListener);
    });
}

// 클릭한 카테고리가 화면에 완전히 보이지 않을 경우 스크롤
function ensureCategoryVisible(category) {
    const categoryRect = category.getBoundingClientRect();
    const containerRect = categoryContent.getBoundingClientRect();

    if (categoryRect.left < containerRect.left) {
        // 카테고리 버튼이 뷰포트 왼쪽 밖에 위치한 경우
        categoryContent.scrollLeft -= (containerRect.left - categoryRect.left) + 20; // 여백 추가
    } else if (categoryRect.right > containerRect.right) {
        // 카테고리 버튼이 뷰포트 오른쪽 밖에 위치한 경우
        categoryContent.scrollLeft += (categoryRect.right - containerRect.right) + 20; // 여백 추가
    }
}

// 카테고리 클릭 리스너를 위한 분리된 함수
let categoryBtn = true;
function categoryClickListener(e) {

    if(categoryBtn) {
        const targetId = e.currentTarget.getAttribute('data-target');
        const targetElement = document.getElementById(targetId);

        if (targetElement) {
            // category-content의 높이를 가져옵니다.
            const categoryContentHeight = document.querySelector('.category-content').offsetHeight;

            // targetElement까지의 절대 위치를 계산합니다.
            const elementPosition = targetElement.getBoundingClientRect().top + window.pageYOffset;

            // category-content의 높이만큼 위치를 조정합니다.
            const offsetPosition = elementPosition - categoryContentHeight;

            // 계산된 위치로 스크롤합니다.
            window.scrollTo({
                top: offsetPosition,
                behavior: "smooth"
            });
        }
        // 현재 클릭된 카테고리가 화면에 완전히 보이도록 스크롤 조정
        ensureCategoryVisible(e.currentTarget); // 여기서 this는 현재 클릭된 카테고리 요소입니다.

        categoryBtn = false;
        setTimeout( ()=>{
            categoryBtn = true;
        }, 1000);
    }
}

// 페이지 로드 시 .category-content의 원래 위치를 계산하여 저장
const stickyElement = document.querySelector('.category-content');
const headerOffset = document.querySelector('header').offsetHeight; // 만약 header가 있다면
const originalOffsetTop = stickyElement.offsetTop - headerOffset; // header 높이를 고려한 조정값

// 스크롤 이벤트 리스너를 추가하는 새 함수
window.addEventListener('scroll', () => {
    if (window.pageYOffset >= originalOffsetTop) {
        stickyElement.classList.add('sticky');
    } else {
        stickyElement.classList.remove('sticky');
    }

    if (window.scrollY > 100) { // 사용자가 100px 이상 스크롤하면 버튼 표시
        scrollTopBtn.style.display = 'flex';
    } else {
        scrollTopBtn.style.display = 'none';
    }
});

// 리뷰 별점 정수값을 별 개수로 표시하는
function reviewStar(star) {
    const rating = parseInt(star.getAttribute('data-rating'));
    const totalStars = 5;
    let starsHtml = '';

    // 채워진 별 생성
    for (let i = 1; i <= rating; i++) {
        starsHtml += '<span><i class="fas fa-star"></i></span>';
    }

    // 빈 별 생성
    for (let i = rating + 1; i <= totalStars; i++) {
        starsHtml += '<span><i class="far fa-star"></i></span>';
    }

    // 별점을 HTML에 삽입
    star.innerHTML = starsHtml;
}

document.addEventListener('DOMContentLoaded', () => {
    // 별포 표시 부분 함수
    ratingStar();

    // 스크롤 이벤트
    window.addEventListener('scroll', () =>{
        setActiveCategory();
    });

    // 초기 활성화 카테고리 설정
    setActiveCategory();
});

scrollTopBtn.addEventListener('click', () => {
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });
});

document.addEventListener("DOMContentLoaded", () =>{
    const activeTab = Array.from(contents).find(tab => tab.style.display !== 'none');

    let reviewPlus = document.querySelector(".review-plus");
    reviewPlus.style.display = 'block';

    if(activeTab.className.split('-')[0] == 'menu'){
        reviewPlus.style.display = 'none';
    }else{
        reviewPlus.style.display = 'block';
    }
    console.log("탭버튼 = " + activeTab.className.split('-')[0]);
    fetchValidate(activeTab.className.split('-')[0]);
})

// 요소의 크기를 계산하고 스타일을 업데이트하는 함수
function updatePosition() {
    // .innerBox 요소의 가로 길이를 가져옵니다.
    const innerBoxWidth = document.querySelector('.innerBox').offsetWidth;

    // 위치를 조정할 요소를 선택합니다. (예를 들어, '.target-element'라고 가정합니다)
    const targetElement = document.querySelector('.scroll-to-top');

    // 계산된 값을 사용하여 위치를 조정합니다.
    // '--innerBox-width' 커스텀 속성을 업데이트하거나, 직접 'left' 스타일을 설정할 수 있습니다.
    targetElement.style.left = `calc(50% + ${innerBoxWidth / 2}px - 70px)`;
}

// 처음 페이지가 로드될 때 함수를 호출하여 초기 위치를 설정합니다.
updatePosition();

window.addEventListener('resize', updatePosition);