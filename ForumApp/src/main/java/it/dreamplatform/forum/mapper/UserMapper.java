package it.dreamplatform.forum.mapper;

import it.dreamplatform.forum.bean.PublicUserBean;
import it.dreamplatform.forum.entities.User;
import it.dreamplatform.forum.bean.UserBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This class do different mappings between a User entity to its corresponding bean and vice versa.
 */
public class UserMapper {

    /**
     * This function creates a UserBean and call another function to maps the values.
     * @param entity is the entity of the DB.
     * @return the corresponding UserBean just created.
     */
    public UserBean mapEntityToBean(User entity){
        return mapEntityToBean(entity, new UserBean());
    }

    /**
     * This function sets the values of a UserBean using the values retrieve from a User entity.
     * @param entity is the entity of the DB.
     * @param bean is the object into which the function will set all the values.
     * @return the corresponding UserBean or null if the entity is null.
     */
    public UserBean mapEntityToBean(User entity, UserBean bean){
        if (entity == null) {return null;}
        bean.setUserId(entity.getUserId());
        bean.setName(entity.getName());
        bean.setSurname(entity.getSurname());
        bean.setMail(entity.getMail());
        bean.setDateOfBirth(entity.getDateOfBirth());
        bean.setAreaOfResidence(entity.getAreaOfResidence());
        bean.setPolicyMakerID(entity.getPolicyMakerID());
        bean.setCreatedAt(entity.getCreatedAt());
        return bean;
    }

    /**
     * This function creates a new List and call another function to map the values from the entities inside it.
     * @param entities are the User entities from which the values will be retrieved.
     * @return the corresponding List of PublicUserBean just created.
     */
    public List<PublicUserBean> mapEntityListToPublicUserBeanList(List<User> entities){
        return mapEntityListToPublicUserBeanList(entities, new ArrayList<>());
    }

    /**
     * This function maps every User entity into a PublicUserBean and then return the complete List.
     * @param entities is the List of User entities of the DB.
     * @param beans is the List of object into which the entities will be mapped.
     * @return the corresponding List of PublicUserBean or null if the List of entities is null.
     */
    public List<PublicUserBean> mapEntityListToPublicUserBeanList(List<User> entities, List<PublicUserBean> beans){
        if (entities.isEmpty()) {return Collections.emptyList();}
        entities.forEach(e -> {
            beans.add(mapEntityToPublicBean(e));
        });
        return beans;
    }

    /**
     * This function creates a User entity and call another function to maps the values.
     * @param bean is the UserBean from which the value will be retrieved.
     * @return the corresponding User entity just created.
     */
    public User mapBeanToEntity(UserBean bean){
        return mapBeanToEntity(new User(), bean);
    }

    /**
     * This function sets the values of a User entity using the values retrieve from a UserBean.
     * @param entity is the entity of the DB.
     * @param bean is the object from which the values will be retrieved.
     * @return the corresponding User entity or null if the Bean is null.
     */
    public User mapBeanToEntity(User entity, UserBean bean){
        if (bean == null) {return null;}
        entity.setUserId(bean.getUserId());
        entity.setName(bean.getName());
        entity.setSurname(bean.getSurname());
        entity.setMail(bean.getMail());
        entity.setDateOfBirth(bean.getDateOfBirth());
        entity.setAreaOfResidence(bean.getAreaOfResidence());
        entity.setPolicyMakerID(bean.getPolicyMakerID());
        entity.setCreatedAt(bean.getCreatedAt());
        return entity;
    }

    /**
     * This function creates a PublicUserBean and call another function to maps the values.
     * @param user is the entity of the DB.
     * @return the corresponding PublicUserBean just created.
     */
    public PublicUserBean mapEntityToPublicBean(User user) {
        return mapEntityToPublicBean(user, new PublicUserBean());
    }

    /**
     * This function sets the values of a PublicUserBean using the values retrieve from a User entity.
     * @param entity is the entity of the DB.
     * @param bean is the object into which the function will set all the values.
     * @return the corresponding PublicUserBean or null if the entity is null.
     */
    public PublicUserBean mapEntityToPublicBean(User entity, PublicUserBean bean) {
        if (entity == null) {return null;}
        bean.setUserId(entity.getUserId());
        bean.setName(entity.getName());
        bean.setSurname(entity.getSurname());
        bean.setPolicyMaker(entity.getPolicyMakerID() != null);
        bean.setAreaOfResidence(entity.getAreaOfResidence());
        return bean;
    }

    /**
     * This function creates a User entity and call another function to maps the values.
     * @param bean is the PublicUserBean from which the value will be retrieved.
     * @return the corresponding User entity just created.
     */
    public User mapPublicBeanToEntity(PublicUserBean bean){
        return mapPublicBeanToEntity(bean , new User());
    }

    /**
     * This function sets the values of a User entity using the values retrieve from a PublicUserBean.
     * @param entity is the entity of the DB.
     * @param bean is the object from which the values will be retrieved.
     * @return the corresponding User entity or null if the Bean is null.
     */
    public User mapPublicBeanToEntity(PublicUserBean bean, User entity){
        if (bean == null) {return null;}
        entity.setUserId(bean.getUserId());
        entity.setName(bean.getName());
        entity.setSurname(bean.getSurname());
        entity.setAreaOfResidence(bean.getAreaOfResidence());
        return entity;
    }
}
