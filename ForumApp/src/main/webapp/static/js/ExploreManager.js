$(function () {

    const exploreController = new ExploreController( $('#discussionContainer'));
    exploreController.loadData();

    function ExploreController(_container) {
        var self = this;
        this.conteiner = _container;
        this.topics = [];

        this.loadData = function() {
            self.conteiner.html("");
            $.get('api/discussion/explore', (json) => {
                if(json.length === 0) {
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

        this.createEmptyAlert = function() {
            var alert = $('<div/>');
            alert.addClass('alert alert-primary').text("No discussions available");
            this.conteiner.append(alert);
        }

        this.createNode = function(d){
            var className = 'vstack gap-0 text-secondary mx-auto align-items-center';
            var rank = $('<div/>').addClass(className).append($('<i/>').addClass('fas fa-sort-up fa-2x text-secondary')).append($('<strong/>').text(d.number_replies)).append($('<i/>').addClass('fas fa-sort-down fa-2x text-secondary'));

            var rankNode = $('<div/>').addClass('col-1 d-flex').append(rank);

            var topicNode = $('<span/>').addClass('text-secondary float-end small').text("Topic: " + d.topicId);
            var h4Node = $('<h4/>').addClass('card-title').text(d.title);
            var descriptionNode = $('<div/>').addClass('card-text').html(d.text);
            var authorNode = '<table class="table table-borderless text-secondary"><tbody><tr class="small">' +
                '<td><img src="static/assets/placeholder_user.png" class="top-user-img"> Posted' +
                'by: <strong>'+d.creator.name + ' ' + d.creator.surname+'</strong></td>' +
                '<td>' + d.timestamp + '</td>' +
                '<td class="text-sm-end"><i class="fas fa-comments"></i> '+ d.number_replies +'</td>' +
                '</tr></tbody></table>';
            var textContainer = $('<div/>').addClass('col-11').append(topicNode).append(h4Node).append(descriptionNode).append('<hr/>').append(authorNode);

            var card = $('<div/>').addClass('card border-0 shadow dream-card').append($('<div/>').addClass('card-body').append($('<div/>').addClass('row').append(rankNode).append(textContainer)));

            var aNode = $('<a/>').addClass('link-card').attr('href', 'discussion/'+d.discussionId).append(card);

            var container = $('<div/>').addClass('col-12 card-container').append(aNode);

            this.conteiner.append(container);
        }
    }


});