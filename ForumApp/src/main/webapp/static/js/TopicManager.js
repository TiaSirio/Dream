$(function(){

    const topicController = new TopicController($('#topicContainer'));
    topicController.loadData();

    //Topic controller declared as class
    function TopicController(_container){
        var self = this;
        this.container = _container;

        this.loadData = function(){
            this.container.html("");
            $.get('api/topic', (json) => {
                json.forEach(t => {
                    self.createNode(t);
                });
            }).fail((error) => {
                self.container.append('<div class="alert alert-danger">An error occurred. Please try later</div>');
            });
        }

        this.createNode = function(topic) {
            var image = '<img src="static/assets/folder_icon.svg" class="svg-card-icon svg-secondary" width="70%" height="auto"/>';
            var imageContainer = $('<div/>').addClass('col-2 d-inline-flex').append(image);
            var h4Node = $('<h4/>').addClass('card-title').text(topic.title);
            var descriptionNode = $('<p/>').addClass('card-text').text('This topic is related to ' + topic.title);
            var textContainer = $('<div/>').addClass('col-10').append(h4Node).append(descriptionNode);

            var card = $('<div/>').addClass('card border-0 shadow dream-card').append($('<div/>').addClass('card-body').append($('<div/>').addClass('row').append(imageContainer).append(textContainer)));

            var aNode = $('<a/>').addClass('link-card').attr('href', 'topic/'+topic.topicId).append(card);

            var container = $('<div/>').addClass('col-12 card-container').append(aNode);

            this.container.append(container);
        }
    }

});