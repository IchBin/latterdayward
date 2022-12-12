$(document).ready(function() {
    // tooltips
    const tooltipTriggerList = document.querySelectorAll('[data-bs-toggle="tooltip"]');
    const tooltipList = [...tooltipTriggerList].map(tooltipTriggerEl => new bootstrap.Tooltip(tooltipTriggerEl));

    // Toggle visibility of token
    $('#show_hide_token a').on('click', function(event) {
        event.preventDefault();
        let showHideTokenI = $('#show_hide_token i')
        let showHideTokenInput = $('#show_hide_token input')
        if(showHideTokenInput.attr('type') === 'text'){
            showHideTokenInput.attr('type', 'password');
            showHideTokenI.addClass( 'fa-eye-slash' );
            showHideTokenI.removeClass( 'fa-eye' );
        }else if(showHideTokenInput.attr('type') === 'password'){
            showHideTokenInput.attr('type', 'text');
            showHideTokenI.removeClass( 'fa-eye-slash' );
            showHideTokenI.addClass( "fa-eye" );
        }
    });

    $('#token_delete, .delete_check').click(function() {
        return confirm("Are you sure you want to remove this?")
    })

    // Check ward path is available and valid
    let wardPath = $("#ward-path")
    wardPath.focusout(function() {
        wardPath.tooltip("hide");
    });
    wardPath.keyup(function(){
        const pathName = $(this).val().trim();
        let div = $("#ward-exists");
        div.remove();
        if(pathName !== "") {
            // Check pattern first
            if (!isValidWardName(pathName)) {
                wardPath.tooltip("dispose");
                wardPath.tooltip({title: "Please match the suggested format.", trigger: "manual"}).tooltip("show");
                $("#ward-save").prop("disabled", true);
                return;
            } else { wardPath.tooltip("dispose"); }
            $.ajax({
                url: "/user/ward/names",
                type: "get",
                data: { path: pathName },
                success: function(response) {
                    if (response === true) {
                        wardPath.after('<div id="ward-exists" class="btn btn-danger btn-sm mt-2">Ward name not available</div>');
                        $("#ward-save").prop("disabled", true);
                    } else {
                        wardPath.after('<div id="ward-exists" class="btn btn-success btn-sm mt-2">Ward name available!</div>');
                        $("#ward-save").prop("disabled", false);
                    }
                }
            });
        } else { div.remove(); }

    });

    function isValidWardName(wardName) {
        const regex = /(^[a-z0-9-]{5,}$)/;
        return wardName.match(regex);
    }

    // copy url text to clipboard.
    $('.copy-text').click(function () {
        const text = $(this).data("url");
        const id = $(this).data("id");
        const alert = $('#alert' + id);
        copyToClipBoard(text, alert);
    });
    $('#image-src').on('change', function() {
        copyToClipBoard(this.value, $('.copy-url'));
    });

    function copyToClipBoard(text, msgAlert) {
        navigator.clipboard.writeText(text).then(function () {
            msgAlert.show().delay(4000).slideUp(200, function() {
                $(this).hide();
            });
        }, function () {
            console.log('Failure to copy. Check permissions for clipboard')
        });
    }

    // Delete image or link row from datacard
    $(document).on('click', '.delete_item', function() {
        if (confirm("Are you sure you want to remove this?")) {
            const id = $(this).attr("data-item-target");
            $('div[data-item-id="'+id+'"]').remove()
        }
    });

    // Toggle active state for data card
    $('.toggle-active').click(function() {
        const id = $(this).data('card-id');
        const active = $(this).is(':checked');
        console.log("data id is " + id + " and active is " + active);
        $.ajax({
            url: '/user/datacard/active',
            type: 'GET',
            data: {
                'id': id,
                'active': active
            },
            dataType: 'json',
            success: function (data) {
                if (data === false) {
                    console.log("There was a problem toggling the active state of the card.");
                    $(this).toggle()
                }
            }
        });
    });

});