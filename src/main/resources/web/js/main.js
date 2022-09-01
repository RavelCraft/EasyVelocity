var alertbox;

if (navigator.language.startsWith("en")) {
    alertbox = document.getElementById("alertboxfr");
    if (typeof(alertbox) != 'undefined' && alertbox != null) {
        if (getCookie() !== "fr") {
            alertbox.style.display = "block";
        }
    } else {
        deleteCookie();
    }
}

if (navigator.language.startsWith("fr")) {
    alertbox = document.getElementById("alertboxen");
    if (typeof(alertbox) != 'undefined' && alertbox != null) {
        if (getCookie() !== "en") {
            alertbox.style.display = "block";
        }
    } else {
        deleteCookie();
    }
}

function getCookie() {
    var name = "langshow=";
    var ca = document.cookie.split(';');

    for(var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) === ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) === 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}

function setCookie(cvalue) {
    var d = new Date();
    d.setTime(d.getTime() + (365*24*60*60*1000));
    var expires = "expires=" + d.toGMTString();
    document.cookie = "langshow=" + cvalue + ";" + expires + ";path=/";
}

function deleteCookie() {
    document.cookie = "langshow" + "=;expires=1 Jan 2000 00:00:00 UTC;path=/";
}