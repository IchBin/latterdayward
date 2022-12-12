$(document).ready(function() {
    // Add another link input set for a DataCard
    $('.duplicate-input').click(function () {
        const target = $(this).data("target");
        const id = $(target).length
        let theClone = $(target).last().clone().appendTo($(this).data("appender"));
        theClone.find("input[type=text]").val("");
        theClone.find("input[type=datetime-local]").val("");
        theClone.find("input").attr("name", function(i, oldVal) {
                return oldVal.replace(/\[(\d+)\]/, function(_,i) {
                    return "[" + id + "]";
                });
            });
        theClone.find("label").attr("for", function(i, oldVal) {
                return oldVal.replace(/\[(\d+)\]/, function(_,i) {
                    return "[" + id + "]";
                });
            });
        theClone.find("input").attr("id", function(i, oldVal) {
                return oldVal.replace(/\[(\d+)\]/, function(_,i) {
                    return "[" + id + "]";
            });
        });
        theClone.find("button").attr("data-item-target", function(i, oldVal) {
            return oldVal.replace(/(\d+)/, function (_, i) {
                return parseInt(i) + 1
            });
        });
        theClone.attr("data-item-id", function(i, oldVal) {
            return oldVal.replace(/(\d+)/, function (_, i) {
                return parseInt(i) + 1
            });
        });
        theClone.find("div.d-none").addClass('col-md-1 d-flex align-items-end').removeClass("d-none");
    });
});