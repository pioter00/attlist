package com.root.attendancelist.model;


import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Entity
@Table(name="users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long ID;
    private Long GroupID;
    @Size(min=2, max=30)
    private String firstName;
    @Size(min=2, max=30)
    private String lastName;
    @Email
    private String email;
    @Size(min=4, max=30)
    private String password;
    @NotNull
    @Pattern(regexp = "(student|teacher)")

    private String role;

    public User() {

    }

    public User(Long ID, Long groupID, String firstName, String lastName, String email, String password, String role) {
        this.ID = ID;
        this.GroupID = groupID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public Long getID() {
        return ID;
    }

    public void setID(Long ID) {
        this.ID = ID;
    }

    public Long getGroupID() {
        return GroupID;
    }

    public void setGroupID(Long groupID) {
        GroupID = groupID;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
