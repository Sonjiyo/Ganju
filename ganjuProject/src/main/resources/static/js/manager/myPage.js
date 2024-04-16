const myPageEdit = () => {
    location.href = "/manager/myPageEdit";
}

function logoutUser() {
    localStorage.removeItem('user');
    location.href = '/logout';
}