function deleteUser(keyId){
    if(confirm("정말 탈퇴하시겠습니까?")){
        fetch(`/user/${keyId}`, {
            method: 'DELETE',
        }).then(response=>{
            return response.text();
        }).then(data => {
            if(data === 'ok'){
                console.log('탈퇴 성공');
                location.href='/logout';
            }else{
                console.log('탈퇴 실패');
            }
        }).catch(error => {
            console.error('확인 실패', error);
        });
    }
}