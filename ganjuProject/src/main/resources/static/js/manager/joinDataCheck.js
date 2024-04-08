let msg = document.createElement('p');
msg.classList.add('msg');
let msgOk = document.createElement('p');
msgOk.classList.add('msg');
msgOk.classList.add('msg-ok');

let validIdCheck = false;
let checkButton = document.querySelector('.check-button');
//중복 아이디 찾기
function validId(form){
    if(!form.loginId.value.trim()){
        msg.textContent='아이디를 입력해주세요';
        document.querySelector('.login-id').appendChild(msg);
        return false;
    }

    const loginId = form.querySelector('#loginId').value;

    fetch(`/manager/join/${loginId}`, {
        method: 'GET',
    }).then(response=>{
        return response.text();
    }).then(data => {
            console.log(data);
        msg.textContent='';
        msgOk.textContent='';
        if(data === 'ok'){
            validIdCheck = true;
            checkButton.value='확인완료';
            checkButton.style.background='#222';
            msgOk.textContent='사용 가능한 아이디입니다';
            document.querySelector('.login-id').appendChild(msgOk);
        }else{
            validIdCheck = false;
            msg.textContent= "이미 사용 중인 아이디입니다";
            document.querySelector('.login-id').appendChild(msg);
        }
    }).catch(error => {
        console.error('확인 실패', error);
    });
}

//아이디 리체크
function idReCheck(){
    validIdCheck = false;
    msg.textContent='';
    msgOk.textContent='';
    checkButton.value='중복체크';
    checkButton.style.background='#ff7a2f';
}

let loginIdValue = "";
let passwordValue = "";
let phoneValue = "";
let emailValue = "";
// 회원가입 정보


function joinCheck(form){
    msg.textContent='';
    msgOk.textContent='';

    //아이디 체크
    if(!form.loginId.value.trim()){
        msg.textContent='아이디를 입력해주세요';
        document.querySelector('.login-id').appendChild(msg);
        return false;
    }
    if(!form.loginId.value.trim().match(/^[A-Za-z]{1}[A-Za-z\d]{2,19}$/)){
        msg.textContent='영어 포함 3자이상 20자 미만으로 작성해주세요';
        document.querySelector('.login-id').appendChild(msg);
        return false;
    }

    //비밀번호 체크
    if(!form.password.value.trim()){
        msg.textContent='비밀번호를 입력해주세요';
        document.querySelector('.password').appendChild(msg);
        return false;
    }
    if(!form.password.value.trim().match(/^(?=.*[A-Za-z])(?=.*\d)[A-Za-z\d]{4,20}$/)){
        msg.textContent='영어 포함 3자이상 20자 미만으로 작성해주세요';
        document.querySelector('.password').appendChild(msg);
        return false;
    }

    if(!form.passwordCheck.value.trim()){
        msg.textContent='비밀번호를 다시 한 번 입력해주세요';
        document.querySelector('.password-check').appendChild(msg);
        return false;
    }
    //번호
    if(!form.phone.value.trim()){
        msg.textContent='휴대전화 번호를 입력해주세요';
        document.querySelector('.phone').appendChild(msg);
        return false;
    }
    if (!form.phone.value.match(/^[\d]{2,3}-[\d]{3,4}-[\d]{4}$/)) {
		msg.textContent= "전화번호 형식에 맞게 입력해주세요";
		document.querySelector('.phone').appendChild(msg);
		return false;
	}

    if(!form.terms.checked){
        msg.textContent= "약관에 동의해주세요";
		document.querySelector('.terms').appendChild(msg);
		return false;
    }
    
    if(!validIdCheck){
        msg.textContent= "아이디 중복 체크 해주세요";
        document.querySelector('.login-id').appendChild(msg);
        return false;
    }

    loginIdValue = form.loginId.value;
    passwordValue = form.password.value;
    phoneValue = form.phone.value;

    document.querySelector(".container").innerHTML='' +
        '<h2>원활한 이용을 위해<br>본인인증이 필요합니다</h2>\n' +
        '<form action="" method="post">\n' +
        '                <div class="email">\n' +
        '<label for="email">이메일<span> *</span></label>\n' +
        '<input type="email" name="email" id="email" placeholder="이메일을 입력해주세요" onkeyup="emailCheck(form)">\n' +
        '</div>\n' +
        '<input type="button" class="button-verification" value="인증번호 발송" onclick="countDown(form)">\n' +
        '<div class="verification">\n' +
        '<label for="verification">인증번호 입력<span> *</span></label>\n' +
        '<input type="number" name="verification" id="verification">\n' +
        '</div>\n' +
        '<input type="button" class="button" value="본인인증" onclick="verificationCheck(form)">\n' +
        '</form>';
}

function passwordCompare(form){
    if(form.password.value.trim() !== form.passwordCheck.value.trim()){
        msgOk.textContent='';
        msg.textContent='비밀번호가 맞지 않습니다.';
        document.querySelector('.password-check').appendChild(msg);
        return false;
    }else{
        msg.textContent='';
        msgOk.textContent='비밀번호가 같습니다.'
        document.querySelector('.password-check').appendChild(msgOk);
    }
}

//핸드폰 번호 자동 완성
function autoHyphen(form){
    form.phone.value = form.phone.value
    .replace(/[^0-9]/g, '')
    .replace(/^(\d{2,3})(\d{3,4})(\d{4})$/, `$1-$2-$3`);
}

//본인인증------------------------------------------------------

//이메일 체크
let emailPatternCheck = false;
function emailCheck(form){
    if(!form.email.value.trim()){
        msg.textContent='이메일을 입력해주세요';
        document.querySelector('.email').appendChild(msg);
        emailPatternCheck = false;
        return;
    }
    if (!form.email.value.match(/[a-z0-9._%+-]+@[a-z0-9.-]+\.[a-z]{2,3}$/)) {
		msg.textContent= "이메일 형식에 맞게 입력해주세요";
		document.querySelector('.email').appendChild(msg);
        emailPatternCheck = false;
        return;
	}
    msg.textContent='';
    emailPatternCheck = true;

}

let timeLeft = document.createElement('span');
timeLeft.classList.add('time-left');

//카운트다운
function countDown(form){
    msg.textContent='';

    emailCheck(form);
    if(!emailPatternCheck){return false;}
    timeLeft.innerHTML = 0o3 + ":" + 0o0;
    document.querySelector('.verification label').appendChild(timeLeft);
    startTimer();
}

let timeCheck = true;

function verificationCheck(form){
    msg.textContent='';

    emailCheck(form);
    if(!emailPatternCheck){return false;}

    //인증번호 확인
    if(!form.verification.value.trim()){
        msg.textContent= "인증번호를 입력해주세요";
		document.querySelector('.verification').appendChild(msg);
        return false;
    }

    if(!timeCheck){
        msg.textContent= "입력 시간이 지났습니다. 다시 시도해주세요";
        document.querySelector('.verification').appendChild(msg);
        return false;
    }

    emailValue = form.email.value;

    fetch('/manager/join', {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json'
        },
        body: JSON.stringify({
            loginId: loginIdValue,
            password: passwordValue,
            phone: phoneValue,
            email:emailValue
        })
    }).then(response => {
        if (!response.ok) {
            throw new Error('Network response was not ok');
        }
        console.log('추가 성공');
        location.href="/manager/joinRestaurant";
    }).catch(error => {
        console.error('추가 실패', error);
    });
}

function startTimer() {
    let timeArray = timeLeft.innerHTML.split(/[:]+/);
    let m = timeArray[0];
    let s = checkSecond((timeArray[1] - 1));
    if(s===59){m=m-1}
    if(m<0){
        timeCheck = false;
        return;
    }
    
    timeLeft.innerHTML = m + ":" + s;

    setTimeout(startTimer, 1000);

}

function checkSecond(sec) { //초가 0이 되면 59로 만듦
    if (sec < 10 && sec >= 0) {sec = "0" + sec};
    if (sec < 0) {sec = "59"};
    return sec;
}
