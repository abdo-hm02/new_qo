package qoraa.net.modules.user.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import qoraa.net.common.auditing.AuditMetadata;
import qoraa.net.common.domain.ActiveEntity;
import qoraa.net.common.domain.Identifiable;
import qoraa.net.modules.usergroup.model.UserGroup;

import java.time.LocalDateTime;
import java.util.Set;

@Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(
        name = "\"user\"",
        indexes = {@Index(name = "idx_user_on_email", columnList = "email")}
)
public class User extends AbstractAggregateRoot<User> implements Identifiable<Long>, ActiveEntity {

    @Id
    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(name = "keycloak_id", unique = true)
    private String keycloakId;

    @Column(name = "first_name", nullable = false)
    private @NotBlank String firstName;

    @Column(name = "last_name", nullable = false)
    private @NotBlank String lastName;

    @Column(name = "email", nullable = false, updatable = false)
    private @NotBlank String email;

    @Column(name = "username", nullable = false, updatable = false)
    private String username;

    @Column(name = "last_login")
    private LocalDateTime lastLogin;

    @Column(name = "active")
    private Boolean active = Boolean.TRUE;

    @Column(name = "is_default")
    private boolean isDefault;

    @OneToMany
    @JoinTable(
            name = "user_group_user",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "user_group_id")
    )
    private Set<UserGroup> userGroups;

    @Embedded
    private AuditMetadata auditMetadata = new AuditMetadata();

    @Override
    public Long getId() {
        return userId;
    }

    @Override
    public String getEntityName() {
        return "super admin user";
    }

    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public boolean isDefault() {
        return isDefault;
    }
}

