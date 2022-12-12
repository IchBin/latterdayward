$(document).ready(function() {

    // Passing event modal data to hidden inputs before meeting save.
    $('#event_submit').click(function() {
        let inputs = '', days = [], checkIndex = 0, title = '', descr = '';
        const lastRow = $('#schedule_events table tbody tr').last().attr('id');
        const update = $(this).attr("data-update");
        const index = (update != null ? update : (lastRow == null ? 0 : parseInt(lastRow) + 1));
        $('#event_form input').each(function() {
            const input = $(this);
            const type = input.attr('type');
            const name = input.attr('name');
            const value = input.val();
            const isChecked = input.is(':checked');
            if (type === "checkbox") {
                if (isChecked && name.includes('repeat')) {
                    inputs += '<input type="hidden" name="events['+ index +'].repeats['+ checkIndex++ +']" value="'+ value +'" class="hidden_checkbox" id="'+ name +'" />';
                    days.push(value);
                } else if (isChecked) {
                    inputs += '<input type="hidden" name="events['+ index +'].'+ name +'" value="'+ value +'" class="hidden_checkbox" id="'+ name +'" />';
                }
            } else {
                if (name === 'title') {
                    title = value;
                } else if (name === 'description') {
                    descr = value;
                }
                inputs += '<input type="hidden" name="events['+index+'].'+ name +'" value="'+ value +'" id="'+ name +'" />';
            }
        });
        const deleteLink = '<a href="#" class="delete_event" data-delete-id="'+ index +'"><i class="fa-solid fa-trash-can text-danger fs-4"></i></a>';
        const editLink = '<a class="edit_event mx-2" href="#" data-edit-id="'+ index +'"><i class="fa-solid fa-pencil text-warning fs-4"></i></a>';
        if (update) {
            $('#schedule_events div.hidden_event' + update).remove();
            $('#schedule_events table tbody tr.event' + update).remove();
        }
        $('#schedule_events').append($('<div>').addClass('hidden_event'+ index).append(inputs));
        $('#schedule_events table tbody')
            .append($('<tr>').addClass('event'+index).attr('id', index)
                .append($('<td>').append(title))
                .append($('<td>').append(descr))
                .append($('<td>').append(days.join(', ')))
                .append($('<td>').addClass("text-end").append(deleteLink).append(editLink)));
        $('#eventModal').modal('hide');
        $('#event_submit').removeAttr('data-update');
        $('#event_form')[0].reset();
    });

    // Delete an event
    $(document).on('click', '.delete_event', function() {
        const id = $(this).attr('data-delete-id');
        $('#schedule_events table tbody tr.event' + id).remove();
        $('#schedule_events div.hidden_event' + id).remove();
    });

    // Population the modal with meeting data
    $(document).on('click', '.edit_event', function() {
        const id = $(this).attr('data-edit-id');
        $('.hidden_event'+ id + ' input').each(function() {
            const input = $(this);
            const formId = input.attr('id');
            const value = input.val();
            const formInput = $('#event_form input[name="'+ formId +'"]');
            if (formId === formInput.attr('name')) {
                if (input.hasClass('hidden_checkbox')) {
                    $('#event_form input[value="'+value+'"]').prop('checked', true)
                } else {
                    formInput.val(value);
                }
            }
        });
        $('#event_submit').attr('data-update', id);
        $('#eventModal').modal('show');
    });

    $('.close_modal').click(function() {
        $('#event_submit').removeAttr('data-update');
        $('#event_form')[0].reset();
    });
});