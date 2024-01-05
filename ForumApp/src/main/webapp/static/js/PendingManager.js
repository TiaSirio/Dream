$(function () {

    var pendingController = new PendingController($('#moderatorRepliesContainer'));
    pendingController.loadData();

    var modalController = new ModalController($('#approveReplyModal'), $('#modalDeclineButton'), $('#modalApproveButton'));
    modalController.init();


    function ModalController(_modal, _decline, _approve){
        var self = this;
        this.modal = _modal;
        this.declineButton = _decline;
        this.approveButton = _approve;
        this.currentPost = {};

        this.init = function() {
            CKEDITOR.replace('replyContent');
            this.declineButton.on('click', function(){
                self.onDecline();
            });

            this.approveButton.on('click', function(){
                self.onApprove();
            });

        }

        this.prepare = function(pId) {
            $.get('../api/post/'+pId, (json) => {
                self.currentPost = json;
                CKEDITOR.instances.replyContent.setData(json.text);
                self.modal.modal('show');
            }).fail((error) => {
                Swal.fire({
                    icon: 'error',
                    title: 'An error occurred. Please try again',
                    showConfirmButton: false,
                    timer: 1500
                });
            });
        }

        this.onApprove = function() {
            self.currentPost.timestamp = new Date();
            self.currentPost.text = CKEDITOR.instances.replyContent.getData();
            $.ajax({
                type: "POST",
                url: "../api/post/approve",
                data: JSON.stringify(self.currentPost),
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function(data){
                    self.modal.modal('hide');
                    Swal.fire({
                        icon: 'success',
                        title: 'Post successfully approved',
                        showConfirmButton: false,
                        timer: 1500
                    }).then((r) => {
                        pendingController.loadData();
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

        this.onDecline = function() {
            $.ajax({
                type: "POST",
                url: "../api/post/decline/"+self.currentPost.postId,
                contentType: "application/json; charset=utf-8",
                dataType: "json",
                success: function(data){
                    self.modal.modal('hide');
                    Swal.fire({
                        icon: 'success',
                        title: 'Post successfully deleted',
                        showConfirmButton: false,
                        timer: 1500
                    }).then((r) => {
                        pendingController.loadData();
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


    function PendingController(_container){
        var self = this;
        this.container = _container;

        this.loadData = function() {
            this.container.html('');
            $('.pendingPostElement').prop('onclick', null).off('click');
            $.get('../api/post/pending', (json) => {
                if(json.length === 0){
                    self.createEmptyAlert();
                } else {
                    json.forEach(p => {
                       self.createNode(p);
                    });
                    self.registerCallback();
                }
            }).fail((error) => {
                self.conteiner.append('<div class="alert alert-danger">An error occurred. Please try later</div>');
            });
        }

        this.createEmptyAlert = function (){
            var alert = $('<div/>');
            alert.addClass('alert alert-success').text("No pending posts available");
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
            var postContent = $('<div/>').addClass('col-9').append($('<h5/>').addClass("card-title").text('Discussion '+p.discussionId));
            postContent.append($('<div/>').addClass('card-text').html(p.text));
            var postRow = $('<div/>').addClass('row').append(userInfo).append(postContent);
            var container = $('<div/>').addClass("col-12 card-container").append(
                $('<div/>').addClass("card border-0 shadow dream-card").append(
                    $('<div/>').addClass("card-body").append(postRow).append($('<button/>').addClass('btn btn-sm btn-outline-primary w-25 float-end pendingPostElement').text("Review").attr('data-post-id', p.postId))
                )
            );
            this.container.append(container);
        }

        this.registerCallback = function() {
            $('.pendingPostElement').on('click', function(e){
                modalController.prepare($(e.target).attr('data-post-id'));
            });
        }
    }


});