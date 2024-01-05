package it.dreamplatform.forum.bean;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;

/**
 * This class represents a User. Those values have been assigned to the User at the first registration on the service.
 * The policyMakerId defines if the User is a Policy maker or a normal User.
 */
public class UserBean implements Serializable {
    private Long userId;
    private String name;
    private String surname;
    private String mail;
    private Date dateOfBirth;
    private String areaOfResidence;
    private String policyMakerID;
    private Timestamp createdAt;

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

    public String getMail() {
        return mail;
    }

    public void setMail(String mail) {
        this.mail = mail;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAreaOfResidence() {
        return areaOfResidence;
    }

    public void setAreaOfResidence(String areaOfResidence) {
        this.areaOfResidence = areaOfResidence;
    }

    public String getPolicyMakerID() {
        return policyMakerID;
    }

    public void setPolicyMakerID(String policyMakerID) {
        this.policyMakerID = policyMakerID;
    }

    public Timestamp getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Timestamp createdAt) {
        this.createdAt = createdAt;
    }
}
