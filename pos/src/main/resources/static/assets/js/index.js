//Screen Phone Only
var currentOrder = document.getElementById('currentOrder');
var closedCustomAmountModal = document.getElementById('closedCustomAmountModal');

closedCustomAmountModal.addEventListener("click", function() {
    const display = window.getComputedStyle(currentOrder).display;
    if (display === "none") { OpeningModalCurrentOrder(); }
});

var closedEditProductModal = document.getElementById('closedEditProductModal');

closedEditProductModal.addEventListener("click", function() {
    const display = window.getComputedStyle(currentOrder).display;
    if (display === "none") { OpeningModalCurrentOrder(); }
});

function OpeningModalCurrentOrder(){
    const modalCurrentOrder = new bootstrap.Modal(document.getElementById('modalCurrentOrder'));
    modalCurrentOrder.show();
}

 function finishOrder() {
    var finishOrder = document.getElementById('finishOrder');
    finishOrder.classList.remove('d-none');
     
     var menuOrder = document.getElementById('menuOrder');
    menuOrder.classList.add('d-none');
     finishOrderModal();
}

function closedFinishOrder(){
    var finishOrder = document.getElementById('finishOrder');
    finishOrder.classList.add('d-none');
     
     var menuOrder = document.getElementById('menuOrder');
    menuOrder.classList.remove('d-none');
    finishOrderModal();
}

function menuOrderModal(){
    var menuOrder = document.getElementById('menuOrder');
    var menuOrderModal = document.getElementById('menuOrderModal');

    menuOrderModal.innerHTML = menuOrder.innerHTML;
}

function finishOrderModal(){ //COPY TO MODAL
    var finishOrder = document.getElementById('finishOrder');
    var menuOrderModal = document.getElementById('menuOrderModal');

    menuOrderModal.innerHTML = finishOrder.innerHTML;
}