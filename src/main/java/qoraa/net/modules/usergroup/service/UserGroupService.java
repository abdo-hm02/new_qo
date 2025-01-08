package qoraa.net.modules.usergroup.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import qoraa.net.common.auditing.AuditMetadataHelper;
import qoraa.net.common.exception.ApplicationException;
import qoraa.net.common.service.DefaultEntityService;
import qoraa.net.modules.permission.entity.Permission;
import qoraa.net.modules.permission.repository.PermissionRepository;
import qoraa.net.modules.user.model.User;
import qoraa.net.modules.user.repository.UserRepository;
import qoraa.net.modules.user.service.UserService;
import qoraa.net.modules.usergroup.controller.model.request.UserGroupRequest;
import qoraa.net.modules.usergroup.model.AllUserGroupLists;
import qoraa.net.modules.usergroup.model.UserGroup;
import qoraa.net.modules.usergroup.model.UserGroupSearchRequest;
import qoraa.net.modules.usergroup.repository.UserGroupRepository;
import qoraa.net.modules.usergroup.specification.UserGroupJpaSpecifications;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.http.HttpStatus.NOT_FOUND;

@RequiredArgsConstructor
@Service
@Slf4j
public class UserGroupService {

    private final MessageSource messageSource;
    private final PermissionRepository permissionRepository;
    private final UserGroupRepository userGroupRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final DefaultEntityService defaultEntityService;
    private final AuditMetadataHelper auditMetadataHelper;

    public UserGroup findByIdOrThrow(Long id) {
	return userGroupRepository.findById(id)
		.orElseThrow(() -> ApplicationException.builder(NOT_FOUND.value())
			.detail(messageSource.getMessage("users-groups.is.not.found", null, Locale.getDefault()))
			.build());
    }

    @Transactional
    public void createUserGroup(UserGroup userGroup) {
	userGroupRepository.save(userGroup);
	log.debug("Saving user group {}", userGroup);
    }

    public void deleteUserGroup(UserGroup userGroup) {
	if (userGroup.isDefault()) {
	    throw ApplicationException.builder(HttpStatus.FORBIDDEN.value())
		    .detail("You cannot delete the default super admin user group.").build();
	}
	userGroupRepository.delete(userGroup);
	log.debug("Deleting user group {}", userGroup);
    }

    public void editUserGroup(Long ticket, UserGroup updatedUserGroup) {
	updatedUserGroup.setId(ticket);
	saveUserGroup(updatedUserGroup);
	log.debug("Updating user group {}", updatedUserGroup);
    }

    public void saveUserGroup(UserGroup userGroup) {
	userGroupRepository.save(userGroup);
    }

    public Optional<UserGroup> findUserGroupById(Long id) {
	return userGroupRepository.findById(id);
    }

    public AllUserGroupLists getAllUserGroupLists(UserGroupRequest request) {
	List<Permission> permissionList = new ArrayList<>();
	List<User> userList = new ArrayList<>();

	if (isNotEmpty(request.getUserTickets())) {
	    userList = userRepository.findAllById(request.getUserTickets());
	}

	if (isNotEmpty(request.getPermissionTickets())) {
	    permissionList = permissionRepository.findAllById(request.getPermissionTickets());
	}

	return new AllUserGroupLists(permissionList, userList);
    }

    public Page<UserGroup> getUserGroupPage(UserGroupSearchRequest searchRequest, Pageable pageable) {
	Specification<UserGroup> userSpecification = UserGroupJpaSpecifications.searchUserGroup(searchRequest);
	pageable = auditMetadataHelper.applyAuditMetadataSort(pageable);
	return userGroupRepository.findAll(userSpecification, pageable);
    }

    public List<String> getCurrentUserGroupTypes(String name) {
	return userService.getGroupTypesByCurrentUser(name);
    }

    public void isNonEditableDefaultUserGroup(UserGroup userGroup, String username) {
	    defaultEntityService.isNonEditableDefault(userGroup, username, this::getCurrentUserGroupTypes);
    }

    public List<UserGroup> getUserGroupByType(String type) {
	return userGroupRepository.findByType(type);
    }

    public Optional<User> getCurrentUser(String email) {
	return userService.getCurrentUser(email);
    }

}
