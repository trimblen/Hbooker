function SetFilter(inputField, TableSearch){
    $(document).ready(function(){
        //alert('!!!');
        $("#"+inputField).on("keyup", function() {
            var value = $(this).val().toLowerCase();
            $("#"+TableSearch + " tr").filter(function() {
                $(this).toggle($(this).text().toLowerCase().indexOf(value) > -1)
            });
        });
    });

    return true;
}