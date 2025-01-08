package qoraa.net.modules.permission.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import qoraa.net.common.exception.ApplicationException;
import qoraa.net.modules.permission.entity.Permission;
import qoraa.net.modules.permission.repository.PermissionRepository;
import qoraa.net.modules.usergroup.model.UserGroupType;
import qoraa.net.modules.usergroup.repository.UserGroupTypeRepository;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PermissionService {

    private final PermissionRepository permissionRepository;
    private final UserGroupTypeRepository userGroupTypeJpaRepository;

    public List<Permission> getAllPermissions() {
	return permissionRepository.findAllByOrderByCategoryAscNameAsc();
    }

    public UserGroupType getUserGroupTypeByName(String name) {
	UserGroupType userGroupType = userGroupTypeJpaRepository.findByNameOrderByCategoryAscNameAsc(name);

        if (userGroupType == null) {
            throw ApplicationException.builder(HttpStatus.NO_CONTENT.value()).detail("User group type not found!").build();
        }

        return userGroupType;
    }
}
