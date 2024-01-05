package it.dreamplatform.forum.controller;

import it.dreamplatform.forum.bean.DiscussionBean;
import it.dreamplatform.forum.bean.DiscussionContentBean;
import it.dreamplatform.forum.bean.PostBean;
import it.dreamplatform.forum.bean.PublicUserBean;
import it.dreamplatform.forum.entities.Discussion;
import it.dreamplatform.forum.entities.Post;
import it.dreamplatform.forum.mapper.DiscussionMapper;
import it.dreamplatform.forum.mapper.PostMapper;
import it.dreamplatform.forum.mapper.UserMapper;
import it.dreamplatform.forum.services.DiscussionService;
import it.dreamplatform.forum.services.PostService;
import it.dreamplatform.forum.services.UserService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all the controller used by a Discussion entity or a Discussion bean of every genre.
 */
public class DiscussionController {
    @Inject
    DiscussionMapper discussionMapper;
    @Inject
    DiscussionService discussionService;
    @Inject
    UserService userService;
    @Inject
    PostService postService;
    @Inject
    PostMapper postMapper;
    @Inject
    UserMapper userMapper;

    @Inject
    public DiscussionController(DiscussionMapper discussionMapper, DiscussionService discussionService, UserService userService, PostService postService, PostMapper postMapper, UserMapper userMapper) {
        this.discussionMapper = discussionMapper;
        this.discussionService = discussionService;
        this.userService = userService;
        this.postService = postService;
        this.postMapper = postMapper;
        this.userMapper = userMapper;
    }

    /**
     * This function is used to retrieve a DiscussionContentBean.
     * @param discussionId is the id of the selected Discussion.
     * @return a Bean containing all the elements of a Discussion.
     */
    public DiscussionContentBean getPostsByDiscussionId(Long discussionId){
        DiscussionContentBean discussionContentBean = discussionMapper.mapEntityToContentBean(discussionService.getDiscussionById(discussionId));
        for (int i = 0; i < discussionContentBean.getPosts().size(); i++){
            if (discussionContentBean.getPosts().get(i).getStatus() == 0) {
                discussionContentBean.getPosts().remove(i);
                discussionContentBean.setNumberReplies(discussionContentBean.getNumberReplies() - 1);
            }
        }
        return discussionContentBean;
    }

    /**
     * This function is used to retrieve a List of DiscussionBean of the most active Discussion.
     * @param numberOfDiscussion is the number of the top discussions to be retrieved.
     * @return a List of Bean of the retrieved discussions.
     */
    public List<DiscussionBean> getDiscussionExplore(int numberOfDiscussion){
        return discussionMapper.mapEntityListToBeanList(discussionService.getMostActiveDiscussions(numberOfDiscussion));
    }

    /**
     * This function is used to retrieve the List of User that has replied to a given discussion.
     * @param discussionId is the id of the Discussion we are interested in.
     * @return a List of Bean of the retrieved users.
     */
    public List<PublicUserBean> getDiscussionFollowers(Long discussionId){
        return userMapper.mapEntityListToPublicUserBeanList(discussionService.getDiscussionFollowers(discussionId));
    }

    /**
     * This function is used to retrieve all the discussion of the selected Policy maker (checking if it exists or not).
     * @param policyMakerId is the id of the selected Policy maker.
     * @return the List of Discussion created by that Policy maker.
     * @throws Exception when the Policy maker is not found.
     */
    public List<DiscussionBean> getDiscussionsByPolicyMaker(String policyMakerId) throws Exception {
        if (userService.getUserByPolicyMakerId(policyMakerId) != null) {
            return discussionMapper.mapEntityListToBeanList(discussionService.getDiscussionByPolicyMaker(policyMakerId));
        } else {
            throw new Exception("Policy Maker was not found");
        }
    }

    /**
     * This function is used to delete a discussion from the DB.
     * @param discussionId is the id of the selected discussion.
     * @throws Exception when the discussion is not found.
     */
    public void deleteDiscussion(Long discussionId) throws Exception {
        if(discussionService.getDiscussionById(discussionId) != null){
            discussionService.deleteDiscussion(discussionService.getDiscussionById(discussionId));
        } else {
            throw new Exception("The discussion was not found.");
        }
    }

    /**
     * This function is used to create a new discussion, with also the first post of the created discussion (already set to 1) and save it on the DB.
     * @param discussion is the complete Bean that is going to be the new discussion.
     */
    public void createDiscussion(DiscussionContentBean discussion)/* throws Exception */{
        PostBean pb = discussion.getPosts().get(0);
        Discussion d = discussionMapper.mapContentBeanToEntity(discussion);
        d.setPosts(new ArrayList<>());
        Long newDiscussionId = discussionService.saveDiscussion(d);
        pb.setDiscussionId(newDiscussionId);
        Post post = postMapper.mapBeanToEntity(pb);
        post.setStatus(1);
        postService.savePost(post);
    }
}
