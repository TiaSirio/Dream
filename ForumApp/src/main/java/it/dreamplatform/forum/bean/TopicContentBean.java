package it.dreamplatform.forum.bean;

import java.util.List;

/**
 * This class extends the classic Topic, but it contains also the complete List of Discussion of the chosen Topic.
 */
public class TopicContentBean extends TopicBean {

    private List<DiscussionBean> discussions;

    public List<DiscussionBean> getDiscussions() {
        return discussions;
    }

    public void setDiscussions(List<DiscussionBean> discussions) {
        this.discussions = discussions;
    }
}
