window.addEventListener('load', () => {
    // Submit form
    $("#navbarBtnSave").on("click", () => {
        $("#btn-form-submit").trigger("click");
    });
    handleCommonBackButtonEvent("#form-settings");
});