const cookie_time = 20;

setInterval(function() {if (getCookie('ustatus') != 'true') {window.location.href='/';}}, 60000); 

check_status(); //CHECK STATUS

function check_status(){
 
    if (getCookie('ustatus') === "true") {
        setCookie("id", getCookie('id'), cookie_time);
        setCookie("uname", getCookie('uname'), cookie_time);
        setCookie("ustatus", getCookie('ustatus'), cookie_time);
        setCookie("acc_type", getCookie('acc_type'), cookie_time);
        setCookie("imageLink", getCookie('imageLink'), cookie_time);

        if (window.location.pathname === "/index.html" || window.location.pathname === "/") {
            window.location.href='/profile.html';
        }
    }
    else
    {
        if (window.location.pathname != "/") {
            window.location.href='/';
        }
    }

}

//=========================================================================================== ALL FUNCTION BELOW

function getCookie(name) {
    const decodedCookies = decodeURIComponent(document.cookie);
    const cookiesArray = decodedCookies.split(';');
    for (let cookie of cookiesArray) {
        cookie = cookie.trim(); // Remove whitespace
        if (cookie.startsWith(name + '=')) {
            return cookie.substring(name.length + 1);
        }
    }
    return null;
}

function setCookie(name, value, minutes) {
    let expires = "";
    if (minutes) {
        const date = new Date();
        date.setTime(date.getTime() + (minutes * 60 * 1000)); // minutes to milliseconds
        expires = "; expires=" + date.toUTCString();
    }
    document.cookie = name + "=" + encodeURIComponent(value || "") + expires + "; path=/";
}

function keyEnter(ID, button) {
    document.getElementById(ID).addEventListener("keypress", function(event) {
    if (event.key === "Enter") {event.preventDefault();document.getElementById(button).click();}});  
}

function isEmpty(data){if (data.value.trim() === "") {return true;}}

function alertModal(type, message, alertNumber){

    var alertModal = document.getElementById('alertModal'+alertNumber);

    alertModal.classList.remove('alert-success', 'alert-danger', 'alert-warning', 'alert-info');

    document.getElementById('alertModalMessage'+alertNumber).innerText = message;
    alertModal.classList.remove('d-none');

    switch (type.toLowerCase()) {
    case "success":
        alertModal.classList.add('alert-success');
        break;
    case "failed":
        alertModal.classList.add('alert-danger');
        break;
    case "warning":
        alertModal.classList.add('alert-warning');
        break;
    case "info":
        alertModal.classList.add('alert-info');
        break;
    }
}

function updateClock() {
    const now = new Date();
    const timeString = now.toLocaleTimeString('en-US', {
        hour: '2-digit',
        minute: '2-digit',
        //second: '2-digit',
        hour12: true // Set to false for 24-hour format
    });
    document.getElementById('liveClock').innerText = timeString;
}

function delCookie() { //LOGOUT
    const cookie_time2 = -9999;

    setCookie("id", getCookie('id'), cookie_time2);
        setCookie("uname", getCookie('uname'), cookie_time2);
        setCookie("ustatus", getCookie('ustatus'), cookie_time2);
        setCookie("acc_type", getCookie('acc_type'), cookie_time2);
        setCookie("imageLink", getCookie('imageLink'), cookie_time2);

    window.location.href='/';
}

function myIcon(iconName) {

    var dataReturn;
    
    switch (iconName.toLowerCase()) {
    case "coffee":
        dataReturn ="M400 192H32C14.25 192 0 206.3 0 224v192c0 53 43 96 96 96h192c53 0 96-43 96-96h16c61.75 0 112-50.25 112-112S461.8 192 400 192zM400 352H384V256h16C426.5 256 448 277.5 448 304S426.5 352 400 352zM107.9 100.7C120.3 107.1 128 121.4 128 136c0 13.25 10.75 23.89 24 23.89S176 148.1 176 135.7c0-31.34-16.83-60.64-43.91-76.45C119.7 52.03 112 38.63 112 24.28c0-13.25-10.75-24.14-24-24.14S64 11.03 64 24.28C64 55.63 80.83 84.92 107.9 100.7zM219.9 100.7C232.3 107.1 240 121.4 240 136c0 13.25 10.75 23.86 24 23.86S288 148.1 288 135.7c0-31.34-16.83-60.64-43.91-76.45C231.7 52.03 224 38.63 224 24.28c0-13.25-10.75-24.18-24-24.18S176 11.03 176 24.28C176 55.63 192.8 84.92 219.9 100.7z";
        break;
    case "cake":
        dataReturn ="M352 111.1c22.09 0 40-17.88 40-39.97S352 0 352 0s-40 49.91-40 72S329.9 111.1 352 111.1zM224 111.1c22.09 0 40-17.88 40-39.97S224 0 224 0S184 49.91 184 72S201.9 111.1 224 111.1zM383.1 223.1L384 160c0-8.836-7.164-16-16-16h-32C327.2 144 320 151.2 320 160v64h-64V160c0-8.836-7.164-16-16-16h-32C199.2 144 192 151.2 192 160v64H128V160c0-8.836-7.164-16-16-16h-32C71.16 144 64 151.2 64 160v63.97c-35.35 0-64 28.65-64 63.1v68.7c9.814 6.102 21.39 11.33 32 11.33c20.64 0 45.05-19.73 52.7-27.33c6.25-6.219 16.34-6.219 22.59 0C114.1 348.3 139.4 367.1 160 367.1s45.05-19.73 52.7-27.33c6.25-6.219 16.34-6.219 22.59 0C242.1 348.3 267.4 367.1 288 367.1s45.05-19.73 52.7-27.33c6.25-6.219 16.34-6.219 22.59 0C370.1 348.3 395.4 367.1 416 367.1c10.61 0 22.19-5.227 32-11.33V287.1C448 252.6 419.3 223.1 383.1 223.1zM352 373.3c-13.75 10.95-38.03 26.66-64 26.66s-50.25-15.7-64-26.66c-13.75 10.95-38.03 26.66-64 26.66s-50.25-15.7-64-26.66c-13.75 10.95-38.03 26.66-64 26.66c-11.27 0-22.09-3.121-32-7.377v87.38C0 497.7 14.33 512 32 512h384c17.67 0 32-14.33 32-32v-87.38c-9.91 4.256-20.73 7.377-32 7.377C390 399.1 365.8 384.3 352 373.3zM96 111.1c22.09 0 40-17.88 40-39.97S96 0 96 0S56 49.91 56 72S73.91 111.1 96 111.1z";
        break;
    case "drinks":
        dataReturn ="M507.3 72.57l-67.88-67.88c-6.252-6.25-16.38-6.25-22.63 0l-22.63 22.62c-6.25 6.254-6.251 16.38-.0006 22.63l-76.63 76.63c-46.63-19.75-102.4-10.75-140.4 27.25l-158.4 158.4c-25 25-25 65.51 0 90.51l90.51 90.52c25 25 65.51 25 90.51 0l158.4-158.4c38-38 47-93.76 27.25-140.4l76.63-76.63c6.25 6.25 16.5 6.25 22.75 0l22.63-22.63C513.5 88.95 513.5 78.82 507.3 72.57zM179.3 423.2l-90.51-90.51l122-122l90.51 90.52L179.3 423.2z";
        break;
    case "sides":
        dataReturn ="M96 128C96.53 128 97.07 128 97.6 128C105 91.49 137.3 64 176 64C190.1 64 204.1 68.1 216.9 75.25C230.2 49.55 257.1 32 288 32C318.9 32 345.8 49.56 359.1 75.25C371 68.1 385 64 400 64C438.7 64 470.1 91.49 478.4 128C478.9 128 479.5 128 480 128C515.3 128 544 156.7 544 192C544 203.7 540.9 214.6 535.4 224H40.56C35.12 214.6 32 203.7 32 192C32 156.7 60.65 128 96 128H96zM16 283.4C16 268.3 28.28 256 43.43 256H532.6C547.7 256 560 268.3 560 283.4C560 356.3 512.6 418.2 446.9 439.8C447.6 442.4 448 445.2 448 448C448 465.7 433.7 480 416 480H160C142.3 480 128 465.7 128 448C128 445.2 128.4 442.4 129.1 439.8C63.4 418.2 16 356.3 16 283.4H16z";
        break;
    }

    dataReturn = '<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" width="1em" height="1em" fill="currentColor" style="margin-top: -5px;"><path d="' + dataReturn + '"></path></svg>';

    return dataReturn;
}