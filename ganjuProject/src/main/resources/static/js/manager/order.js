let listselect = [...document.querySelectorAll('.list-select li')];
listselect.forEach(e=>{
    e.addEventListener('click', ()=>{
        listselect.forEach(en=>en.classList.remove('on'));
        e.classList.add('on');
    })
});
    
let buttons = [...document.querySelectorAll('.content-summary .right button')];
let content = [...document.querySelectorAll('.info')];
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
