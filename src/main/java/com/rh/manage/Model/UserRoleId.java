package com.rh.manage.Model;

import java.io.Serializable;
import java.util.Objects;

public class UserRoleId implements Serializable {
    private String idUser;
    private Integer idType;

    public UserRoleId() {}

    public UserRoleId(String idUser, Integer idType) {
        this.idUser = idUser;
        this.idType = idType;
    }

    public String getIdUser() { return idUser; }
    public void setIdUser(String idUser) { this.idUser = idUser; }

    public Integer getIdType() { return idType; }
    public void setIdType(Integer idType) { this.idType = idType; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UserRoleId)) return false;
        UserRoleId that = (UserRoleId) o;
        return Objects.equals(idUser, that.idUser) && Objects.equals(idType, that.idType);
    }

    @Override
    public int hashCode() {
        return Objects.hash(idUser, idType);
    }
}
