updateClock(); setInterval(updateClock, 1000); //TIME CLOCK

document.getElementById("logout").addEventListener("click", delCookie); //DELETE COOKIE

document.getElementById("changePassButton").addEventListener("click", changePass); //CHANGE PASS
keyEnter("currentPass", "changePassButton");
keyEnter("newPass1", "changePassButton");
keyEnter("newPass2", "changePassButton");

document.getElementById("uname").innerHTML = getCookie('uname');
document.getElementById("acc_type").innerHTML = getCookie('acc_type');
document.getElementById("profileImage").src = "assets/img/profile/" + getCookie('imageLink');

function changePass() {
    
    const currentPass = document.getElementById("currentPass");
    const newPass1 = document.getElementById("newPass1");
    const newPass2 = document.getElementById("newPass2");

    if (isEmpty(currentPass) || isEmpty(newPass1) || isEmpty(newPass2)) {
        alertModal("info", "All input must not be empty!", "1");
        return;
    }

    const inputData = {
        userID: getCookie('id'),
        currentPass: currentPass.value,
        newPass1: newPass1.value,
        newPass2: newPass2.value
    };

    fetch("/changePass", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"  
        },
        body: JSON.stringify(inputData)
    })
    .then(res => res.text())
    .then(data => {

        if(data == "true"){
            alertModal("success", "Password has been changed.", "1");
            currentPass.value = newPass1.value = newPass2.value = "";
        }
        else{alertModal("failed", data, "1");}
        
    })
    .catch(err => {alertModal("failed",err, "2");});
}

//======================================================================SESSION BELOW

document.getElementById("session_button").addEventListener("click", function() {session("false");});
document.getElementById("ConfirmFloatButton").addEventListener("click", function() {session("true");});
keyEnter("startingCashInput", "ConfirmFloatButton");

function session(verifyButtonClick) {

    var startingCashInput = document.getElementById("startingCashInput");

    if (isEmpty(startingCashInput)) {
        startingCashInput.value = 0;
    }

    const inputData = {
        userID: getCookie('id'),
        starting_cash: startingCashInput.value,
        verifyButton: verifyButtonClick
    };

    fetch("/session", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"  
        },
        body: JSON.stringify(inputData)
    })
    .then(res => res.text())
    .then(data => {

        if (data == "true") { 
            $('#startingCashFloatModal').modal('hide');
            document.getElementById("session_button").classList.add('d-none');
            alertModal("success","Session Start Success. You will be redirect to POS in 5s.", "2");
            setInterval(function() {window.location.href = '/pos.html';}, 5000);
        }
        else if(data == "continue"){
            window.location.href = '/pos.html';
        }
        else if (data == "float") { 
            startingCashInput.value = 0;
            $('#startingCashFloatModal').modal('show');
        }
        else
        {
            $('#sessionEndModal').modal('show');
        }
        
    })
    .catch(err => {alertModal("failed",err, "2");});
}

//======================================================================SESSION CHECKER
//IF THE CASHIER HAS EXISTING UNCLOSE SESSION WITHIN THE MAX SESSION DURATION, THE BUTTON LABEL WILL CHANGE INTO BACK TO SESSION.

session_checker(); //initialation

function session_checker() {

    const inputData = {
        userID: getCookie('id')
    };

    fetch("/session_checker", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"  
        },
        body: JSON.stringify(inputData)
    })
    .then(res => res.text())
    .then(data => {

        var arr = data.split('+');

        if (arr[0] == "true") {
            document.getElementById("session_button").innerText = "Session Start";
        }
        else if (arr[0] == "continue") {
            document.getElementById("session_button").innerText = "Continue ( " + arr[1] + " )";
            alertModal("info","Welcome back. Your session is active.", "2");
        }
        else if (arr[0] == "close") {
            document.getElementById("session_button").innerText = " Closing Session ( " + arr[1] + " hours )";
            alertModal("warning", "Your session limit has been reached. Please end this session to start a new one.", "2");
        }
        
    })
    .catch(err => {alertModal("failed",err, "2");});
}