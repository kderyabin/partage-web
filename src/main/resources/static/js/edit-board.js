window.onload = () => {

    const dialog = new mdc.dialog.MDCDialog(document.querySelector('.mdc-dialog'));

    $("#navbarBtnSave").on("click", () => {
        $("#btn-form-board-submit").click();
    });
    const personInput = $("#person");
    const participantInput = $("#participant");
    const addParticipant = ( name ) => {
        let item = $("#participant-item").html();
        item = item.replaceAll("{{name}}", name);
        $("#participants").append(item);
    }

    $("#participant-add-button").on("click", (event) => {
        let name = personInput.val().trim();
        if (name.length > 0) {
            let dto = { name: name, id: null };
            $.ajax({
                type: "POST",
                contentType: "application/json",
                url: "add-participant",
                data: JSON.stringify(dto),
                dataType: 'json',
                xhrFields: {
                    withCredentials: true
                }

            }).done( response => {
                console.log(response);
                if(response.error) {
                    $("#my-dialog-content").html(response.errMsg);
                    dialog.open();
                } else {
                    addParticipant(name);
                }
                // Reset participants choice
                personInput.val("");
                if(participantInput.length > 0) {
                    participantInput[0].selectedIndex = 0;
                }
            }).fail( (jqXHR, textStatus, errorThrown ) => {
                console.error(jqXHR);
                console.error(textStatus);
                console.error(errorThrown);
            });
        }
    });
}