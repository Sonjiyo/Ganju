// 옵션 추가 버튼 클릭 시 모달 창 열기
document.querySelector('.add-option').addEventListener('click', function () {
    document.querySelector('.modal').style.display = 'block';
});

// 모달 창 닫기
document.querySelector('.close').addEventListener('click', function () {
    document.querySelector('.modal').style.display = 'none';
});

// 세부 옵션 추가 로직
document.getElementById('add_option_value').addEventListener('click', function () {
    const container = document.getElementById('option_values_container');
    const newField = document.createElement('div');
    newField.classList.add('option-value'); // 이 클래스를 추가
    newField.innerHTML = `
        <input type="text" placeholder="세부 옵션 이름" class="option-subname">
        <input type="number" placeholder="가격" class="option-subprice">원
        <span class="remove_option_value" style="cursor: pointer;">[제거]</span> `;
    container.appendChild(newField);
});

// 세부 옵션 제거 기능
document.addEventListener('click', function (e) {
    if (e.target.classList.contains('remove_option_value')) {
        e.target.parentElement.remove();
    }
});

// '옵션 추가 완료' 버튼 클릭 이벤트 핸들러
document.getElementById('submit_option').addEventListener('click', function () {
    // 옵션 타입, 옵션 이름, 세부 옵션 데이터 수집
    const optionType = document.getElementById('option_type').value;
    const optionName = document.getElementById('option_name').value;
    const optionDetails = [...document.querySelectorAll('.option-value')].map(detail => {
        return {
            name: detail.querySelector('.option-subname').value,
            price: detail.querySelector('.option-subprice').value
        };
    });

    // 옵션 데이터를 폼에 추가
    addOptionToForm(optionType, optionName, optionDetails);

    // 모달 닫기
    document.querySelector('.modal').style.display = 'none';
});

function addOptionToForm(optionType, optionName, details) {
    const form = document.getElementById('menuForm');
    const optionDiv = document.createElement('div');
    optionDiv.classList.add('option');

    // 옵션 타입과 이름 표시
    const headerDiv = document.createElement('div');
    headerDiv.classList.add('option-header');

    const nameInput = document.createElement('input');
    nameInput.type = 'text';
    nameInput.value = optionName;
    nameInput.readOnly = true; // 이름을 입력 후 수정할 수 없게 만듭니다. 필요에 따라 readOnly를 제거하세요.
    headerDiv.appendChild(nameInput);

    // Thymeleaf로 생성된 select 태그를 대체할 새로운 select 태그를 생성합니다.
    const typeSelect = document.createElement('select');
    typeSelect.name = 'optionsType'; // 필드 이름 설정

    // 필수와 선택 옵션을 추가합니다.
    const requiredOption = document.createElement('option');
    requiredOption.value = 'REQUIRED';
    requiredOption.textContent = '필수';
    typeSelect.appendChild(requiredOption);

    const optionalOption = document.createElement('option');
    optionalOption.value = 'OPTIONAL';
    optionalOption.textContent = '선택';
    typeSelect.appendChild(optionalOption);

    // optionType 값에 따라 선택된 옵션을 설정합니다.
    if (optionType === 'REQUIRED') {
        requiredOption.selected = true;
    } else {
        optionalOption.selected = true;
    }

    // 폼에 추가합니다.
    headerDiv.appendChild(typeSelect);

    optionDiv.appendChild(headerDiv);

    // 세부 옵션들 표시
    details.forEach((detail, index) => {
        const detailDiv = document.createElement('div');
        detailDiv.classList.add('detail');

        const detailNameInput = document.createElement('input');
        detailNameInput.type = 'text';
        detailNameInput.value = detail.name;
        detailDiv.appendChild(detailNameInput);

        const detailPriceInput = document.createElement('input');
        detailPriceInput.type = 'number';
        detailPriceInput.value = detail.price;
        detailDiv.appendChild(detailPriceInput);

        optionDiv.appendChild(detailDiv);
    });

    // 옵션 삭제 버튼 추가
    const removeButton = document.createElement('button');
    removeButton.textContent = '옵션 삭제';
    removeButton.onclick = function () {
        form.removeChild(optionDiv);
    };
    optionDiv.appendChild(removeButton);

    form.appendChild(optionDiv);

    // 현재 옵션의 순서를 기반으로 한 인덱스 생성
    const index = document.querySelectorAll('.option').length - 1; // 마지막으로 추가된 옵션 인덱스

    // 옵션 타입, 옵션 이름, 세부 옵션 정보를 서버로 전송하기 위한 숨겨진 필드 생성
    createHiddenInput(form, `options[${index}].type`, optionType);
    createHiddenInput(form, `options[${index}].name`, optionName);
    details.forEach((detail, detailIndex) => {
        createHiddenInput(form, `options[${index}].details[${detailIndex}].name`, detail.name);
        createHiddenInput(form, `options[${index}].details[${detailIndex}].price`, detail.price);
    });
}

// 숨겨진 입력 필드를 생성하고 폼에 추가하는 함수
function createHiddenInput(form, name, value) {
    const input = document.createElement('input');
    input.type = 'hidden';
    input.name = name;
    input.value = value;
    form.appendChild(input);
}