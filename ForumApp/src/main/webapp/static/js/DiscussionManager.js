$(function () {

    var discussionController = new DiscussionController($('#topicTitle'), $('#discussionContainer'), topicId);
    discussionController.loadData();

    if(typeof isPolicyMaker !== 'undefined' && isPolicyMaker){
        var modalController = new ModalController($('#newDiscussionModal'), $('#newDiscussionForm'), $('#modalCreateDiscussionButton'), userId, topicId);
        modalController.init();
    }

    function ModalController(_modal, _form, _formButton, _userId, _topicId){
        var self = this;
        this.modal = _modal;
        this.form = _form;
        this.button = _formButton;
        this.userId = _userId;
        this.topicId = _topicId;

        this.init = function (){
            CKEDITOR.replace('discussionContent');
            this.button.on('click', function (e){
                self.postData();
            });
        }

        this.getDiscussiOnbject = function() {
            var postObj = {};
            var discussionObj = {};
            discussionObj.title = this.form.find('input[name="discussionTitle"]').val();
            var text = CKEDITOR.instances.discussionContent.getData();
            discussionObj.text = text;
            discussionObj.timestamp = new Date();
            discussionObj.topicId = this.topicId;
            postObj.text = text;
            postObj.timestamp = new Date();
            postObj.creator = {"userId": this.userId};
            discussionObj.posts = [postObj];
            return discussionObj;
        }

        this.postData = function (){
            $.ajax({
                type: "POST",
                url: "../api/discussion/create",
                data: JSON.stringify(self.getDiscussiOnbject()),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function(data){
                    self.modal.modal('hide');
                    Swal.fire({
                        icon: 'success',
                        title: 'Discussion successfully added',
                        showConfirmButton: false,
                        timer: 1500
                    }).then((r) => {
                        discussionController.loadData();
                    });
                },
                error: function(e) {
                    Swal.fire({
                        icon: 'error',
                        title: 'An error occurred. Please try again',
                        showConfirmButton: false,
                        timer: 1500
                    });
                }
            });
        }

    }


    function DiscussionController(_title, _container, _topicId) {
        var self = this;
        this.title = _title;
        this.container = _container;
        this.topicId = _topicId;

        this.loadData = function() {
            this.container.html("");
            $.get('../api/topic/' + this.topicId, (json) => {
                this.title.text(json.title);
                if (json.discussions.length === 0) {
                    self.createEmptyAlert();
                } else {
                    json.discussions.forEach(d => {
                        self.createNode(d, json);
                    });
                }
            }).fail((error) => {
                self.conteiner.append('<div class="alert alert-danger">An error occurred. Please try later</div>');
            });

        }


        this.createEmptyAlert = function() {
            var alert = $('<div/>');
            alert.addClass('alert alert-primary').text("No discussions available");
            this.container.append(alert);
        }

        this.createNode = function(d, t) {
            var className = 'vstack gap-0 text-secondary mx-auto align-items-center';
            var rank = $('<div/>').addClass(className).append($('<i/>').addClass('fas fa-sort-up fa-2x text-secondary')).append($('<strong/>').text(d.number_replies)).append($('<i/>').addClass('fas fa-sort-down fa-2x text-secondary'));

            var rankNode = $('<div/>').addClass('col-1 d-flex').append(rank);

            var topicNode = $('<span/>').addClass('text-secondary float-end small').text("Topic: " + t.title);
            var h4Node = $('<h4/>').addClass('card-title').text(d.title);
            var descriptionNode = $('<div/>').addClass('card-text').html(d.text);
            var authorNode = '<table class="table table-borderless text-secondary"><tbody><tr class="small">' +
                '<td><img src="../static/assets/placeholder_user.png" class="top-user-img"> Posted' +
                'by: <strong>'+d.creator.name + ' ' + d.creator.surname+'</strong></td>' +
                '<td>' + d.timestamp + '</td>' +
                '<td class="text-sm-end"><i class="fas fa-comments"></i> '+ d.number_replies +'</td>' +
                '</tr></tbody></table>';
            var textContainer = $('<div/>').addClass('col-11').append(topicNode).append(h4Node).append(descriptionNode).append('<hr/>').append(authorNode);

            var card = $('<div/>').addClass('card border-0 shadow dream-card').append($('<div/>').addClass('card-body').append($('<div/>').addClass('row').append(rankNode).append(textContainer)));

            var aNode = $('<a/>').addClass('link-card').attr('href', '../discussion/'+d.discussionId).append(card);

            var container = $('<div/>').addClass('col-12 card-container').append(aNode);

            this.container.append(container);
        }
    }



});