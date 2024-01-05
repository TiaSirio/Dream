package it.dreamplatform.forum.mapper;

import it.dreamplatform.forum.bean.TopicContentBean;
import it.dreamplatform.forum.entities.Topic;
import it.dreamplatform.forum.bean.TopicBean;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class do different mappings between a Topic entity to its corresponding bean and vice versa.
 */
public class TopicMapper {
    @Inject
    DiscussionMapper discussionMapper;

    /**
     * This function creates a TopicBean and call another function to maps the values.
     * @param entity is the entity of the DB.
     * @return the corresponding TopicBean just created.
     */
    public TopicBean mapEntityToBean(Topic entity){
        return mapEntityToBean(entity, new TopicBean());
    }

    /**
     * This function sets the values of a TopicBean using the values retrieve from a topic entity.
     * @param entity is the entity of the DB.
     * @param bean is the object into which the function will set all the values.
     * @return the corresponding TopicBean or null if the entity is null.
     */
    public TopicBean mapEntityToBean(Topic entity, TopicBean bean) {
        if (entity == null) {return null;}
        bean.setTopicId(entity.getTopicId());
        bean.setTitle(entity.getTitle());
        bean.setTimestamp(entity.getTimestamp());
        return bean;
    }

    /**
     * This function creates a new List and call another function to map the values from the entities inside it.
     * @param entities are the Topic entities from which the values will be retrieved.
     * @return the corresponding List of TopicBeans just created.
     */
    public List<TopicBean> mapEntityListToBeanList(List<Topic> entities){
        return mapEntityListToBeanList(entities, new ArrayList<>());
    }

    /**
     * This function maps every Topic entity into a List of TopicBean.
     * @param entities is the List of Topic entities of the DB.
     * @param beans is the List of object into which the entities will be mapped.
     * @return the corresponding List of TopicBean or null if the List of entities is null.
     */
    public List<TopicBean> mapEntityListToBeanList(List<Topic> entities, List<TopicBean> beans){
        if (entities.isEmpty()) {return Collections.emptyList();}
        for (Topic entity: entities) {
            beans.add(mapEntityToBean(entity, new TopicBean()));
        }
        return beans;
    }

    /**
     * This function creates a Topic entity and call another function to maps the values.
     * @param bean is the TopicBean from which the value will be retrieved.
     * @return the corresponding Discussion entity just created.
     */
    public Topic mapBeanToEntity(TopicBean bean){
        return mapBeanToEntity(new Topic(), bean);
    }

    /**
     * This function sets the values of a Topic entity using the values retrieve from a TopicBean.
     * @param entity is the entity of the DB.
     * @param bean is the object from which the values will be retrieved.
     * @return the corresponding Topic entity or null if the Bean is null.
     */
    public Topic mapBeanToEntity(Topic entity, TopicBean bean){
        entity.setTopicId(bean.getTopicId());
        entity.setTitle(bean.getTitle());
        entity.setTimestamp(bean.getTimestamp());
        return entity;
    }

    /**
     * This function creates a TopicContentBean and call another function to maps the values.
     * @param entity is the entity of the DB.
     * @return the corresponding TopicContentBean just created.
     */
    public TopicContentBean mapEntityToContentBean(Topic entity){
        return mapEntityToContentBean(entity, new TopicContentBean());
    }

    /**
     * This function sets the values of a TopicContentBean using the values retrieve from a Topic entity.
     * @param entity is the entity of the DB.
     * @param bean is the object into which the function will set all the values.
     * @return the corresponding TopicContentBean or null if the entity is null.
     */
    public TopicContentBean mapEntityToContentBean(Topic entity, TopicContentBean bean){
        if (entity == null) {return null;}
        bean.setTopicId(entity.getTopicId());
        bean.setTitle(entity.getTitle());
        bean.setTimestamp(entity.getTimestamp());
        bean.setDiscussions(discussionMapper.mapEntityListToBeanList(entity.getDiscussions()));
        return bean;
    }
}


