package vos.hoteldemo.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "role_permission")
public class RolePermission {
    @Id
    @Column(name = "role_id")
    private int roleID;

    @Column(name = "permission_id")
    private int permissionID;

    public RolePermission() {
    }

    public RolePermission(int roleID, int permissionID) {
        this.roleID = roleID;
        this.permissionID = permissionID;
    }

    public int getRoleID() {
        return roleID;
    }

    public void setRoleID(int roleID) {
        this.roleID = roleID;
    }

    public int getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(int permissionID) {
        this.permissionID = permissionID;
    }
}
