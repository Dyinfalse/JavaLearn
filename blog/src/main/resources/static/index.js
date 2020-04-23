
const mySwiper = new Swiper('.swiper-container', {
    direction: 'vertical',
})

window.onload = function(){
    const XHR = new XMLHttpRequest();
    XHR.open('GET', 'http://localhost:8080/home/getBingImages', true);

    XHR.send({width: 10, height: 10});

    XHR.onreadystatechange = function(){
        if(XHR.readyState === 4 && XHR.status === 200){
            console.log(XHR.responseText)
        }else {
            console.log(XHR.statusText)
        }
    }

}