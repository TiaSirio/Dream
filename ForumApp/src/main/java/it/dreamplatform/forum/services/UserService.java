package it.dreamplatform.forum.services;

import it.dreamplatform.forum.entities.User;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * This class creates and call the queries to retrieve User entities from the DB.
 */
@Stateless
public class UserService {
    @PersistenceContext(unitName = "forum-persistence-provider")
    private EntityManager em;

    public UserService() {}

    public UserService(EntityManager em) {
        this.em = em;
    }

    /**
     * This function query the DB to retrieve a given User using its id.
     * @param userId is the id of the User.
     * @return the User searched.
     */
    public User getUserById(Long userId){
        return em.find(User.class, userId);
    }

    /**
     * This function query the DB to retrieve a given User using its mail.
     * @param mail is the mail of the User.
     * @return the User searched.
     */
    public User getUserByMail(String mail){
        TypedQuery<User> query = em.createNamedQuery("User.findByMail" , User.class);
        List<User> result = query.setParameter("mail", mail).getResultList();
        if(result.isEmpty()){
            return null;
        }
        return result.get(0);
    }

    /**
     * This function query the DB to retrieve a given User using its policyMakerId.
     * @param policyMakerId is the policyMakerId of the User.
     * @return the User searched.
     */
    public User getUserByPolicyMakerId(String policyMakerId){
        TypedQuery<User> query = em.createQuery("SELECT u FROM User u WHERE u.policyMakerID = :policyMakerId", User.class);
        query.setParameter("policyMakerId", policyMakerId);
        return query.getSingleResult();
    }

    /**
     * This function query the DB, in order to store in it a new User.
     * @param user is the User entity.
     * @return the id of the User just created.
     */
    public Long createUser(User user){
        em.persist(user);
        em.flush();
        return user.getUserId();
    }


    /**
     * This function query the DB in order to update an existing User
     * @param user is the User to update
     * @return the id of the updated user
     */
    public Long updateUser(User user){
        user = em.merge(user);
        em.flush();
        return user.getUserId();
    }


    /**
     * This function query the DB, in order to delete a User.
     * @param user is the User entity.
     */
    public void deleteUser(User user){
        if(em.contains(user)){
            em.remove(user);
            em.flush();
        } else{
            em.merge(user);
            em.flush();
        }
    }

    /**
     * This function query the DB to retrieve the List of User that has created more posts (not including the Policy maker).
     * @param numberOfUser is the number of User we want to retrieve.
     * @return the List of User searched.
     */
    public List<User> getMostActiveUsers(Integer numberOfUser){
        Query query = em.createNativeQuery("SELECT u.*, COUNT(*) AS n_posts FROM Post as p JOIN User as u ON p.creatorId = u.userId WHERE p.status=1 AND u.policyMakerID IS NULL GROUP BY p.creatorId ORDER BY n_posts DESC LIMIT ?", User.class);
        query.setParameter(1,numberOfUser);
        return ((List<User>) query.getResultList());
    }
}
