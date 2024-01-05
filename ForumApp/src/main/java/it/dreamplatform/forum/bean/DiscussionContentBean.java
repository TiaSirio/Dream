package it.dreamplatform.forum.bean;

import java.util.List;

/**
 * This class extends the classic Discussion, but instead of having just the number of posts, this class contains
 * the complete List of Posts of the chosen Discussion.
 */
public class DiscussionContentBean extends DiscussionBean {
    List<PostBean> posts;

    public List<PostBean> getPosts() {
        return posts;
    }

    public void setPosts(List<PostBean> posts) {
        this.posts = posts;
    }
}
