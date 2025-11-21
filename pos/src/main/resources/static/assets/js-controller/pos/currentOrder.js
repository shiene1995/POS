currentOrderShow();
retrieveOrderShow();

function currentOrderShow(){

    const currentOrderRow = document.getElementById("currentOrderRow");
    const tabName = document.getElementById("tabName");
    const transferTabName = document.getElementById("transferTabName");

    const inputData = {
        userID: getCookie('id')
    };

    fetch("/currentOrderShow", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"  
        },
        body: JSON.stringify(inputData)
    })
    .then(res => res.text())
    .then(data => {

        var arr = data.split('+');

        currentOrderRow.innerHTML = arr[0];
        tabName.innerText = arr[1];
        transferTabName.innerText = arr[1];


    })
    .catch(err => {alertModal("failed",err, "1");});
}

function retrieveOrderShow(){

    const retrieveOrderTableBody = document.getElementById("retrieveOrderTableBody");
    const retrieveOrderHeading = document.getElementById("retrieveOrderHeading");
    const transferTabName = document.getElementById("transferTabName");
    
    const inputData = {
        userID: getCookie('id')
    };

    fetch("/retrieveOrderShow", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"  
        },
        body: JSON.stringify(inputData)
    })
    .then(res => res.text())
    .then(data => {

        var arr = data.split('+');

        retrieveOrderTableBody.innerHTML = arr[0];
        retrieveOrderHeading.innerText = arr[1];
        transferTabName.innerText = arr[2];

    })
    .catch(err => {alertModal("failed",err, "1");});
}

function retrieveOrder(data){

    const inputData = {
        tabName: data
    };

    fetch("/retrieveOrder", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"  
        },
        body: JSON.stringify(inputData)
    })
    .then(res => res.text())
    .then(data => {

        currentOrderShow();
        retrieveOrderShow();

    })
    .catch(err => {alertModal("failed",err, "1");});
}

function minusProduct(product_id) {

    const inputData = {
            product_id: product_id
        };

        fetch("/minusProduct", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"  
            },
            body: JSON.stringify(inputData)
        })
        .then(res => res.text())
        .then(data => {

            if(data != "true")
            {
                alertModal("failed","THERE IS SOMETHING ERROR! | " + data, "1");
            }
            currentOrderShow();
        })
        .catch(err => {alertModal("failed",err, "1");});
}

function plusProduct(product_id) {

    const inputData = {
            product_id: product_id
        };

        fetch("/plusProduct", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"  
            },
            body: JSON.stringify(inputData)
        })
        .then(res => res.text())
        .then(data => {

            if(data != "true")
            {
                alertModal("failed","THERE IS SOMETHING ERROR! | " + data, "1");
            }
            currentOrderShow();
        })
        .catch(err => {alertModal("failed",err, "1");});
}

var tab = "";

document.getElementById("tabName").addEventListener("click", () => {
    if (document.getElementById("tabName").innerText == "-- NO NAME --") {
        if (document.getElementById("currentOrderRow").innerHTML != "") {
            $("#tabOrder").modal('show');tab = "create";
        }
    }
});
document.getElementById("holdOrder").addEventListener("click", () => {
    tab = "holdOrder";

    if (document.getElementById("currentOrderRow").innerHTML == "") {return;}

    if (document.getElementById("tabName").innerText == "-- NO NAME --") {$("#tabOrder").modal('show');}
    else{tabOrder();}
});

function tabOrder() {

    const tabName = document.getElementById("tabSelectName").value + " " + document.getElementById("tabSelectNum").value;

    //CREATE TAB NAME
    if (tab == "create") {

        const inputData = {
            tabName: tabName
        };

        fetch("/createTabName", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"  
            },
            body: JSON.stringify(inputData)
        })
        .then(res => res.text())
        .then(data => {

            currentOrderShow();

        })
        .catch(err => {alertModal("failed",err, "1");});
    }

    //HOLD ORDER
    if (tab == "holdOrder") {

        const inputData = {
            tabName: tabName
        };

        fetch("/holdOrder", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"  
            },
            body: JSON.stringify(inputData)
        })
        .then(res => res.text())
        .then(data => {

            currentOrderShow();
            retrieveOrderShow();

        })
        .catch(err => {alertModal("failed",err, "1");});
    }
}


document.getElementById("deleteCurrentOrder").addEventListener("click", deleteCurrentOrder);

function deleteCurrentOrder() {

    const inputData = {
        product_id: 1
    };

    fetch("/deleteCurrentOrder", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"  
        },
        body: JSON.stringify(inputData)
    })
    .then(res => res.text())
    .then(data => {

        currentOrderShow();

    })
    .catch(err => {alertModal("failed",err, "1");});
}