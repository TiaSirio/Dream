package it.dreamplatform.forum.controller;

import it.dreamplatform.forum.bean.PublicUserBean;
import it.dreamplatform.forum.bean.UserBean;
import it.dreamplatform.forum.mapper.UserMapper;
import it.dreamplatform.forum.services.UserService;

import javax.inject.Inject;
import java.util.List;

/**
 * This class contains all the controller used by a User entity or a User bean of every genre.
 */
public class UserController {
    @Inject
    UserMapper userMapper;
    @Inject
    UserService userService;

    @Inject
    public UserController(UserMapper userMapper, UserService userService) {
        this.userMapper = userMapper;
        this.userService = userService;
    }

    /**
     * This function retrieve a given User, given its id.
     * @param userId is the id of the selected user.
     * @return the Bean of the needed user.
     */
    public UserBean getUserById(Long userId) {
        return userMapper.mapEntityToBean(userService.getUserById(userId));
    }

    /**
     * This function retrieve a User, given its email (since the emails are unique this function will always return 1 user).
     * @param email is the email of the User.
     * @return the Bean of the searched User.
     */
    public UserBean searchUser(String email) {
        return userMapper.mapEntityToBean(userService.getUserByMail(email));
    }

    /**
     * This function is used to create a new User.
     * @param userBean is the Bean containing all the information of the User that the function is going to create.
     * @return the id of the User just created.
     */
    public Long createUser(UserBean userBean){
        return userService.createUser(userMapper.mapBeanToEntity(userBean));
    }


    /**
     * This function is used to update a User.
     * @param userBean is the Bean containing all the information of the User that the function is going to update.
     * @return the id of the User just updated.
     */
    public Long updateUser(UserBean userBean){
        return userService.updateUser(userMapper.mapBeanToEntity(userBean));
    }


    /**
     * This function is used to retrieve the List of the most active User in the forum.
     * @param numberOfUser is the number of the top users to be retrieved.
     * @return a List of Bean of the retrieved users.
     */
    public List<PublicUserBean> getMostActiveUsers(int numberOfUser){
        return userMapper.mapEntityListToPublicUserBeanList(userService.getMostActiveUsers(numberOfUser));
    }
}
