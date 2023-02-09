$(document).ready(function() {

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

    $('.close').click(function(e) {
        e.preventDefault();
        $(this).parent().hide();
    });

    $('#menu-toggle').click(function() {
        $('#collapsed-menu').toggle()
    });

    $(window).resize(function(e) {
        if ($(window).width() > 767) {
            $('#collapsed-menu').show();
        }
    });

    // Check ward path is available and valid
    let wardPath = $("#ward-path")
    wardPath.focusout(function() {
        wardPath.tooltip("hide");
    });
    wardPath.keyup(function(){
        let successDiv = "<div id=\"ward-exists\" class=\"rounded my-2 w-2/3 border border-blue-600 bg-blue-100 bg-blue-200 p-2\">Ward name available</div>"
        let errorDiv = "<div id=\"ward-exists\" class=\"rounded my-2 w-2/3 border border-red-600 bg-red-100 bg-red-200 p-2\">Ward name is not available</div>"
        const pathName = $(this).val().trim();
        let div = $("#ward-exists");
        div.remove();
        if(pathName !== "") {
            // Check pattern first
            if (!isValidWardName(pathName)) {
                wardPath.after(errorDiv);
                $('#ward-exists').text("Please match the suggested format.")
                $("#ward-save").prop("disabled", true);
                return;
            }
            $.ajax({
                url: "/user/ward/names",
                type: "get",
                data: { path: pathName },
                success: function(response) {
                    if (response === true) {
                        wardPath.after(errorDiv);
                        $("#ward-save").prop("disabled", true);
                    } else {
                        wardPath.after(successDiv);
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