document.getElementById("login").addEventListener("click", loginButton);

keyEnter("username", "login");
keyEnter("password", "login");

function loginButton() {
    const username = document.getElementById("username");
    const password = document.getElementById("password");

    if (isEmpty(username) || isEmpty(password)) {
        alertModal("info", "Username and password must not be empty!", "1");
        return;
    }

    const inputData = {
        username: username.value,
        password: password.value
    };

    fetch("/login", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"  
        },
        body: JSON.stringify(inputData)
    })
    .then(res => res.text())
    .then(data => {

        var arr = data.split(',');
        const cookie_time = 20; //20mins

        if (arr[2] == "true" && arr[3] == "master") { //FOR CASHIER ONLY
            setCookie("id", arr[0], cookie_time);
            setCookie("uname", arr[1], cookie_time);
            setCookie("ustatus", arr[2], cookie_time);
            setCookie("acc_type", arr[3], cookie_time);
            setCookie("imageLink", arr[4], cookie_time);
            window.location.assign("profile.html");
        }
        else if (arr[2] == "true" && arr[3] == "master2") {
            setCookie("id", arr[0], cookie_time);
            setCookie("uname", arr[1], cookie_time);
            setCookie("ustatus", arr[2], cookie_time);
            setCookie("acc_type", arr[3], cookie_time);
            setCookie("imageLink", arr[4], cookie_time);
            window.location.assign("../master/master.html");
        }
        else {
            alertModal("failed",data, "1");username.value="";password.value="";
        }
        
    })
    .catch(err => {alertModal("failed",err, "1");});
}

updateClock(); setInterval(updateClock, 1000);