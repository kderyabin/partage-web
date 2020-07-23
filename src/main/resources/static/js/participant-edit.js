window.addEventListener('load', () => {
    // Submit form
    $("#navbarBtnSave").on("click", () => {
        $("#btn-form-submit").trigger("click");
    });
    // Watch for the form changes
    let isFormUpdated = false;
    $("#form-participant-edit :input").on('change', () => isFormUpdated = true);
    // Check if form is updated.
    const canGoBack = () => {
        return !isFormUpdated;
    }
    const dialogChoiceListener = (event) => {
        if(event.detail.action === 'accept') {
            window.location.assign( $("#back-btn").prop("href"));
        }
    };
    const dialog = new mdc.dialog.MDCDialog(document.querySelector('.mdc-dialog'));
    dialog.listen('MDCDialog:closing', dialogChoiceListener );

    $("#back-btn").on('click', function (event) {
        if(!canGoBack()) {
            event.preventDefault();
            dialog.open();
        }
    });
});