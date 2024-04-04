// 패스워드 숨기기 / 보이기

let passwordEye = document.querySelector('.passwordEye');
let password = document.querySelector('.password');

passwordEye.addEventListener('click', ()=>{
    if(passwordEye.classList.contains('fa-eye')){
        passwordEye.classList.add('fa-eye-slash');
        passwordEye.classList.remove('fa-eye');
        password.type = 'password';
    }else{
        passwordEye.classList.remove('fa-eye-slash');
        passwordEye.classList.add('fa-eye');
        password.type='text';
    } 
})
