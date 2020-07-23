window.addEventListener('load', () => {
    let participantToDelete = null;
    const removeLine = (btn) => {
        const element = $(btn);
        element.closest(".list-item").remove();
    }
    // init delete confirmation dialog
    const dialogChoiceListener = (event) => {
        if (event.detail.action !== 'accept') {
            participantToDelete = null;
            return;
        }
        const id = participantToDelete.dataset.deleteBtn;
        const dto = {id: id};
        JsonRequest("remove", dto)
            .done(function (response) {
                const msg = response.error ? response.errMsg : response.output;
                Notification.show(msg)
                if (!response.error) {
                    removeLine(participantToDelete);
                }
                participantToDelete = null;
            });

    };

    const dialog = new mdc.dialog.MDCDialog(document.querySelector('.mdc-dialog'));
    dialog.listen('MDCDialog:closing', dialogChoiceListener);

    document.querySelectorAll("[data-delete-btn]").forEach(element => {
        element.onclick = (event) => {
            event.preventDefault();
            event.stopPropagation();
            participantToDelete = element;
            dialog.open();
        };
    });

});