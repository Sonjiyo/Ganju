const range = document.querySelector('[name="range"]');
const blackStar = document.querySelector('.star-black');
range.addEventListener('input', function(e) {
    const value = e.target.value;
    blackStar.style.width = value/2 + 'em';
});


function reviewCheck(form){
    form.submit();
}