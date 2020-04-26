
const BASE_URL = '';

const mySwiper = new Swiper('.swiper-container', {
    direction: 'vertical',
});

/**
 * image loading
 */
function loadImage(imageList, loadCallback) {
    let imageLoaded = [];
    for(let i = 0; i < imageList.length; i++){

        let image = document.createElement('img');

        // image.src = imageList[i].url;
        image.src = 'http://localhost:8080/images/74afbe499aa6e6e01bd9d78f4f08f147.jpg';

        // document.getElementsByTagName('body')[0].appendChild(image);

        image.onload = function(){
            imageLoaded.push({ ...imageList[i], error: false});

            if(imageLoaded.length === imageList.length){
                loadCallback(imageLoaded);
            }
        };

        image.onerror = function () {
            imageLoaded.push({ ...imageList[i], error: true});

            if(imageLoaded.length === imageList.length){
                loadCallback(imageLoaded);
            }
        }
    }
}

/**
 * 获取必应背景图
 */
function getBingImages (){
    let width = document.documentElement.offsetWidth;
    let height = document.documentElement.offsetHeight;
    request({
        url: '/home/getBingImages',
        data: { width, height, max: 3 },
        success(res){
            if(res.error === 0){
                loadImage(res.list, function (loadedImageList) {
                    console.log('图片加载完成');
                    for(let i = 0; i < loadedImageList.length; i++){
                        if(loadedImageList[i].error === false){

                            let slider = document.createElement('img');
                            let text = document.createElement('div');

                            text.innerText = loadedImageList[i].title;
                            slider.className = 'swiper-slide';
                            // slider.style.backgroundImage = 'url(' + loadedImageList[i].url + ')';
                            slider.style.backgroundImage = 'http://localhost:8080/images/74afbe499aa6e6e01bd9d78f4f08f147.jpg';

                            slider.appendChild(text);
                            document.getElementById('swiper-wrapper').appendChild(slider);
                        }
                    }
                });
            }
        }
    });
// <div class="swiper-slide">1</div>
}


/**
 * 请求封装
 */
function request ({ url, data = {}, method = 'GET', success }){

    const XHR = new XMLHttpRequest();

    if(method === 'GET'){
        let param = [];

        for (let key in data){
            if(data.hasOwnProperty(key)){
                param.push(key + '=' + data[key]);
            }
        }

        url += '?' + param.join('&');
    }

    XHR.open(method, BASE_URL + url, true);

    XHR.send(data);

    XHR.onreadystatechange = function(){

        if(XHR.readyState === 4 && XHR.status === 200){
            success(JSON.parse(XHR.responseText));
        }else {

        }
    }
}

window.onload = function(){

    getBingImages();
};