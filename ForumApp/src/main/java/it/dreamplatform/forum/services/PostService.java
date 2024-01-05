package it.dreamplatform.forum.services;

import it.dreamplatform.forum.entities.Post;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * This class creates and call the queries to retrieve Post entities from the DB.
 */
@Stateless
public class PostService {
    @PersistenceContext(unitName = "forum-persistence-provider")
    private EntityManager em;

    public PostService(){}

    public PostService(EntityManager em) {
        this.em = em;
    }

    /**
     * This function query the DB to retrieve a List of Post of a given Discussion.
     * @param discussionId is the id of the Discussion.
     * @return a List of Post entities.
     */
    public List<Post> getPostsByDiscussionId (Long discussionId){
        TypedQuery<Post> query = em.createQuery("SELECT p FROM Post p where p.discussion.discussionId = :discussionId" , Post.class);
        query.setParameter("discussionId", discussionId);
        return query.getResultList();
    }

    /**
     * This function query the DB to retrieve a given Post.
     * @param postId is the id of the Post.
     * @return the Post searched.
     */
    public Post getPostById(Long postId){
        return em.find(Post.class, postId);
    }

    /**
     * This function query the DB to retrieve a List of Post that is in pending list (has its status sets to 0).
     * @return the List of Post searched.
     */
    public List<Post> getPendingPosts(){
        TypedQuery<Post> query = em.createQuery("SELECT p FROM Post p WHERE p.status = 0", Post.class);
        return query.getResultList();
    }

    /**
     * This function query the DB to retrieve a List of Post of given a userId.
     * @param creatorId is the id of the creator of the post.
     * @return the List of Post searched.
     */
    public List<Post> getPostsByCreator(Long creatorId) {
        TypedQuery<Post> query = em.createQuery("SELECT p FROM Post p WHERE p.creator.userId =:creatorId", Post.class);
        query.setParameter("creatorId", creatorId);
        return query.getResultList();
    }

    /**
     * This function query the DB, in order to store in it a new Post.
     * @param post is the Post entity.
     * @return the id of the Post just created.
     */
    public Long savePost(Post post) {
        if(post.getPostId() == null){
            em.persist(post);
            em.flush();
            return post.getPostId();
        } else {
            post = em.merge(post);
            em.flush();
            return post.getPostId();
        }
    }

    /**
     * This function query the DB, in order to delete a Post.
     * @param post is the Post entity.
     */
    public void deletePost(Post post){
        if(em.find(Post.class, post.getPostId()) != null){
            if (!em.contains(post)) {
                post = em.merge(post);
            }
            em.remove(post);
            em.flush();
        }
    }
}
