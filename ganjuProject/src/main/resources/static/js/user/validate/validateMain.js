const loadMoreReviews = document.getElementById('loadMoreReviews');
// 탭 버튼 클릭 이벤트 핸들러
function handleTabClick(e) {
    currentPage = 0;
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

    // 더보기 버튼을 리뷰 에서만 보이기
    if(selectedContentId !== 'menu-content'){
        loadMoreReviews.style.display = 'block';
    }else{
        loadMoreReviews.style.display = 'none';
    }

    e.target.style.backgroundColor = 'var(--orange)';
    e.target.style.color = 'var(--white)';
    selectedContent.style.display = 'block';
}
// 해당 버튼을 클릭 했을 때 비동기로 데이터 불러온

let currentPage = 0;

function fetchTap(e){
    console.log(e.target);
    const target = e.target.getAttribute('data-target').split("-")[0];
    console.log(target);
    const capitalizedTarget = target.charAt(0).toUpperCase() + target.slice(1);
    let url = `/${target}/validateMenu${capitalizedTarget}`;

    // 리뷰 탭일 경우, 현재 페이지 번호를 URL에 포함
    if(target === 'review'){
        url += `?page=${currentPage}`;
    }

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
                currentPage++;
            }else if(target === 'menu'){
                menuList(data);
            }else if(target === 'review'){
                reviewList(data);
                currentPage++;
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
    const boardContent = document.querySelector('.board-content');
    // 기존 콘텐츠를 초기화
    if(currentPage === 0){
        boardContent.innerHTML = '';
    }

    let boardList = document.querySelectorAll('.board-item');

    if(data.size > boardList.length){
        loadMoreReviews.style.display = "block";
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
            dateDiv.textContent = formatRegDate(review.regDate);

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
    }else{
        loadMoreReviews.style.display = "none";
    }
}

// 리뷰 비동기로 불러 왔을 때
function reviewList(data){
    const reviewContent = document.querySelector('.review-content');
    // 기존 리뷰 콘텐츠를 초기화
    if(currentPage === 0){
        reviewContent.innerHTML = '';
    }

    let reviewList = document.querySelectorAll('.review');

    if(data.size > reviewList.length ) {
        loadMoreReviews.style.display = "block";
        data.reviews.forEach(review => {
            // 리뷰 컨테이너 생성
            const reviewDiv = document.createElement('div');
            reviewDiv.className = 'review';

            // 리뷰 제목 (여기서는 사용자 이름을 제목으로 사용)
            const titleDiv = document.createElement('div');
            titleDiv.className = 'title';
            titleDiv.textContent = review.name; // 'user'는 서버에서 받은 사용자 이름에 해당하는 필드

            // 중간 컨테이너 (별점과 날짜를 포함)
            const midDiv = document.createElement('div');
            midDiv.className = 'mid';

            // 별점
            const starDiv = document.createElement('div');
            starDiv.className = 'star';
            starDiv.setAttribute('data-rating', review.star); // 'rating'은 서버에서 받은 별점 데이터에 해당하는 필드
            // 별점 표시 로직을 호출 (별점에 따라 별 아이콘 생성)
            reviewStar(starDiv);

            // 날짜
            const dateDiv = document.createElement('div');
            dateDiv.className = 'date';
            dateDiv.textContent = formatRegDate(review.regDate); // 'date'는 서버에서 받은 리뷰 날짜에 해당하는 필드

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
    }else{
        loadMoreReviews.style.display = "none";
    }
}

// 메뉴 비동기로 불러 왔을 때
function menuList(data){

    console.log("메뉴 비동기 뿌리기");
    // 기존 콘텐츠를 초기화
    categoryContent.innerHTML = '';
    menusContainer.innerHTML = '';

    // 카테고리 목록을 생성
    data.categories.forEach(category => {
        const categoryDiv = document.createElement('div');
        categoryDiv.className = 'category';
        categoryDiv.textContent = category.name;
        categoryDiv.setAttribute('data-target', 'menu-category-' + category.id);
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
        data.menus.filter(menu => menu.categoryId === category.id).forEach(menu => {
            console.log("menu = " + menu);
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
        category.addEventListener('click', categoryClickListener); // 새 리스너 추가
    });
}

// 각 버튼에 이벤트 리스너 추가
buttons.forEach(button => {
    button.addEventListener('click', e =>{
        handleTabClick(e);
        fetchTap(e);
    });
});

// 비동기로 새로 생성된 요소에 대해 이벤트 위임 사용
categoryContent.addEventListener('click', e => {
    if(e.target && e.target.matches('.category')){
        // fetchTap에 전달할 올바른 매개변수를 결정합니다.
        // 이 경우 e.target이 적절할 수 있습니다.
        fetchTap(e); // e 대신 e.target을 전달
    }
});

// 더보기 이벤트
document.getElementById('loadMoreReviews').addEventListener('click', e =>{
    // 현재 활성화된 탭을 찾습니다.
    const activeTab = Array.from(contents).find(tab => tab.style.display !== 'none');

    console.log(activeTab.className.split('-')[0]);
    fetchValidate(activeTab.className.split('-')[0]);
});

// 리뷰 불러오기 함수 수정
function fetchValidate(active) {
    const capitalizedTarget = active.charAt(0).toUpperCase() + active.slice(1);
    fetch(`/${active}/validateMenu${capitalizedTarget}?page=${currentPage}`)
        .then(response => response.json())
        .then(data => {
            if(active === 'board'){
                boardList(data);
            }else if(active === 'review'){
                reviewList(data);
            }
            currentPage++; // 다음 페이지로 업데이트
        })
        .catch(error => console.error('Fetch error:', error));
}
