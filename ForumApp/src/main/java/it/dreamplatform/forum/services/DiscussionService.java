package it.dreamplatform.forum.services;

import it.dreamplatform.forum.entities.Discussion;
import it.dreamplatform.forum.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * This class creates and call the queries to retrieve Discussion entities from the DB.
 */
@Stateless
public class DiscussionService {
    @PersistenceContext(unitName = "forum-persistence-provider")
    private EntityManager em;

    public DiscussionService(){}

    public DiscussionService(EntityManager em){
        this.em = em;
    }

    /**
     * This function query the DB to retrieve a List of Discussion of a given Topic.
     * @param topicId is the id of the Topic.
     * @return a List of Discussion entities.
     */
    public List<Discussion> getDiscussionsByTopicId (Long topicId){
        TypedQuery<Discussion> query = em.createQuery("SELECT d FROM Discussion d WHERE d.topic.topicId = :topicId" , Discussion.class);
        query.setParameter("topicId", topicId);
        return query.getResultList();
    }

    /**
     * This function query the DB to retrieve a given Discussion.
     * @param discussionId is the id of the Discussion.
     * @return the Discussion searched.
     */
    public Discussion getDiscussionById(Long discussionId){
        return em.find(Discussion.class, discussionId);
    }

    /**
     * This function query the DB to retrieve a List of Discussion given a title.
     * @param title is the title used.
     * @return the List of Discussion searched.
     */
    public List<Discussion> getDiscussionByTitle(String title){
        TypedQuery<Discussion> query = em.createQuery("SELECT d FROM Discussion d WHERE d.title =:title", Discussion.class);
        query.setParameter("title", title);
        return query.getResultList();
    }

    /**
     * This function query the DB to retrieve a List of Discussion created by a Policy maker.
     * @param policyMakerId is the id of the Policy maker.
     * @return a List of Discussion.
     */
    public List<Discussion> getDiscussionByPolicyMaker(String policyMakerId){
        Query query = em.createNativeQuery("SELECT d.* FROM Discussion d JOIN Post p1 ON p1.discussionId = d.discussionId JOIN User u ON p1.creatorId = u.userId WHERE p1.postId IN (SELECT MIN(postId) FROM Post p2 GROUP BY p2.discussionId) AND u.policyMakerID = ?", Discussion.class);
        query.setParameter(1, policyMakerId);
        return ((List<Discussion>) query.getResultList());
    }

    /**
     * This function query the DB, in order to store in it a new Discussion.
     * @param discussion is the Discussion entity.
     * @return the id of the Discussion just created.
     */
    public Long saveDiscussion(Discussion discussion) {
        if(discussion.getDiscussionId() == null){
            em.persist(discussion);
            em.flush();
            return discussion.getDiscussionId();
        } else {
            discussion = em.merge(discussion);
            em.flush();
            return discussion.getDiscussionId();
        }
    }

    /**
     * This function query the DB, in order to delete a Discussion.
     * @param discussion is the Discussion entity.
     */
    public void deleteDiscussion(Discussion discussion){
        if(em.find(Discussion.class, discussion.getDiscussionId()) != null){
            if (!em.contains(discussion)) {
                discussion = em.merge(discussion);
            }
            em.remove(discussion);
            em.flush();
        }
    }

    /**
     * This function query the DB to retrieve the List of User that have created a post in a specific discussion.
     * @param discussionId is the id of the Discussion.
     * @return the List of User searched.
     */
    public List<User> getDiscussionFollowers(Long discussionId){
        Query query = em.createNativeQuery("SELECT u.* FROM User AS u JOIN Post p ON p.creatorId = u.userId WHERE p.discussionId = ? GROUP BY p.creatorId", User.class);
        query.setParameter(1, discussionId);
        return ((List<User>) query.getResultList());
    }

    /**
     * This function query the DB to retrieve the List of Discussion with more posts in it.
     * @param numberOfDiscussions is the number of discussion we want to retrieve.
     * @return the List of Discussion searched.
     */
    public List<Discussion> getMostActiveDiscussions(Integer numberOfDiscussions){
        Query query = em.createNativeQuery("SELECT d.*, COUNT(*) AS n_posts FROM Post as p JOIN Discussion as d ON p.discussionId = d.discussionId WHERE p.status=1 GROUP BY p.discussionId ORDER BY n_posts DESC LIMIT ?", Discussion.class);
        query.setParameter(1,numberOfDiscussions);
        return (List<Discussion>) query.getResultList();
    }
}
