$(document).ready(function() {
    const sortable = $('#sortable').sortable({handle: '.handle'});
    sortable.on('sortupdate', function (event, ui) {
        //const id = ui.item.index();
        const targetItemsClass = ui.item.data('item-set');
        // Get all elements with class from data-item-set
        $(targetItemsClass).each((index, element) => {
            const item = $(element)
            item.find('input').each(function () {
                const inputId = $(this).attr('id');
                const name = $(this).attr("name")
                $(this).attr('id', inputId.replace(/\d/, index))
                $(this).attr('name', name.replace(/\d/, index))
            });
            item.find('label').each(function () {
                const forId = $(this).attr('for');
                $(this).attr('for', forId.replace(/\d/, index));
            });

            // Update delete buttons so the first row is the only row without one.
            const deleteTarget = item.data('item-id').replace(/\d/, index);
            const deleteExists = item.find('.delete_item').length;
            if (index === 0 && deleteExists) {
                item.find('.delete_item').parent().addClass('hidden').removeClass("inline my-2");
            } else {
                item.find('.delete_item').parent().addClass('inline my-2').removeClass("hidden");
            }
            // Change data-item-id index and matching delete button attribute.
            item.attr('data-item-id', deleteTarget);
            item.find('.delete_item').attr('data-item-target', deleteTarget);

        });
    });
});