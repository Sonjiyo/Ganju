const menu = document.querySelector(".menu-btn");
const announcement = document.querySelector(".announcement-btn");
const reviews = document.querySelector(".reviews-btn");
const menuContent = document.querySelector('.menu-content');
const announcementContent = document.querySelector('.announcement-content');
const reviewContent = document.querySelector('.review-content');

/* 식당 평점 별 부분*/
document.addEventListener('DOMContentLoaded', function() {
    var ratings = document.querySelectorAll('.ratings');

    ratings.forEach(function(rating) {
        var stars = rating.querySelector('.stars');
        var label = rating.querySelector('label');
        var ratingValue = parseFloat(label.textContent);
        var starPercentage = (ratingValue / 5) * 100; // 평점을 백분율로 변환
        var starPercentageRounded = `${parseFloat(starPercentage.toFixed(1))}%`; // 첫 번째 소수점 자리까지 표시

        // 금색 별을 표시할 span 요소 생성 및 스타일 설정
        var starFill = document.createElement('span');
        starFill.className = 'star-fill';
        starFill.style.width = starPercentageRounded;
        starFill.innerHTML = '★★★★★'; // 금색 별

        // 기존 별점 요소를 클리어하고, 금색 별 span과 기본 별을 추가
        stars.innerHTML = ''; // 기존 내용을 클리어
        stars.appendChild(starFill); // 금색 별 추가
        stars.innerHTML += '★★★★★'; // 기본 별 추가 (회색 별)
    });
});

/* 메뉴 버튼 클릭 시 */
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

/* 공지사항 버튼 클릭 시 */
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


/* 리뷰 버튼 클릭 시 */
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

// 호출하기 버튼 클릭 이벤트
document.querySelector('.call-button').addEventListener('click', () => {
    document.getElementById('callModal').style.display = 'block';
});

// 호출하기 모달에서 호출하기 버튼 클릭 이벤트
document.getElementById('submitCall').addEventListener('click', () => {
    // 실제 애플리케이션에서는 이곳에 선택된 옵션을 처리하는 로직을 구현합니다.
    // 예: 선택된 라디오 버튼의 값을 서버로 전송
    console.log('호출 옵션:', document.querySelector('input[name="callOption"]:checked').value);
    document.getElementById('callModal').style.display = 'none'; // 모달 닫기
});

// 모달 창 밖을 클릭할 때 모달 창 닫기
window.addEventListener('click', e => {

    if (e.target.classList.contains('modal')) {
        e.target.style.display = 'none';
    }
});


// 슬라이더 부분
document.addEventListener('DOMContentLoaded', function() {
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
        if(!isDown) return;
        e.preventDefault();
        const x = e.pageX - slider.offsetLeft;
        const walk = (x - startX) * 3; //scroll-fast
        slider.scrollLeft = scrollLeft - walk;
    });
});

document.addEventListener('DOMContentLoaded', () => {
    const categories = document.querySelectorAll('.category');
    const menuContainers = document.querySelectorAll('.menu-category');
    const categoryContent = document.querySelector('.category-content');

    // 현재 활성화된 카테고리를 표시하고 스크롤 시 갱신
    function setActiveCategory() {
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
            } else {
                category.classList.remove('active');
            }
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

    // 카테고리 클릭 이벤트
    categories.forEach(category => {
        category.addEventListener('click', function() {
            const targetId = this.getAttribute('data-target');
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
            ensureCategoryVisible(this); // 여기서 this는 현재 클릭된 카테고리 요소입니다.
        });
    });

    // 스크롤 이벤트
    window.addEventListener('scroll', setActiveCategory);

    // 초기 활성화 카테고리 설정
    setActiveCategory();
});

// 스크롤 이벤트 리스너를 추가하는 새 함수
window.addEventListener('scroll', function() {
    var stickyElement = document.querySelector('.category-content');
    var headerOffset = document.querySelector('header').offsetHeight; // 만약 header가 있다면
    var stickyOffset = stickyElement.offsetTop - headerOffset; // header 높이를 고려한 조정값
  
    if (window.pageYOffset >= stickyOffset) {
      stickyElement.classList.add('sticky');
    } else {
      stickyElement.classList.remove('sticky');
    }
});

// 리뷰 별점 정수값을 별 개수로 표시하는
document.addEventListener('DOMContentLoaded', function() {
    const reviewStars = document.querySelectorAll('.review .star');

    reviewStars.forEach(function(star) {
        const rating = parseInt(star.getAttribute('data-rating'));
        let starsText = '★★★★★';

        // 노란색 별을 표시할 부분과 회색 별을 표시할 부분을 결정
        let filledStars = starsText.slice(0, rating).replace(/★/g, '<span class="filled">★</span>');
        let emptyStars = starsText.slice(rating).replace(/★/g, '<span class="empty">★</span>');

        // 별점을 HTML로 설정
        star.innerHTML = filledStars + emptyStars;
    });
});