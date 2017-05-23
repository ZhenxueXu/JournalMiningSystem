
//<![CDATA[
var prox;
var proy;
var proxc;
var proyc;
function tshow(id) {
    clearInterval(prox);
    clearInterval(proy);
    clearInterval(proxc);
    clearInterval(proyc);
    var o = document.getElementById(id);
    o.style.display = "block";
    o.style.width = "1px";
    o.style.height = "1px";
    prox = setInterval(function () {
        openx(o, 500)
    }, 10);
}
function openx(o, x) {
    var cx = parseInt(o.style.width);
    if (cx < x) {
        o.style.width = (cx + Math.ceil((x - cx) / 5)) + "px";
    } else {
        clearInterval(prox);
        proy = setInterval(function () {
            openy(o, 200)
        }, 10);
    }
}

function openy(o, y) {
    var cy = parseInt(o.style.height);
    if (cy < y) {
        o.style.height = (cy + Math.ceil((y - cy) / 5)) + "px";
    } else {
        clearInterval(proy);
    }
}

function closeed(id) {
    clearInterval(prox);
    clearInterval(proy);
    clearInterval(proxc);
    clearInterval(proyc);
    var o = document.getElementById(id);
    if (o.style.display == "block") {
        proyc = setInterval(function () {
            closey(o)
        }, 10);
    }
}

function closey(o) {
    var cy = parseInt(o.style.height);
    if (cy > 0) {
        o.style.height = (cy - Math.ceil(cy / 5)) + "px";
    } else {
        clearInterval(proyc);
        proxc = setInterval(function () {
            closex(o)
        }, 10);
    }
}

function closex(o) {
    var cx = parseInt(o.style.width);
    if (cx > 0) {
        o.style.width = (cx - Math.ceil(cx / 5)) + "px";
    } else {
        clearInterval(proxc);
        o.style.display = "none";
    }
}

/*-------------------------鼠标拖动---------------------*/
var od = document.getElementById("feedbox");
var dx, dy, mx, my, mouseD;
var odrag;
var isIE = false;
document.onmousedown = function (e) {
    var e = e ? e : event;
    if (e.button == (document.all ? 1 : 0)) {
        mouseD = true;
    }
}

document.onmouseup = function () {
    mouseD = false;
    odrag = "";
    if (isIE) {
        od.releaseCapture();
        od.filters.alpha.opacity = 100;
    } else {
        window.releaseEvents(od.MOUSEMOVE);
        od.style.opacity = 1;
    }
}


//function readyMove(e){
od.onmousedown = function (e) {
    odrag = this;
    var e = e ? e : event;
    if (e.button == (document.all ? 1 : 0)) {
        mx = e.clientX;
        my = e.clientY;
        od.style.left = od.offsetLeft + "px";
        od.style.top = od.offsetTop + "px";
        if (isIE) {
            od.setCapture();
            od.filters.alpha.opacity = 50;
        } else {
            window.captureEvents(Event.MOUSEMOVE);
            od.style.opacity = 0.5;
        }
    }
}

document.onmousemove = function (e) {
    var e = e ? e : event;
    if (mouseD == true && odrag) {
        var mrx = e.clientX - mx;
        var mry = e.clientY - my;
        od.style.left = parseInt(od.style.left) + mrx + "px";
        od.style.top = parseInt(od.style.top) + mry + "px";
        mx = e.clientX;
        my = e.clientY;
    }
}
//]]>
