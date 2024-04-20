const menuUpdate = (id) => {
    const newName = document.getElementById("name").value;
    const newPrice = document.getElementById("price1").value;
    const newInfo = document.getElementById("info1").value;
    if (!newName || !newPrice || !newInfo) {
        alert("메뉴 이름, 가격, 정보를 입력해주세요");
        return;
    }
    fetch(`/menu/updateMenu/${id}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify({name: newName, price: newPrice, info: newInfo}),
    }).then(response => {
        if (response.ok) {
            window.location.href = '/category/main';
        } else {
            console.log("실패");
        }
    }).catch(error => console.error('Error : ', error));
}

function deleteImage(id){
    fetch(`/menu/image/${id}`, {
        method: 'DELETE',
    }).then(response=>{
        return response.text();
    }).then(data => {
        if(data === 'ok'){
            document.querySelector('.flex img').src='data:image/gif;base64,R0lGODlhAQABAAD/ACwAAAAAAQABAAACADs=';
            document.querySelector('#deleteImageButton').style.display='none';
        }else{
            console.log('삭제 실패');
        }
    }).catch(error => {
        console.error('확인 실패', error);
    });
}