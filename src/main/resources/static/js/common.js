window.onload = () => {
    // Settings drop down menu
    const menu = new mdc.menu.MDCMenu(document.querySelector('.mdc-menu'));
    if(menu) {
        document.querySelector('#settings-btn').addEventListener('click', () => {
            menu.open = true;
        });
    }
}