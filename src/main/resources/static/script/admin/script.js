$(document).ready(function() {

    $('.edit-user').click(function () {
        //clone dialog and remove ids to ensure uniqueness
        const $modal = $('#userModal').clone().removeAttr('id');
        const $btn = $(this);
        $modal.find('input[name="id"]').val($btn.attr('data-user-id'));
        $modal.find('input[name="username"]').val($btn.attr('data-username'));
        $modal.find('input[name="wardId"]').val($btn.attr('data-ward-id'));
        $modal.find('input[name="apiActive"]').prop('checked', $btn.attr('data-api-active') === 'true');
        $modal.find('input[name="active"]').prop('checked', $btn.attr('data-active') === 'true');
        $modal.find('input[name="admin"]').prop('checked', $btn.attr('data-admin')=== 'true');
        $modal.find('input[name="created"]').val($btn.attr('data-created'));
        $modal.modal('show');
    });

    $('#userModal').on('hidden.bs.modal', function (e) {
        e.modal('dispose')
    })

    $('#ward-select').change(function() {
        this.form.submit()
    })
});