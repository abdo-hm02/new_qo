package qoraa.net.modules.permission.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import qoraa.net.common.exception.ApplicationException;
import qoraa.net.modules.permission.entity.Permission;
import qoraa.net.modules.permission.entity.PermissionGroup;
import qoraa.net.modules.permission.repository.PermissionRepository;
import qoraa.net.modules.usergroup.model.UserGroupType;
import qoraa.net.modules.usergroup.repository.UserGroupTypeRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PermissionServiceTest {

    private final String userGroupTypeName = "SYSTEM_ADMIN";

    @InjectMocks
    private PermissionService permissionServiceUnderTest;

    @Mock
    private PermissionRepository mockPermissionRepository;

    @Mock
    private UserGroupTypeRepository userGroupTypeRepository;

    @Test
    void getAllPermissionsTest() {
        final List<Permission> permissions = List.of(new Permission(0L, "name", PermissionGroup.PERMISSION));
        when(mockPermissionRepository.findAllByOrderByCategoryAscNameAsc()).thenReturn(permissions);

        List<Permission> result = permissionServiceUnderTest.getAllPermissions();

        assertThat(result).isEqualTo(permissions);
        verify(mockPermissionRepository, atLeastOnce()).findAllByOrderByCategoryAscNameAsc();
    }

    @Test
    void getUserGroupTypeByNameTest() {
        UserGroupType expectedUserGroupType = new UserGroupType();
        expectedUserGroupType.setName(userGroupTypeName);

        when(userGroupTypeRepository.findByNameOrderByCategoryAscNameAsc(userGroupTypeName))
                .thenReturn(expectedUserGroupType);

        UserGroupType actualUserGroupType = permissionServiceUnderTest.getUserGroupTypeByName(userGroupTypeName);

        assertEquals(expectedUserGroupType, actualUserGroupType);
    }

    @Test
    void getUserGroupTypeByNameThrowWhenNameDoesNotExistTest() {
        when(userGroupTypeRepository.findByNameOrderByCategoryAscNameAsc(userGroupTypeName)).thenReturn(null);

        ApplicationException exception = Assertions.assertThrows(ApplicationException.class,
                () -> permissionServiceUnderTest.getUserGroupTypeByName(userGroupTypeName));

        assertEquals(HttpStatus.NO_CONTENT.value(), exception.getStatusCode());
        assertEquals("User group type not found!", exception.getBody().getDetail());
    }
}

