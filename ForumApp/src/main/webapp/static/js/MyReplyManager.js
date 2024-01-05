$(function () {

    var myDiscussionController = new MyReplyController($('#repliesContainer'));
    myDiscussionController.loadData();

    function MyReplyController(_container){
        var self = this;
        this.container = _container;

        this.loadData = function() {
            this.container.html("");
            $.get('../api/post/my', (json) => {
                if(json.length === 0){
                    self.createEmptyAlert();
                } else {
                    json.forEach(d => {
                        self.createNode(d);
                    });
                }
            }).fail((error) => {
                self.conteiner.append('<div class="alert alert-danger">An error occurred. Please try later</div>');
            });
        }

        this.createEmptyAlert = function (){
            var alert = $('<div/>');
            alert.addClass('alert alert-primary').text("No discussions available");
            this.container.append(alert);
        }

        this.createNode = function(p){
            var image = "<img src=\"../static/assets/placeholder_user.png\" class=\"card-profile\">";
            var username = $('<strong/>').addClass('text-primary').text(p.creator.name+' '+p.creator.surname);
            var badge = $('<div/>');
            if(p.creator.isPolicyMaker){
                badge.addClass('badge bg-success').text("Policy Maker");
            } else {
                badge.addClass('badge bg-warning').text("User");
            }
            var area = $('<span/>').addClass('text-secondary small').text('District of '+p.creator.areaOfResidence);
            var userInfo = $('<div/>').addClass('col-3 d-flex').append(
                $('<div/>').addClass('vstack gap-1 mx-auto align-items-center')
                    .append(image)
                    .append(username)
                    .append(badge)
                    .append(area)
            );
            var postContent = $('<div/>').addClass('col-9');
            postContent.append($('<div/>').addClass('card-text').html(p.text));
            var postRow = $('<div/>').addClass('row').append(userInfo).append(postContent);
            var container = $('<div/>').addClass("col-12 card-container").append($('<a/>').addClass('link-card').attr('href', '../discussion/'+p.discussionId).append(
                $('<div/>').addClass("card border-0 shadow dream-card").append(
                    $('<div/>').addClass("card-body").append(postRow)
                ))
            );

            this.container.append(container);
        }
    }

});