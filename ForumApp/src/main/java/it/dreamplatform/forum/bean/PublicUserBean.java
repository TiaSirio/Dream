package it.dreamplatform.forum.bean;

import java.io.Serializable;

/**
 * This class represents the public data available for a User. Those values are retrieved by mapping the User entity of
 * the DB.
 */
public class PublicUserBean implements Serializable {
    private Long userId;
    private String name;
    private String surname;
    private String areaOfResidence;
    private boolean isPolicyMaker;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getAreaOfResidence() {
        return areaOfResidence;
    }

    public void setAreaOfResidence(String areaOfResidence) {
        this.areaOfResidence = areaOfResidence;
    }

    public boolean isPolicyMaker() {
        return isPolicyMaker;
    }

    public void setPolicyMaker(boolean policyMaker) {
        isPolicyMaker = policyMaker;
    }
}
