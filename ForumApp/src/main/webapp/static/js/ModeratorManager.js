$(function(){

    var moderatorController = new ModeratorController($('#numberPending'));
    moderatorController.loadData();

    function ModeratorController(_number){
        var self = this;
        this.number = _number;

        this.loadData = function() {
            $.get('api/post/pending', (json) => {
                self.number.text(json.length);
            }).fail((error) => {
                self.number.text('#');
            });
        }
    }

});