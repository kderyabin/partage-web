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
window.onload = () => {
    // Settings drop down menu
    const menu = new mdc.menu.MDCMenu(document.querySelector('.mdc-menu'));
    if(menu) {
        document.querySelector('#settings-btn').addEventListener('click', () => {
            menu.open = true;
        });
    }
}
