/* form 하기 전에 체크 */

function infosubmit(form) {
    form.submit();
}

const ocs = document.querySelectorAll('.option-contents-select');

ocs.forEach(select => {
    select.addEventListener('click', e => {
        // 'this' 대신 'select' 사용
        const input = select.querySelector('input[type=checkbox], input[type=radio]');
        if (e.target !== input) {
            input.checked = !input.checked;
            // 라디오 버튼의 경우, 다른 라디오 버튼의 상태 변경을 위해 이벤트를 발생시킵니다.
            input.dispatchEvent(new Event('change'));
        }
    });
});