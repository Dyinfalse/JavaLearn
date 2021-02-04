window.onload = function () {

    document.getElementById("index").addEventListener('click', function (e) {
        if(e.target.nodeName === 'SPAN') {
            document.getElementById("iframe").src = e.target.id;
        }
        let children = document.getElementById("index").children;
        for (let i = 0; i < children.length; i++) {
            children[i].className = '';
        }
        e.target.className = 'active';
    }, false)
};