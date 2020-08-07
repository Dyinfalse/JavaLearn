const BASE_URL = '/api';
/**
 * 代码高亮
 */
hljs && hljs.initHighlightingOnLoad();

window.onload = function () {
    let select = document.getElementById("select");
    let link = document.getElementById("css-link");
    /**
     * 获取css文件选项
     */
    request({
        url: "/getCssFileList",
        success(res) {
            res.list.map(filename => {
                let option = document.createElement("option");
                option.innerText = filename;
                if(filename === "androidstudio.css") {
                    option.selected = true;
                }
                select.appendChild(option);
            })
        }
    });

    select.onchange = function () {
        link.href = "/css/markdown/" + this.value;
    }

};

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