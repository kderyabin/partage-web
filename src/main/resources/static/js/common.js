const JsonRequest = (url, dto) => {
    return $.ajax({
        type: "POST",
        contentType: "application/json",
        url: url,
        data: JSON.stringify(dto),
        dataType: 'json',
        xhrFields: {
            withCredentials: true
        }
    });
}
const Notification = {
    snackbar : null,
    init: function(snackbar) {
        this.snackbar = snackbar;
    },
    show: function (msg) {
        this.snackbar.labelText = msg;
        this.snackbar.open();
    }
}

window.addEventListener( "load",  (event) => {
    // Settings drop down menu
    const menu = new mdc.menu.MDCMenu(document.querySelector('.mdc-menu'));
    if (menu) {
        document.querySelector('#settings-btn').addEventListener('click', () => {
            menu.open = true;
        });
    }

    Notification.init(new mdc.snackbar.MDCSnackbar(document.querySelector('.mdc-snackbar')));
    if( typeof notifications !== 'undefined' && notifications.display) {
        Notification.show(notifications.display);
    }
});
