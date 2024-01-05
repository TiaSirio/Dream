package it.dreamplatform.forum.bean;

import java.io.Serializable;
import java.util.Date;

/**
 * This class represents a Topic. It is mapped from the Topic entity, it doesn't have the List of Discussion
 * contained in the Topic entity. The only relevant value is the title that represents the title of the Topic.
 */
public class TopicBean implements Serializable {
    private Long topicId;
    private String title;
    private Date timestamp;

    public Long getTopicId() {
        return topicId;
    }

    public void setTopicId(Long topicId) {
        this.topicId = topicId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Date getTimestamp() {
        return this.timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }
}
