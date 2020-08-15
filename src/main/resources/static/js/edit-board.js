window.addEventListener('load', () => {
    handleCommonBackButtonEvent("#form-board-edit");
    // Dialog for removing participants from the list
    const dialog = new mdc.dialog.MDCDialog(document.querySelector('#delete-dialog'));
    const mode = $("#form-board-edit").data("mode");
    const personInput = $("#person");
    const participantSelect = $("#participant");
    const participantResetBtn = $("#participant-reset-button");
    const participants = $("#participants");
    /**
     * Appends a line with a participant into participants list
     * @param name  Participant's name
     */
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
     * Callback function which listens to user choice during participant removal.
     * If user confirms his choice the participant is removed from the list.
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
     * Sends AJAX request to the server with participant data to remove from the board.
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
    /**
     * Event handler attached to a "remove participant" button.
     * Triggers participant's removal.
     */
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
    // Submit board form with a click on a menu button
    $("#navbarBtnSave").on("click", () => {
        $("#btn-form-board-submit").trigger("click");
    });
    /**
     * Handles participants form submission.
     * Adds new participant to the board.
     */
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