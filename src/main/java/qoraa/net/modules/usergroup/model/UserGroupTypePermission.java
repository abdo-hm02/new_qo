package qoraa.net.modules.usergroup.model;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import qoraa.net.modules.permission.entity.Permission;

import java.util.Objects;


@Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "user_group_type_permission")
public class UserGroupTypePermission {

    @EmbeddedId
    private UserGroupTypePermissionKey id;

    @ManyToOne
    @MapsId("userGroupTypeName")
    @JoinColumn(name = "user_group_type_name")
    private UserGroupType userGroupType;

    @ManyToOne
    @MapsId("permissionId")
    @JoinColumn(name = "permission_id")
    private Permission permission;

    @Column(name = "disabled")
    private boolean disabled;

    @Column(name = "preselected")
    private boolean preselected;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGroupTypePermission that = (UserGroupTypePermission) o;
	return preselected == that.preselected && Objects.equals(userGroupType, that.userGroupType)
		&& Objects.equals(permission, that.permission);
    }

    @Override
    public int hashCode() {
	return Objects.hash(userGroupType, permission, preselected);
    }
}
