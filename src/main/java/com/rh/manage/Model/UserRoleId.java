package com.rh.manage.Model;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserRoleId implements Serializable {
    
    @Column(name = "id_user", length = 50)
    private String user;  // Propriété nommée "user" (pas userId)
    
    @Column(name = "id_type")
    private Integer typeUser;  // Propriété nommée "typeUser" (pas typeUserId)
    
    public UserRoleId() {}
    
    public UserRoleId(String user, Integer typeUser) {
        this.user = user;
        this.typeUser = typeUser;
    }
    
    public String getUser() {
        return user;
    }
    
    public void setUser(String user) {
        this.user = user;
    }
    
    public Integer getTypeUser() {
        return typeUser;
    }
    
    public void setTypeUser(Integer typeUser) {
        this.typeUser = typeUser;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRoleId)) return false;
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(user, that.user) && Objects.equals(typeUser, that.typeUser);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(user, typeUser);
    }
}
