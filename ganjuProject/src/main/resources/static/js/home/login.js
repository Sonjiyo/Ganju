let msg = document.createElement('p');
msg.classList.add('msg');

function loginCheck(form){
    msg.textContent='';
    if(!form.loginId.value.trim()){
        msg.textContent='아이디를 입력해주세요';
        document.querySelector('.login-id').appendChild(msg);
        return false;
    }

    if(!form.password.value.trim()){
        msg.textContent='비밀번호를 입력해주세요';
        document.querySelector('.password').appendChild(msg);
        return false;
    }
    form.submit();
}