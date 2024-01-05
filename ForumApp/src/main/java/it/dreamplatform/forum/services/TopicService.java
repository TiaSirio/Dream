package it.dreamplatform.forum.services;

import it.dreamplatform.forum.entities.Topic;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * This class creates and call the queries to retrieve Topic entities from the DB.
 */
@Stateless
public class TopicService {
    @PersistenceContext(unitName = "forum-persistence-provider")
    private EntityManager em;

    public TopicService(){}

    public TopicService(EntityManager em){
        this.em = em;
    }

    /**
     * This function query the DB to retrieve all the Topic entities stored.
     * @return a List of Topic entities.
     */
    public List<Topic> getAllTopics() {
        TypedQuery<Topic> query = em.createQuery("SELECT t FROM Topic t", Topic.class);
        return query.getResultList();
    }

    /**
     * This function query the DB to retrieve a given Topic.
     * @param topicId is the id of the Topic.
     * @return the Topic searched.
     */
    public Topic getTopicById(Long topicId){
        return em.find(Topic.class, topicId);
    }

    /**
     * This function query the DB to retrieve a List of Topic given a title.
     * @param title is the title used.
     * @return the List of Discussion searched.
     */
    public List<Topic> getTopicByTitle(String title){
        TypedQuery<Topic> query = em.createQuery("SELECT t FROM Topic t WHERE t.title =:title", Topic.class);
        query.setParameter("title", title);
        return query.getResultList();
    }

    /**
     * This function query the DB, in order to store in it a new Topic.
     * @param title is the title used for the new Topic.
     * @return the id of the Topic just created.
     */
    public Long createTopic(String title){
        Topic c = new Topic();
        c.setTitle(title);
        em.persist(c);
        em.flush();
        return c.getTopicId();
    }

    /**
     * This function query the DB, in order to store the modifies applied to a Topic.
     * @param topic is the Topic entity.
     */
    public void saveTopic(Topic topic) {
        if(topic.getTopicId() == null){
            em.persist(topic);
            em.flush();
        } else {
            topic = em.merge(topic);
            em.flush();
        }
    }

    /**
     * This function query the DB, in order to delete a Topic.
     * @param topic is the Topic entity.
     */
    public void deleteTopic(Topic topic){
        if(em.contains(topic)){
            em.remove(topic);
            em.flush();
        } else{
            em.merge(topic);
            em.flush();
        }
    }
}
