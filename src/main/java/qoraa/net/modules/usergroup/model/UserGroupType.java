package qoraa.net.modules.usergroup.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.util.List;
import java.util.Objects;


@Entity
@AllArgsConstructor @NoArgsConstructor
@Setter @Getter
@Table(name = "user_group_type")
public class UserGroupType {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "name")
    private String name;

    @Column(name = "description")
    private String description;
    @OneToMany(mappedBy = "userGroupType", fetch = FetchType.LAZY)
    @ToString.Exclude
    @NotEmpty
    private List<UserGroupTypePermission> userGroupTypePermissions;

    @Override
    public boolean equals(Object o) {
	if (this == o)
	    return true;
	if (o == null || getClass() != o.getClass())
	    return false;
	UserGroupType that = (UserGroupType) o;
	return Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
	return Objects.hashCode(name);
    }
}
