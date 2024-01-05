package it.dreamplatform.forum.mapper;

import it.dreamplatform.forum.entities.Discussion;
import it.dreamplatform.forum.entities.Post;
import it.dreamplatform.forum.bean.PostBean;
import it.dreamplatform.forum.services.DiscussionService;
import it.dreamplatform.forum.services.PostService;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class do different mappings between a Post entity to its corresponding bean and vice versa.
 */
public class PostMapper {
    @Inject
    UserMapper userMapper;
    @Inject
    DiscussionService discussionService;

    @Inject
    public PostMapper(UserMapper userMapper, DiscussionService discussionService) {
        this.userMapper = userMapper;
        this.discussionService = discussionService;
    }

    /**
     * This function creates a PostBean and call another function to maps the values.
     * @param entity is the entity of the DB.
     * @return the corresponding Bean just created.
     */
    public PostBean mapEntityToBean(Post entity){
        return mapEntityToBean(entity, new PostBean());
    }

    /**
     * This function sets the values of a PostBean using the values retrieve from a post entity.
     * @param entity is the entity of the DB.
     * @param bean is the object into which the function will set all the values.
     * @return the corresponding PostBean or null if the entity is null.
     */
    public PostBean mapEntityToBean(Post entity, PostBean bean){
        if (entity == null) {return null;}
        bean.setPostId(entity.getPostId());
        bean.setText(entity.getText());
        bean.setTimestamp(entity.getTimestamp());
        bean.setStatus(entity.getStatus());
        bean.setCreator(userMapper.mapEntityToPublicBean(entity.getCreator()));
        bean.setDiscussionId(entity.getDiscussion().getDiscussionId());
        return bean;
    }

    /**
     * This function creates a Post entity and call another function to maps the values.
     * @param bean is the PostBean from which the value will be retrieved.
     * @return the corresponding Post entity just created.
     */
    public Post mapBeanToEntity(PostBean bean){
        return mapBeanToEntity(new Post(), bean);
    }

    /**
     * This function sets the values of a Post entity using the values retrieve from a PostBean.
     * @param entity is the entity of the DB.
     * @param bean is the object from which the values will be retrieved.
     * @return the corresponding Post or null if the Bean is null.
     */
    public Post mapBeanToEntity(Post entity, PostBean bean){
        if (bean == null) {return null;}
        entity.setPostId(bean.getPostId());
        entity.setText(bean.getText());
        entity.setTimestamp(bean.getTimestamp());
        entity.setStatus(bean.getStatus());
        entity.setCreator(userMapper.mapPublicBeanToEntity(bean.getCreator()));
        Discussion discussion = discussionService.getDiscussionById(bean.getDiscussionId());
        entity.setDiscussion(discussion);
        return entity;
    }

    /**
     * This function creates a new List and call another function to map the values from the entities inside it.
     * @param entities are the Discussion entities from which the values will be retrieved.
     * @return the corresponding List of PostBean just created.
     */
    public List<PostBean> mapEntityListToBeanList(List<Post> entities){
        return mapEntityListToBeanList(entities, new ArrayList<>());
    }

    /**
     * This function maps every Post entity into a List of PostBean.
     * @param entities is the List of Post entities of the DB.
     * @param beans is the List of object into which the entities will be mapped.
     * @return the corresponding List of PostBean or null if the List of entities is null.
     */
    public List<PostBean> mapEntityListToBeanList(List<Post> entities, List<PostBean> beans){
        if (entities.isEmpty()) {return Collections.emptyList();}
        entities.forEach(e -> {
            beans.add(mapEntityToBean(e));
        });
        return beans;
    }

    /**
     * This function creates a new List and call another function to map the values from the beans inside it.
     * @param beans is the List of Post from which the values will be retrieved.
     * @return the corresponding List of Post entities just created.
     */
    public List<Post> mapBeanListToEntityList(List<PostBean> beans){
        return mapBeanListToEntityList(beans, new ArrayList<>());
    }

    /**
     * This function maps every PostBean into a List of Post entity.
     * @param entities is the List of entities into which the beans will be mapped.
     * @param beans is the List of beans.
     * @return the corresponding List of Post entities or null if the List of entities is null.
     */
    public List<Post> mapBeanListToEntityList(List<PostBean> beans, List<Post> entities){
        if (beans.isEmpty()) {return Collections.emptyList();}
        beans.forEach(b -> {
            entities.add(mapBeanToEntity(b));
        });
        return entities;
    }
}
