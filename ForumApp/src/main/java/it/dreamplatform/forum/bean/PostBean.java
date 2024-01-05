package it.dreamplatform.forum.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents a Post. The text is the content of the post, the discussionId is the id of the Discussion
 * in which this post is contained, it is also present the creator of the Post and the status value that check if
 * the post is APPROVED (Status = 1) or is PENDING (Status = 0).
 */
public class PostBean implements Serializable {
    private Long postId;
    private String text;
    private Date timestamp;
    private int status;
    private PublicUserBean creator;
    private Long discussionId;

    public Long getPostId() {
        return postId;
    }

    public void setPostId(Long postId) {
        this.postId = postId;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public PublicUserBean getCreator() {
        return creator;
    }

    public void setCreator(PublicUserBean creator) {
        this.creator = creator;
    }

    public Long getDiscussionId() {
        return discussionId;
    }

    public void setDiscussionId(Long discussionId) {
        this.discussionId = discussionId;
    }
}
