/**
 * AJAX request initializer
 * @param url   URL where the request must be sent
 * @param dto   DTO to send
 * @returns     JQuery ajax instance
 */
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
        // Silently log errors
        console.error(jqXHR);
        console.error(textStatus);
        console.error(errorThrown)
    });
}
// Notification object to display messages in a snackbar.
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
 * @param formSelector  Form selector, ex.: "#edit-board"
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
// Common events initializer
window.addEventListener( "load",  (event) => {
    // Initialize Settings drop down menu if it's present on the page
    const settingsBtn = document.querySelector('#settings-btn');
    if(settingsBtn != null) {
        const menu = new mdc.menu.MDCMenu(document.querySelector('.mdc-menu'));
        settingsBtn.addEventListener('click', () => {
            menu.open = true;
        });
    }
    // Initialize snackbar notification
    // Checks if notifications object is declared on the page and if it's the a case display the notification message
    const snackbarElem = document.querySelector('.mdc-snackbar');
    if(snackbarElem) {
        Notification.init(new mdc.snackbar.MDCSnackbar(snackbarElem));
        if( typeof notifications !== 'undefined' && notifications.display) {
            Notification.show(notifications.display);
        }
    }
});
