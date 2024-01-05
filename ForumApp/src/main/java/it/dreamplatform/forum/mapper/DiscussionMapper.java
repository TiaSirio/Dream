package it.dreamplatform.forum.mapper;

import it.dreamplatform.forum.bean.DiscussionContentBean;
import it.dreamplatform.forum.entities.Discussion;
import it.dreamplatform.forum.bean.DiscussionBean;
import it.dreamplatform.forum.entities.Post;
import it.dreamplatform.forum.entities.Topic;

import javax.inject.Inject;
import java.util.*;

/**
 * This class do different mappings between a Discussion entity to its corresponding bean and vice versa.
 */
public class DiscussionMapper {
    @Inject
    PostMapper postMapper;
    @Inject
    UserMapper userMapper;

    @Inject
    public DiscussionMapper(PostMapper postMapper, UserMapper userMapper) {
        this.postMapper = postMapper;
        this.userMapper = userMapper;
    }

    /**
     * This function creates a DiscussionBean and call another function to maps the values.
     * @param entity is the entity of the DB.
     * @return the corresponding Bean just created.
     */
    public DiscussionBean mapEntityToBean (Discussion entity){
        return mapEntityToBean(entity, new DiscussionBean());
    }

    /**
     * This function sets the values of a DiscussionBean using the values retrieve from a Discussion entity.
     * @param entity is the entity of the DB.
     * @param bean is the object into which the function will set all the values.
     * @return the corresponding DiscussionBean or null if the entity is null.
     */
    public DiscussionBean mapEntityToBean (Discussion entity, DiscussionBean bean){
        if (entity == null) {return null;}
        bean.setDiscussionId(entity.getDiscussionId());
        bean.setTitle(entity.getTitle());
        bean.setText(entity.getText());
        bean.setTimestamp(entity.getTimestamp());
        bean.setTopicId(entity.getTopic().getTopicId());
        bean.setNumberReplies(entity.getPosts().size());
        bean.setCreator(userMapper.mapEntityToPublicBean(entity.getPosts().stream().min(Comparator.comparingLong(Post::getPostId)).get().getCreator()));
        return bean;
    }

    /**
     * This function sets the values of a DiscussionBean using the values retrieve from a Discussion entity.
     * @param entity is the entity of the DB.
     * @param bean is the object into which the function will set all the values.
     * @param numberOfReplies is the modified number of replies without the post with status = 0;
     * @return the corresponding DiscussionBean or null if the entity is null.
     */
    public DiscussionBean mapEntityToBeanForDiscussion(Discussion entity, DiscussionBean bean, int numberOfReplies){
        if (entity == null) {return null;}
        bean.setDiscussionId(entity.getDiscussionId());
        bean.setTitle(entity.getTitle());
        bean.setText(entity.getText());
        bean.setTimestamp(entity.getTimestamp());
        bean.setTopicId(entity.getTopic().getTopicId());
        bean.setNumberReplies(numberOfReplies);
        bean.setCreator(userMapper.mapEntityToPublicBean(entity.getPosts().stream().min(Comparator.comparingLong(Post::getPostId)).get().getCreator()));
        return bean;
    }

    /**
     * This function creates a Discussion entity and call another function to maps the values.
     * @param bean is the DiscussionBean from which the value will be retrieved.
     * @return the corresponding Discussion entity just created.
     */
    public Discussion mapBeanToEntity (DiscussionBean bean){
        return mapBeanToEntity(new Discussion(), bean);
    }

    /**
     * This function sets the values of a Discussion entity using the values retrieve from a DiscussionBean.
     * @param entity is the entity of the DB.
     * @param bean is the object from which the values will be retrieved.
     * @return the corresponding Discussion entity or null if the Bean is null.
     */
    public Discussion mapBeanToEntity (Discussion entity, DiscussionBean bean){
        if (bean == null) {return null;}
        entity.setDiscussionId(bean.getDiscussionId());
        entity.setTitle(bean.getTitle());
        entity.setText(bean.getText());
        entity.setTimestamp(bean.getTimestamp());
        return entity;
    }

    /**
     * This function creates a new List and call another function to map the values from the entities inside it.
     * @param entities are the Discussion entities from which the values will be retrieved.
     * @return the corresponding List of DiscussionBean just created.
     */
    public List<DiscussionBean> mapEntityListToBeanList(List<Discussion> entities){
        return mapEntityListToBeanList(entities, new ArrayList<>());
    }

    /**
     * This function maps every Discussion entity into a List of DiscussionBean.
     * @param entities is the List of Discussion entities of the DB.
     * @param beans is the List of object into which the entities will be mapped.
     * @return the corresponding List of DiscussionBean or null if the List of entities is null.
     */
    public List<DiscussionBean> mapEntityListToBeanList(List<Discussion> entities, List<DiscussionBean> beans){
        if (entities.isEmpty()) {return Collections.emptyList();}
        entities.forEach(e -> {
            beans.add(mapEntityToBean(e));
        });
        return beans;
    }

    /**
     * This function creates a DiscussionContentBean and call another function to maps the values.
     * @param entity is the entity of the DB.
     * @return the corresponding Bean just created.
     */
    public DiscussionContentBean mapEntityToContentBean(Discussion entity){
        return mapEntityToContentBean(entity, new DiscussionContentBean());
    }

    /**
     * This function sets the values of a DiscussionContentBean using the values retrieve from a discussion entity.
     * @param entity is the entity of the DB.
     * @param bean is the object into which the function will set all the values.
     * @return the corresponding DiscussionContentBean or null if the entity is null.
     */
    public DiscussionContentBean mapEntityToContentBean(Discussion entity, DiscussionContentBean bean){
        if (entity == null) {return null;}
        bean.setTopicId(entity.getTopic().getTopicId());
        bean.setDiscussionId(entity.getDiscussionId());
        bean.setTitle(entity.getTitle());
        bean.setTimestamp(entity.getTimestamp());
        bean.setCreator(userMapper.mapEntityToPublicBean(entity.getPosts().stream().min(Comparator.comparingLong(Post::getPostId)).get().getCreator()));
        bean.setText(entity.getText());
        bean.setPosts(postMapper.mapEntityListToBeanList(entity.getPosts()));
        bean.setNumberReplies(entity.getPosts().size());
        return bean;
    }

    /**
     * This function creates a Discussion entity and call another function to maps the values.
     * @param bean is the Bean from which the value will be retrieved.
     * @return the corresponding Discussion entity just created.
     */
    public Discussion mapContentBeanToEntity(DiscussionContentBean bean){
        return mapContentBeanToEntity(bean, new Discussion());
    }

    /**
     * This function sets the values of a Discussion entity using the values retrieve from a DiscussionContentBean.
     * @param entity is the entity of the DB.
     * @param bean is the object from which the values will be retrieved.
     * @return the corresponding Discussion entity or null if the Bean is null.
     */
    public Discussion mapContentBeanToEntity(DiscussionContentBean bean, Discussion entity){
        if (bean == null) {return null;}
        entity.setText(bean.getText());
        entity.setTimestamp(bean.getTimestamp());
        entity.setTitle(bean.getTitle());
        Topic topic = new Topic();
        topic.setTopicId(bean.getTopicId());
        entity.setTopic(topic);
        return entity;
    }
}
