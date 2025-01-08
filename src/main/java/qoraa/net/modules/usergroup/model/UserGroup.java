package qoraa.net.modules.usergroup.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import qoraa.net.common.auditing.AuditMetadata;
import qoraa.net.common.domain.DefaultEntity;
import qoraa.net.common.domain.Identifiable;
import qoraa.net.modules.permission.entity.Permission;
import qoraa.net.modules.user.model.User;
import qoraa.net.modules.usergroup.model.enumeration.UserGroupType;

import java.util.Set;


@Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "user_group")
public class UserGroup extends AbstractAggregateRoot<UserGroup> implements Identifiable<Long>, DefaultEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_group_id")
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private UserGroupType type;

    @OneToMany
    @JoinTable(name = "user_group_user", joinColumns = @JoinColumn(name = "user_group_id"), inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> users;

    @OneToMany
    @JoinTable(
            name = "user_group_permission",
            joinColumns = @JoinColumn(name = "user_group_id"), foreignKey = @ForeignKey(name = "fk_user_group_permission_on_user_group"),
            inverseJoinColumns = @JoinColumn(name = "permission_id"), inverseForeignKey = @ForeignKey(name = "fk_user_group_permission_on_permission")
    )
    private Set<Permission> permissions;

    @Column(name = "is_default")
    private boolean isDefault;

    @Embedded
    private AuditMetadata auditMetadata = new AuditMetadata();

    @Override
    public String getEntityName() {
        return "super admin user group";
    }

    @Override
    public Long getId() {
        return id;
    }
}
