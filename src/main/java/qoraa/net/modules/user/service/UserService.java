package qoraa.net.modules.user.service;

import jakarta.validation.constraints.NotBlank;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import qoraa.net.common.auditing.AuditMetadataHelper;
import qoraa.net.common.exception.ApplicationException;
import qoraa.net.common.service.DefaultEntityService;
import qoraa.net.modules.user.model.User;
import qoraa.net.modules.user.model.UserSearchCommand;
import qoraa.net.modules.user.repository.UserRepository;
import qoraa.net.modules.user.specification.UserJpaSpecifications;

import java.util.List;
import java.util.Optional;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static qoraa.net.common.exception.ApplicationException.NOT_FOUND_EXCEPTION;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final DefaultEntityService defaultEntityService;
    private final AuditMetadataHelper auditMetadataHelper;
    private final KeycloakUserService keycloakUserService;

    public User findByIdOrThrow(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> ApplicationException.builder(NOT_FOUND.value()).detail("User not found.").build());
    }

    public void activateDeactivateUser(String username) {
        userRepository.findByUsername(username).map(value -> {
            userRepository.save(changeUserStatus(value));
            return value;
        }).orElseThrow(() -> ApplicationException.builder(HttpStatus.NOT_FOUND.value()).build());
    }

    public User changeUserStatus(User user) {
	user.setActive(!user.getActive());
	return user;
    }

    public boolean isEmailExists(@NotBlank String email) {
        return userRepository.findUserByEmail(email).isPresent();
    }

    public Optional<User> findUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Optional<User> getCurrentUser(String email) {
        return userRepository.findUserByEmail(email);
    }

    public void saveUser(User user) {
        userRepository.save(user);
    }

    public void createUser(User user, String password) {
        // Create user in Keycloak first
        String keycloakUserId = keycloakUserService.createKeycloakUser(
                user.getUsername(),
                password,
                user.getEmail(),
                user.getFirstName(),
                user.getLastName()
        );

        // Set the Keycloak ID in our user entity
        user.setKeycloakId(keycloakUserId);

        saveUser(user);
        log.info("A new user '{}' is created", user);
    }

    public void updateUser(User user) {
        saveUser(user);
        log.info("User '{}' is updated", user);
    }

    public void deleteUser(Long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        if (user.isDefault()) {
            throw ApplicationException.builder(HttpStatus.FORBIDDEN.value())
                    .detail("You cannot delete the default system admin user.").build();
        }
        userRepository.delete(user);
        log.info("User with Id '{}' deleted successfully", userId);
    }

    public void checkIfDefaultUser(User user) {
        if (user.isDefault()) {
            throw ApplicationException.builder(HttpStatus.UNAUTHORIZED.value())
                    .detail("You cannot edit the default system admin user.").build();
        }
    }

    public Page<User> getUsers(UserSearchCommand userSearchCommand, Pageable pageable) {
        Specification<User> userSpecification = UserJpaSpecifications.search(userSearchCommand);
        pageable = auditMetadataHelper.applyAuditMetadataSort(pageable);
        return userRepository.findAll(userSpecification, pageable);
    }

    public List<String> getGroupTypesByCurrentUser(String name) {
        User user = getCurrentUser(name).orElseThrow(NOT_FOUND_EXCEPTION);
        return user.getUserGroups().stream().map(userGroup -> userGroup.getType().name()).toList();
    }

    public void isDeactivationDefaultUser(User user) {
        defaultEntityService.isDeactivationDefault(user);
    }
}
