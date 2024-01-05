$(function () {
    var MODAL_ACTION = "create";
    var current_selected_post = undefined;
    if(typeof userId !== 'undefined'){
        CKEDITOR.replace('replyContent');

        $('#modalCreateReplyButton').on('click', function (e){
            var form = $('#newReplyForm');
            if(MODAL_ACTION === 'create'){
                var postObj = {};

                var text = CKEDITOR.instances.replyContent.getData();

                postObj.text = text;
                postObj.timestamp = new Date();
                postObj.creator = {"userId": userId};
                postObj.discussionId = discussionId;
                $.ajax({
                    type: "POST",
                    url: "../api/post/publish",
                    data: JSON.stringify(postObj),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json",
                    success: function(data){
                        $('#newReplyModal').modal('hide');
                        CKEDITOR.instances.replyContent.setData("");
                        Swal.fire({
                            icon: 'success',
                            title: 'Post successfully added',
                            showConfirmButton: false,
                            timer: 1500
                        }).then((r) => {
                            loadData();
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
            } else {
                if(current_selected_post !== undefined){
                    current_selected_post.text = CKEDITOR.instances.replyContent.getData();
                    current_selected_post.timestamp = new Date();
                    $.ajax({
                        type: "POST",
                        url: "../api/post/modify",
                        data: JSON.stringify(current_selected_post),
                        contentType: "application/json; charset=utf-8",
                        dataType: "json",
                        success: function(data){
                            $('#newReplyModal').modal('hide');
                            CKEDITOR.instances.replyContent.setData("");
                            $('#modalCreateReplyButton').text("Create");
                            Swal.fire({
                                icon: 'success',
                                title: 'Post successfully modified',
                                showConfirmButton: false,
                                timer: 1500
                            }).then((r) => {
                                loadData();
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

        });

    }

    var repliesContainer = $('#repliesContainer');
    var discussionTitle = $('#discussionTitle');

    loadData();

    function loadData() {
        repliesContainer.html("");
        $.ajax(
            {
                'url': '../api/discussion/'+discussionId,
                'method': 'GET',
                'success': function(json){
                    var first = json.posts[0];
                    createPrimaryDiscussion(first, json);
                    json.posts.shift();
                    json.posts.forEach(d => {
                        createReplyNode(d, json);
                    });
                    registerActions();
                },
                'error':function(){
                    alert('Invalid discussion');
                }
            }
        );
    }

    function registerActions() {
        $('.modifyDropdown').on('click', function (e){
            MODAL_ACTION = "update";
            var postId = $(e.target).data('post-id');
            fetch('../api/post/'+postId).then(response => {
                return response.json();
            }).then(json => {
               current_selected_post = json;
               CKEDITOR.instances.replyContent.setData(json.text);
                $('#modalCreateReplyButton').text("Update");
               $('#newReplyModal').modal('show');
            });
        });

        $('.deleteDropdown').on('click', function (e){
            var postId = $(e.target).data('post-id');
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    $.post( "../api/post/delete/"+postId, function( data ) {
                        Swal.fire(
                            'Deleted!',
                            'Your file has been deleted.',
                            'success'
                        )
                        loadData();
                    }).fail(function (error){
                        Swal.fire(
                            'Error!',
                            'An error occured while deleting',
                            'error'
                        )
                    });

                }
            });
        });

        $('#discussionDelete').on('click', function (e){
            Swal.fire({
                title: 'Are you sure?',
                text: "You won't be able to revert this!",
                icon: 'warning',
                showCancelButton: true,
                confirmButtonColor: '#3085d6',
                cancelButtonColor: '#d33',
                confirmButtonText: 'Yes, delete it!'
            }).then((result) => {
                if (result.isConfirmed) {
                    $.post( "../api/discussion/delete/"+discussionId, function( data ) {
                        Swal.fire(
                            'Deleted!',
                            'Your file has been deleted.',
                            'success'
                        )
                        window.location.href = '../home';
                    }).fail(function (error){
                        Swal.fire(
                            'Error!',
                            'An error occured while deleting',
                            'error'
                        )
                    });

                }
            });
        });

        $('#discussionModify').on('click', function (e){

        });


    }

    function createPrimaryDiscussion(p, d){
        discussionTitle.text(d.title);
        $('#discussionText').html(p.text);
        $('#discussionModify').attr('data-post-id', p.postId);
        $('#discussionCreatorName').text(p.creator.name+' '+p.creator.surname);
        $('#discussionCreatorArea').text('District of '+ p.creator.areaOfResidence);
    }


    function createReplyNode(p, t) {
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
        if((typeof isPolicyMaker !== 'undefined' && typeof userId !== 'undefined') && (isPolicyMaker || p.creator.userId===userId)){
            postContent.append('<a class="float-end text-secondary" id="cardDropdown'+p.postId+'" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false"><i class="fas fa-ellipsis-v"></i></a>\n' +
                '                                    <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="cardDropdown'+p.postId+'" >\n' +
                '                                        <li><a class="dropdown-item modifyDropdown" href="#" data-post-id="'+p.postId+'"><i class="fas fa-pen"></i> Modify</a></li>\n' +
                '                                        <li><a class="dropdown-item deleteDropdown" href="#" data-post-id="'+p.postId+'"><i class="fa fa-trash"></i> Delete</a></li>\n' +
                '                                    </ul>');
        }
        postContent.append($('<div/>').addClass('card-text').html(p.text));
        var postRow = $('<div/>').addClass('row').append(userInfo).append(postContent);
        var container = $('<div/>').addClass("col-12 card-container").append(
            $('<div/>').addClass("card border-0 shadow dream-card").append(
                $('<div/>').addClass("card-body").append(postRow)
            )
        );
        repliesContainer.append(container);
    }


});