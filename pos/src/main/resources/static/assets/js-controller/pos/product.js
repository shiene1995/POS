product_show(); //initialation

function product_show() {

    const product_row = document.getElementById("product_row");
    const product_category = document.getElementById("product_category");

    const inputData = {
        userID: getCookie('id')
    };

    fetch("/product_show", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"  
        },
        body: JSON.stringify(inputData)
    })
    .then(res => res.text())
    .then(data => {
        var arr = data.split('+');
        
        if (arr[0] == "") {product_row.innerHTML = noRecord();product_category.innerHTML=arr[1];}
        else{product_row.innerHTML = arr[0];product_category.innerHTML=arr[1];}
    })
    .catch(err => {alertModal("failed",err, "1");});
}

function category_click(data) {

    document.getElementById("searchInput").value = "";

    const product_row = document.getElementById("product_row");
    const product_category = document.getElementById("product_category");

    const inputData = {
        categoryName: data.toLowerCase().trim()
    };

    fetch("/categorySearch", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"  
        },
        body: JSON.stringify(inputData)
    })
    .then(res => res.text())
    .then(data => {

        var arr = data.split('+');
        
        if (arr[0] == "") {product_row.innerHTML = noRecord();product_category.innerHTML=arr[1];}
        else{product_row.innerHTML = arr[0];product_category.innerHTML=arr[1];}
    })
    .catch(err => {alertModal("failed",err, "1");});
}

document.getElementById("searchButton").addEventListener("click", searchProduct); //SEARCH PRODUCT
keyEnter("searchInput", "searchButton");

function searchProduct() {

    const searchInput = document.getElementById("searchInput");
    const product_row = document.getElementById("product_row");
    const product_category = document.getElementById("product_category");

    const inputData = {
        searchProduct: searchInput.value
    };

    fetch("/searchProduct", {
        method: "POST",
        headers: {
            "Content-Type": "application/json"  
        },
        body: JSON.stringify(inputData)
    })
    .then(res => res.text())
    .then(data => {

        var arr = data.split('+');
        
        if (arr[0] == "") {product_row.innerHTML = noRecord();product_category.innerHTML=arr[1];}
        else{product_row.innerHTML = arr[0];product_category.innerHTML=arr[1];}
    })
    .catch(err => {alertModal("failed",err, "1");});
    
}

function noRecord() {
    return '<div class="col text-center align-self-center mb-1" style="margin: 0 100px;"><svg xmlns="http://www.w3.org/2000/svg" viewBox="0 -32 576 576" width="1em" height="1em" fill="currentColor" class="fs-1 text-warning"><path d="M0 64C0 28.65 28.65 0 64 0H224V128C224 145.7 238.3 160 256 160H384V198.6C310.1 219.5 256 287.4 256 368C256 427.1 285.1 479.3 329.7 511.3C326.6 511.7 323.3 512 320 512H64C28.65 512 0 483.3 0 448V64zM256 128V0L384 128H256zM288 368C288 288.5 352.5 224 432 224C511.5 224 576 288.5 576 368C576 447.5 511.5 512 432 512C352.5 512 288 447.5 288 368zM432 464C445.3 464 456 453.3 456 440C456 426.7 445.3 416 432 416C418.7 416 408 426.7 408 440C408 453.3 418.7 464 432 464zM415.1 288V368C415.1 376.8 423.2 384 431.1 384C440.8 384 447.1 376.8 447.1 368V288C447.1 279.2 440.8 272 431.1 272C423.2 272 415.1 279.2 415.1 288z"></path></svg><h1 class="text-warning">No Record Found</h1></div>';
}

document.getElementById("productVariantModalButton").addEventListener("click", () => {

    const selectedRadio = document.querySelector(`input[name="Option"]:checked`);
    addOrder(selectedRadio.value, "", "product");
});

function addOrder(product_id, product_name, type) {

    const variant_name = document.getElementById("variant_name");
    const variant_div = document.getElementById("variant_div");

    //PRODUCT WITH VARIANT
    if(type == "variant")
    {
        const inputData = {
            product_id: product_id
        };

        fetch("/requestVariantProduct", {
            method: "POST",
            headers: {
                "Content-Type": "application/json"  
            },
            body: JSON.stringify(inputData)
        })
        .then(res => res.text())
        .then(data => {

            variant_div.innerHTML = data;
            variant_name.innerText = product_name;
            $("#productVariantModal").modal('show');
        })
        .catch(err => {alertModal("failed",err, "1");});
        
        return;
    }

    //MEAL PRODUCT / PACKAGE PRODUCT
    if (type == "package") {
        return;
    }

    //ADDING PRODUCT TO CURRENT ORDER TAB
    if (type == "product") {
        const inputData = {
            product_id: product_id
        };

        fetch("/addOrder", {
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
}