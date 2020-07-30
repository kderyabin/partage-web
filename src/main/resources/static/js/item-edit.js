window.addEventListener('load', () => {
    // Submit form
    const form = $("#form-item-edit");
    $("#navbarBtnSave").on("click", () => {
        $("#btn-form-item-submit").trigger("click");
    });

    // Watch for the form changes
    let isFormUpdated = false;
    $("#form-item-edit :input").on('change', () => isFormUpdated = true);
    // Check if form is updated.
    const canGoBack = () => {
        return !isFormUpdated;
    }
    const btnGoBack = $("#back-btn");
    // Init "go back" button event
    const dialogChoiceListener = (event) => {
        if(event.detail.action === 'accept') {
            window.location.assign( btnGoBack.prop("href"));
        }
    };
    const dialogGoBack = new mdc.dialog.MDCDialog(document.querySelector('#go-back-dialog'));
    dialogGoBack.listen('MDCDialog:closing', dialogChoiceListener );
    btnGoBack.on('click', function (event) {
        if(!canGoBack()) {
            event.preventDefault();
            dialogGoBack.open();
        }
    });
    // Init delete button events
    const deleteItemAction = () => {
        form.prop("action", "item/remove");
        form.submit();
    }
    const dialogDeleteChoiceListener = (event) => {
        if(event.detail.action === 'accept') {
            deleteItemAction();
        }
    };
    const dialogDelete = new mdc.dialog.MDCDialog(document.querySelector('#delete-dialog'));
    dialogDelete.listen('MDCDialog:closing', dialogDeleteChoiceListener );
    $("#navbarBtnDelete").on('click', () => dialogDelete.open());
});