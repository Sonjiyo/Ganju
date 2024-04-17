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

document.getElementById('deleteImageButton').addEventListener('click', function () {
    const imgBox = document.querySelector('.imgbox');
    const img = imgBox.querySelector('img');
    if (img) {
        imgBox.removeChild(img);
    }
})