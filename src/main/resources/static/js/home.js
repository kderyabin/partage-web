window.onload = () => {
    // init delete confirmation dialog
    const dialogChoiceListener = (event) => {
        console.log(event.detail.action);
        if(event.detail.action === 'accept') {
            console.log(boardToDelete);
            console.log(boardToDelete.dataset.deleteBtn);
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
    const menu = new mdc.menu.MDCMenu(document.querySelector('.mdc-menu'));
    document.querySelector('#settings-btn').addEventListener('click', () => {
        menu.open = true;
    });
}