window.addEventListener('load', () => {
    // init delete confirmation dialog
    const dialogChoiceListener = (event) => {
        console.log(event.detail.action);
        if(event.detail.action === 'accept') {
            const boardId = boardToDelete.dataset.deleteBtn;
            const dto = { id : boardId};
            JsonRequest("board/remove-board", dto)
                .done(response => {
                    const msg = response.error ? response.errMsg : response.output;
                    Notification.show(msg)
                    if(!response.error){
                        removeLine(boardToDelete);
                    }
                });
        }
        boardToDelete = null;
    };
    const dialog = new mdc.dialog.MDCDialog(document.querySelector('.mdc-dialog'));
    dialog.listen('MDCDialog:closing', dialogChoiceListener );

    document.querySelectorAll("[data-link-href]").forEach(element => {
        element.onclick = (event) => {
            event.preventDefault();
            event.stopPropagation();
            window.location.assign(element.dataset.linkHref);
        };
    });
    let boardToDelete = null;
    document.querySelectorAll("[data-delete-btn]").forEach(element => {
        element.onclick = (event) => {
            event.preventDefault();
            event.stopPropagation();
            boardToDelete = element;
            dialog.open();
        };
    });

    const removeLine =  (btn) => {
        const element = $(btn);
        element.closest(".list-item").remove();
    }
});