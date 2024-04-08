
// 탭 버튼 클릭 이벤트 핸들러
function handleTabClick(e) {
    // 모든 버튼과 컨텐츠 초기화
    buttons.forEach(btn => {
        btn.style.backgroundColor = 'inherit';
        btn.style.color = '#717171';
    });

    contents.forEach(content => {
        content.style.display = 'none';
    });

    // 클릭된 버튼과 관련된 컨텐츠 활성화
    const selectedContentId = e.target.getAttribute('data-target');
    const selectedContent = document.querySelector(`.${selectedContentId}`);

    e.target.style.backgroundColor = 'var(--orange)';
    e.target.style.color = 'var(--white)';
    selectedContent.style.display = 'block';
}
// 해당 버튼을 클릭 했을 때 비동기로 데이터 불러온
function fetchTap(e){
    const target = e.getAttribute('data-target').split("-")[0];
    const capitalizedTarget = target.charAt(0).toUpperCase() + target.slice(1);
    const url = `/${target}/validateMenu${capitalizedTarget}`;


    fetch(url)
        .then(response => {
            if (!response.ok) {
                throw new Error('Network response was not ok');
            }
            return response.json();
        })
        .then(data => {
            console.log(data);
            if(target === 'board'){
                boardList(data);
            }else if(target === 'menu'){
                menuList(data);
            }else if(target === 'review'){
                reviewList(data);
            }
            // 가져온 데이터로 DOM 업데이트
            // 예: document.querySelector('.content').innerHTML = data.content;
        })
        .catch(error => {
            console.error('Fetch error:', error);
        });
}

// 게시글 비동기로 불러 왔을 때
function boardList(data){
    console.log("보드 비동기 뿌리기")
    const boardContent = document.querySelector('.board-content');
    // 기존 콘텐츠를 초기화
    boardContent.innerHTML = '';

    const boardDiv = document.createElement('div');
    boardDiv.className = 'board';

    data.boards.forEach(review => {
        const boardItemDiv = document.createElement('div');
        boardItemDiv.className = 'board-item';

        const titleDiv = document.createElement('div');
        titleDiv.className = 'title';
        const textDiv = document.createElement('div');
        textDiv.className = 'text';
        textDiv.textContent = review.title;
        const dateDiv = document.createElement('div');
        dateDiv.className = 'date';
        // 날짜 형식은 필요에 따라 조정하세요
        dateDiv.textContent = new Date(review.date).toLocaleString();

        titleDiv.appendChild(textDiv);
        titleDiv.appendChild(dateDiv);

        const contentDiv = document.createElement('div');
        contentDiv.className = 'content';
        contentDiv.textContent = review.content;

        boardItemDiv.appendChild(titleDiv);
        boardItemDiv.appendChild(contentDiv);

        boardDiv.appendChild(boardItemDiv);
    });

    boardContent.appendChild(boardDiv);
}

// 리뷰 비동기로 불러 왔을 때
function reviewList(data){
    const reviewContent = document.querySelector('.review-content');
    // 기존 리뷰 콘텐츠를 초기화
    reviewContent.innerHTML = '';

    data.reviews.forEach(review => {
        // 리뷰 컨테이너 생성
        const reviewDiv = document.createElement('div');
        reviewDiv.className = 'review';

        // 리뷰 제목 (여기서는 사용자 이름을 제목으로 사용)
        const titleDiv = document.createElement('div');
        titleDiv.className = 'title';
        titleDiv.textContent = review.user; // 'user'는 서버에서 받은 사용자 이름에 해당하는 필드

        // 중간 컨테이너 (별점과 날짜를 포함)
        const midDiv = document.createElement('div');
        midDiv.className = 'mid';

        // 별점
        const starDiv = document.createElement('div');
        starDiv.className = 'star';
        starDiv.setAttribute('data-rating', review.rating); // 'rating'은 서버에서 받은 별점 데이터에 해당하는 필드
        // 별점 표시 로직을 호출 (별점에 따라 별 아이콘 생성)
        generateStarsForRating(starDiv, review.rating);

        // 날짜
        const dateDiv = document.createElement('div');
        dateDiv.className = 'date';
        dateDiv.textContent = review.date; // 'date'는 서버에서 받은 리뷰 날짜에 해당하는 필드

        // 리뷰 내용
        const contentDiv = document.createElement('div');
        contentDiv.className = 'content';
        contentDiv.textContent = review.content; // 'content'는 서버에서 받은 리뷰 내용에 해당하는 필드

        // 요소들을 DOM에 삽입
        midDiv.appendChild(starDiv);
        midDiv.appendChild(dateDiv);
        reviewDiv.appendChild(titleDiv);
        reviewDiv.appendChild(midDiv);
        reviewDiv.appendChild(contentDiv);

        reviewContent.appendChild(reviewDiv);
    });
}

// 메뉴 비동기로 불러 왔을 때
function menuList(data){

    console.log("메뉴 비동기 뿌리기")
    // 기존 콘텐츠를 초기화
    categoryContent.innerHTML = '';
    menusContainer.innerHTML = '';

    // 카테고리 목록을 생성
    data.categories.forEach(category => {
        const categoryDiv = document.createElement('div');
        categoryDiv.className = 'category';
        categoryDiv.textContent = category.name;
        categoryDiv.setAttribute('data-targets', 'menu-category-' + category.id);
        categoryContent.appendChild(categoryDiv);

        // 해당 카테고리의 메뉴 목록을 생성
        const menuCategoryDiv = document.createElement('div');
        menuCategoryDiv.className = 'menu-category';
        menuCategoryDiv.id = 'menu-category-' + category.id;

        const categoryNameDiv = document.createElement('div');
        categoryNameDiv.className = 'category-name';
        categoryNameDiv.textContent = category.name;
        menuCategoryDiv.appendChild(categoryNameDiv);

        const menusDiv = document.createElement('div');
        menusDiv.className = 'menus';
        data.menus.filter(menu => menu.category.id === category.id).forEach(menu => {
            const menuDiv = document.createElement('div');
            menuDiv.className = 'menu';
            menuDiv.onclick = function() {
                window.location.href = '/menu/info?id=' + menu.id;
            };

            const imageDiv = document.createElement('div');
            imageDiv.className = 'image';
            const img = document.createElement('img');
            img.src = '/images/sample.png'; // 여기는 실제 메뉴 이미지 URL을 사용해야 함
            img.alt = '메뉴 이미지';
            imageDiv.appendChild(img);

            const textDiv = document.createElement('div');
            textDiv.className = 'text';
            const menuNameDiv = document.createElement('div');
            menuNameDiv.className = 'menu-name';
            menuNameDiv.textContent = menu.name;
            const menuInfoDiv = document.createElement('div');
            menuInfoDiv.className = 'menu-info';
            menuInfoDiv.textContent = menu.info || '맛있음'; // 예시 데이터
            const menuPriceDiv = document.createElement('div');
            menuPriceDiv.className = 'menu-price';
            menuPriceDiv.textContent = menu.price + '원';

            textDiv.appendChild(menuNameDiv);
            textDiv.appendChild(menuInfoDiv);
            textDiv.appendChild(menuPriceDiv);

            menuDiv.appendChild(imageDiv);
            menuDiv.appendChild(textDiv);

            menusDiv.appendChild(menuDiv);
        });

        menuCategoryDiv.appendChild(menusDiv);
        menusContainer.appendChild(menuCategoryDiv);
    });

    const newCategories = document.querySelectorAll('.category');
    newCategories.forEach(category => {
        category.removeEventListener('click', categoryClickListener); // 기존 리스너 제거
        category.addEventListener('click', categoryClickListener); // 새 리스너 추가
    });
}

// 각 버튼에 이벤트 리스너 추가
buttons.forEach(button => {
    button.addEventListener('click', e =>{
        const targetElement = e.target; // 클릭된 타겟 요소
        fetchTap(targetElement); // 수정: e 대신 targetElement를 전달
    });
});

// 비동기로 새로 생성된 요소에 대해 이벤트 위임 사용
categoryContent.addEventListener('click', e => {
    if(e.target && e.target.matches('.category')){
        // fetchTap에 전달할 올바른 매개변수를 결정합니다.
        // 이 경우 e.target이 적절할 수 있습니다.
        fetchTap(e.target); // e 대신 e.target을 전달
    }
});