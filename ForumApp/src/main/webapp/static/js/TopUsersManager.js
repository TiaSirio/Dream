$(function(){

    const topUserController = new TopUserController($('#topUsers'));
    topUserController.loadData();

    function TopUserController(_container){
        var self = this;
        this.container = _container;
        this.basePath = '/';
        this.loadData = function(){
            this.container.html("");
            $.get(this.basePath+'api/user/active', (json) => {
                var c = 0;
                json.forEach(u => {
                    c = c + 1;
                    self.createNode(u, c);
                });
            }).fail((error) => {
                self.container.append('<div class="alert alert-danger">An error occurred. Please try later</div>');
            });
        }

        this.createNode = function(user, c) {
            var image = '<td><img src="'+this.basePath+'static/assets/placeholder_user.png" class="top-user-img"/></td>';
            var username = $('<td/>').text(user.name + " " + user.surname);
            var np = $('<td/>').addClass('text-center text-secondary small').text(c);
            var tableNode = $('<tr/>').append(image).append(username).append(np);
            this.container.append(tableNode);
        }
    }

});