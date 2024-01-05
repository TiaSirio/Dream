$(function (){
    //At the moment is not required
    var profileController = new ProfileController($('#userName') );

    function ProfileController(_title){
        var self = this;
        this.title = _title;
    }

});