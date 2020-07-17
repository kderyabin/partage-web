window.onload = () => {


    const dialog = new mdc.dialog.MDCDialog(document.querySelector('.mdc-dialog'));
    const mode = $("#form-board-edit").data("mode");
    const personInput = $("#person");
    const participantSelect = $("#participant");
    const participantResetBtn = $("#participant-reset-button");
    const addParticipant = (name) => {
        let content = $("#participant-item").html();
        content = content.replaceAll("{{name}}", name);
        const element = $(content);
        const btnRemove = element.find("button");
        btnRemove.on("click", removeParticipant);
        $("#participants").append(element);
    }
    // Attached to remove button
    const removeParticipant = function () {
        const btn = $(this);
        const name = btn.prev()
        if (mode === 'create') {
            let dto = {name: name.text(), id: null};
            JsonRequest("remove-participant", dto)
                .done(response => {
                    console.log(response);
                    if (response.error) {
                        $("#my-dialog-content").html(response.errMsg);
                        dialog.open();
                    } else {
                        btn.closest("li").remove();
                    }
                }).fail((jqXHR, textStatus, errorThrown) => {
                console.error(jqXHR);
                console.error(textStatus);
                console.error(errorThrown);
            });
        }
    }
    // Attach remove event for each participant
    const participants = $("#participants");
    if (participants.children("li").length > 0) {
        participants.find("button").on("click", removeParticipant);
    }
    // Submit board form
    $("#navbarBtnSave").on("click", () => {
        $("#btn-form-board-submit").trigger("click");
    });
    // Handle participants form submission
    $("#form-participants").on("submit", (event) => {
        event.preventDefault();
        let dto = {name: "", id: null};
        if(participantSelect.val() !== "") {
            dto.name = participantSelect.find("option:selected").text();
            dto.id = parseInt(participantSelect.val());
        } else {
            dto.name = personInput.val().trim();
        }
        if (dto.name.length > 0) {
            JsonRequest("add-participant", dto)
                .done(response => {
                    console.log(response);
                    if (response.error) {
                        $("#my-dialog-content").html(response.errMsg);
                        dialog.open();
                    } else {
                        addParticipant(dto.name);
                    }
                    // Reset participants choice
                    participantResetBtn.trigger("click");
                }).fail((jqXHR, textStatus, errorThrown) => {
                console.error(jqXHR);
                console.error(textStatus);
                console.error(errorThrown);
            });
        }
        return false;
    });
}