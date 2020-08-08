window.addEventListener('load', () => {
    // Submit form
    const form = $("#form-item-edit");
    $("#navbarBtnSave").on("click", () => {
        $("#btn-form-item-submit").trigger("click");
    });
	handleCommonBackButtonEvent("#form-item-edit");

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