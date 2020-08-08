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
    }).fail((jqXHR, textStatus, errorThrown) => {
        console.error(jqXHR);
        console.error(textStatus);
        console.error(errorThrown)
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
/**
 * Verifies if a form is updated and triggers warning message display before redirection.
 * @param formSelector
 */
const handleCommonBackButtonEvent = (formSelector) => {
    const form = $(formSelector);
    if(form.length === 0) {
        return;
    }
    const dialog = new mdc.dialog.MDCDialog(document.querySelector('#go-back-dialog'));
    if(!dialog) {
        return;
    }
    const backBtn = $("#back-btn");
    if(backBtn.length === 0) {
        return;
    }

    // Watch for the form changes
    let isFormUpdated = false;
    form.find(":input").on('change', () => isFormUpdated = true);
    // Check if form is updated.
    const canGoBack = () => {
        return !isFormUpdated;
    }
    const dialogChoiceListener = (event) => {
        if(event.detail.action === 'accept') {
            window.location.assign( backBtn.prop("href"));
        }
    };

    dialog.listen('MDCDialog:closing', dialogChoiceListener );

    backBtn.on('click', function (event) {
        if(!canGoBack()) {
            event.preventDefault();
            dialog.open();
        }
    });
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
