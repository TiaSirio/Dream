package it.dreamplatform.forum.controller;

import it.dreamplatform.forum.bean.PostBean;
import it.dreamplatform.forum.bean.UserBean;
import it.dreamplatform.forum.entities.Post;
import it.dreamplatform.forum.entities.User;
import it.dreamplatform.forum.mapper.PostMapper;
import it.dreamplatform.forum.mapper.UserMapper;
import it.dreamplatform.forum.services.PostService;
import it.dreamplatform.forum.services.UserService;

import javax.inject.Inject;
import java.util.List;
import java.util.Objects;

/**
 * This class contains all the controller used by a Post entity or a Post bean.
 */
public class PostController {
    @Inject
    PostService postService;
    @Inject
    PostMapper postMapper;
    @Inject
    UserService userService;
    @Inject
    UserMapper userMapper;
    @Inject
    NotificationController notificationController;

    @Inject
    public PostController(PostService postService, PostMapper postMapper, UserService userService, UserMapper userMapper, NotificationController notificationController) {
        this.postService = postService;
        this.postMapper = postMapper;
        this.userService = userService;
        this.userMapper = userMapper;
        this.notificationController = notificationController;
    }

    /**
     * This function is used to retrieve the List of pending posts (the posts with status 0) from the DB.
     */
    public List<PostBean> getPendingPosts() {
        return postMapper.mapEntityListToBeanList(postService.getPendingPosts());
    }

    /**
     * This function is used to approve a post, setting its status to 1, that is in pending list. So it's initial status is set 0.
     * @param postId the id of the selected Post.
     * @throws Exception when the post didn't exist or when the post has already been approved.
     */
    public void approvePendingPost(Long postId) throws Exception {
        Post post = postService.getPostById(postId);
        if(post == null){
            throw new Exception("The selected post has an invalid Id.");
        }
        if(post.getStatus() == 1){
            throw new Exception("The post has already been accepted.");
        }
        post.setStatus(1);
        postService.savePost(post);
        notificationController.notifyApprovePost(post);
    }

    /**
     * This function is used to decline a post, deleting it from the DB, that is in pending list. So it's initial status is set 0.
     * @param postId the id of the selected Post.
     * @throws Exception when the post didn't exist or when the post has already been approved.
     */
    public void declinePendingPost(Long postId) throws Exception {
        Post post = postService.getPostById(postId);
        if(post == null){
            throw new Exception("The selected post has an invalid Id.");
        }
        if(post.getStatus() == 1){
            throw new Exception("The post can't be declined.");
        }
        notificationController.notifyDeclinePost(post);
        postService.deletePost(post);
    }

    /**
     * This function is used to delete a post from the DB.
     * @param postId is the id of the selected Post.
     * @throws Exception the id of the post is not valid.
     */
    public void deletePost(Long postId) throws Exception{
        if(postId == null){
            throw new Exception("Post not valid.");
        }
        Post post = postService.getPostById(postId);
        if(post != null){
            postService.deletePost(post);
        }
    }

    /**
     * This function will create a post, then it will store it, calling the savePost.
     * @param post is the PostBean that will be mapped to an entity and stored on the DB.
     * @param userBean is the bean of the user in the session.
     * @throws Exception when the user is null or the text of the post is null.
     */
    public void publishPost(PostBean post, UserBean userBean) throws Exception {
        User user = userService.getUserById(userBean.getUserId());
        if(user == null){
            throw new Exception("User not found.");
        }
        if(post.getText() == null){
            throw new Exception("The post must contains text.");
        }
        post.setCreator(userMapper.mapEntityToPublicBean(user));
        if(user.getPolicyMakerID() == null){
            post.setStatus(0);
        }
        else{
            post.setStatus(1);
            notificationController.notifyFollowers(post.getDiscussionId());
        }
        postService.savePost(postMapper.mapBeanToEntity(post));
    }

    /**
     * This function will modify a post, then it will store it, calling the savePost.
     * @param post is the PostBean that will be mapped to an entity and mod.
     * @param user is the bean of the user in the session.
     * @throws Exception when the post doesn't exist or the creator of the post is not a Policy maker and try to modify a post that he hasn't created.
     */
    public void modifyPost(PostBean post, UserBean user) throws Exception {
        Post postToModify = postService.getPostById(post.getPostId());
        if(postToModify == null){
            throw new Exception("The post to modify is not present");
        }
        if(user.getPolicyMakerID() == null){
            if(!Objects.equals(postToModify.getCreator().getUserId(), userMapper.mapBeanToEntity(user).getUserId())){
                throw new Exception("You don't have the permission to modify the post.");
            }
        }
        postService.savePost(postMapper.mapBeanToEntity(post));
        notificationController.notifyFollowers(post.getDiscussionId());
    }

    /**
     * This function retrieve a given post, given its id.
     * @param postId is the id of the selected post.
     * @return the Bean of the needed post.
     * @throws Exception when the post is not found.
     */
    public PostBean getPostById(Long postId) throws Exception {
        if(postService.getPostById(postId) != null){
            return postMapper.mapEntityToBean(postService.getPostById(postId));
        } else {
            throw new Exception("Post not found!");
        }
    }

    /**
     * This function retrieve the posts in a given discussion.
     * @param discussionId is the id of the selected discussion.
     * @return a List of Bean with all the posts contained in the discussion.
     * @throws Exception the post is not found.
     */
    public List<PostBean> getPostsByDiscussionId(Long discussionId) throws Exception {
        if(postService.getPostsByDiscussionId(discussionId) != null){
            return postMapper.mapEntityListToBeanList(postService.getPostsByDiscussionId(discussionId));
        } else {
            throw new Exception("Post not found!");
        }
    }

    /**
     * This function retrieve the posts written by a User.
     * @param creatorId is the id of the User.
     * @return a List of Bean with all the posts written by the selected User.
     * @throws Exception when the User does not exist.
     */
    public List<PostBean> getPostsByUser(Long creatorId) throws Exception {
        if(userService.getUserById(creatorId) != null){
            List<PostBean> posts = postMapper.mapEntityListToBeanList(postService.getPostsByCreator(creatorId));
            for (int i = 0; i < posts.size(); i++){
                if (posts.get(i).getStatus() == 0) {
                    posts.remove(i);
                }
            }
            return posts;
        } else {
            throw new Exception("The user Id inserted not exist.");
        }
    }

}
