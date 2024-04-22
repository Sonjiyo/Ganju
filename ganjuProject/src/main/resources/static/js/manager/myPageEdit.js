const myPageUpdate = () => {
    const inputPass = document.getElementById("password").value;
    const inputPhone = document.getElementById("phone").value; // 새 전화번호 입력 값
    if (confirm("정말 수정하시겠습니까 ?")) {
        fetch('/manager/update', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify({password: inputPass, phone: inputPhone}),
        })
            .then(response => {
                if (response.ok) {
                    alert("수정이 완료되었습니다");
                    window.location.href = '/manager/myPage';
                } else {
                    alert("비밀번호가 일치하지 않거나 전화번호 형식이 올바르지 않습니다");
                    window.location.href = '/manager/myPageEdit';
                }
            })
            .catch(error => console.error('Error checking password:', error));
    }
}