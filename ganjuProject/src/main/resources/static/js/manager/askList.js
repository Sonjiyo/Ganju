document.addEventListener('DOMContentLoaded', () => {
    buttonOpen();
});

function buttonOpen(){
    let buttons = [...document.querySelectorAll('.title-right button')];
    let content = [...document.querySelectorAll('.content')];
    buttons.forEach(e=>{
        e.addEventListener('click', ()=>{
            if(e.classList.contains('on')){
                e.classList.remove('on');
                content[buttons.indexOf(e)].classList.remove('on');
            }else{
                e.classList.add('on');
                content[buttons.indexOf(e)].classList.add('on');
            }
        })
    })
}