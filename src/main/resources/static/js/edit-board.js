window.addEventListener('load', () => {
    const dialog = new mdc.dialog.MDCDialog(document.querySelector('.mdc-dialog'));
    const mode = $("#form-board-edit").data("mode");
    const personInput = $("#person");
    const participantSelect = $("#participant");
    const participantResetBtn = $("#participant-reset-button");
    const participants = $("#participants");
    const addParticipant = (name) => {
        let content = $("#participant-item").html();
        content = content.replaceAll("{{name}}", name);
        const element = $(content);
        const btnRemove = element.find("button");
        btnRemove.on("click", removeParticipant);
        participants.append(element);
        participants.removeClass("hidden");
    }
    /**
     * Listen to user choice
      * @param event
     */
    const dialogChoiceListener = (event) => {
        if (event.detail.action === 'accept') {
            doRemoval();
        } else {
            participantToDelete = null;
        }
    };
    dialog.listen('MDCDialog:closing', dialogChoiceListener);
    let participantToDelete = null;
    /**
     * Sends request to the server to remove participant from the board.
     */
    const doRemoval = () => {
        const btn = $(participantToDelete);
        const name = btn.prev()
        let dto = {name: name.text(), id: null};
        JsonRequest("remove-participant", dto)
            .done(response => {
                if (response.error) {
                    Notification.show(response.errMsg);
                } else {
                    btn.closest("li").remove();
                    if (participants.children().length === 0) {
                        participants.addClass("hidden");
                    }
                }
                participantToDelete = null;
            });
    }
    // Event handler to remove user
    const removeParticipant = function () {
        participantToDelete = this;
        if (mode === "create") {
            doRemoval();
            return;
        }
        // Warn that expenses will be removed also.
        // The removal is done through Dialog callback.
        dialog.open();
    }
    // Attach remove event for each participant
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
        if (participantSelect.length > 0 && participantSelect.val() !== "") {
            dto.name = participantSelect.find("option:selected").text();
            dto.id = parseInt(participantSelect.val());
        } else {
            dto.name = personInput.val().trim();
        }
        if (dto.name.length > 0) {
            JsonRequest("add-participant", dto)
                .done(response => {
                    if (response.error) {
                        Notification.show(response.errMsg);
                    } else {
                        addParticipant(dto.name);
                    }
                    // Reset participants choice
                    participantResetBtn.trigger("click");
                });
        }
        return false;
    });
});