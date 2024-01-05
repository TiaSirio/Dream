package it.dreamplatform.forum.controller;

import it.dreamplatform.forum.bean.DiscussionBean;
import it.dreamplatform.forum.bean.DiscussionContentBean;
import it.dreamplatform.forum.bean.TopicBean;
import it.dreamplatform.forum.bean.TopicContentBean;
import it.dreamplatform.forum.entities.Discussion;
import it.dreamplatform.forum.entities.Topic;
import it.dreamplatform.forum.mapper.DiscussionMapper;
import it.dreamplatform.forum.mapper.TopicMapper;
import it.dreamplatform.forum.services.DiscussionService;
import it.dreamplatform.forum.services.TopicService;


import javax.inject.Inject;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains all the controller used by a Topic entity or a Topic bean of every genre.
 */
public class TopicController {
    @Inject
    TopicMapper topicMapper;
    @Inject
    TopicService topicService;
    @Inject
    DiscussionService discussionService;
    @Inject
    DiscussionMapper discussionMapper;

    /**
     * This function is used to retrieve a TopicBean.
     * @param topicId is the id of the selected Topic.
     * @return a Bean containing all the elements of a Topic, excluding the discussions inside it.
     */
    public TopicBean getTopicById(Long topicId) {
        return topicMapper.mapEntityToBean(topicService.getTopicById(topicId));
    }

    /**
     * This function is used to retrieve a TopicContentBean.
     * @param topicId is the id of the selected Topic.
     * @return a Bean containing all the elements of a Topic, including all the discussions inside it.
     */
    public TopicContentBean getDiscussionsByTopicId(Long topicId){
        Topic topic = topicService.getTopicById(topicId);
        List<Discussion> discussions = new ArrayList<>();
        List<DiscussionBean> discussionBeans = new ArrayList<>();
        int numberOfReplies = 0;
        for (int i = 0; i < topic.getDiscussions().size(); i++){
            discussions.add(discussionService.getDiscussionById(topic.getDiscussions().get(i).getDiscussionId()));
            for (int j = 0; j < discussions.get(i).getPosts().size(); j++){
                numberOfReplies = discussions.get(i).getPosts().size();
                if (discussions.get(i).getPosts().get(j).getStatus() == 0) {
                    numberOfReplies--;
                }
            }
            discussionBeans.add(discussionMapper.mapEntityToBeanForDiscussion(discussions.get(i), new DiscussionBean(), numberOfReplies));
        }
        TopicContentBean topicContentBean = topicMapper.mapEntityToContentBean(topic);
        topicContentBean.setDiscussions(discussionBeans);
        return topicContentBean;
    }

    /**
     * This function is used to retrieve a List of TopicBean.
     * @return a List of TopicBean containing, for each Topic, all the elements of a Topic, excluding the discussions inside it.
     */
    public List<TopicBean> getAllTopics() {
        return topicMapper.mapEntityListToBeanList(topicService.getAllTopics());
    }
}
