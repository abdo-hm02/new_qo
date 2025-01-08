package qoraa.net.modules.usergroup.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
@Setter @Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserGroupTypePermissionKey implements Serializable {

    @Column(name = "user_group_type_name")
    private String userGroupTypeName;

    @Column(name = "permission_id")
    private Long permissionId;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserGroupTypePermissionKey that = (UserGroupTypePermissionKey) o;
        return Objects.equals(userGroupTypeName, that.userGroupTypeName) &&
                Objects.equals(permissionId, that.permissionId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userGroupTypeName, permissionId);
    }
}
