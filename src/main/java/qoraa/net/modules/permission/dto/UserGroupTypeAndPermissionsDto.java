package qoraa.net.modules.permission.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.util.List;

@Value
@Builder
@AllArgsConstructor
public class UserGroupTypeAndPermissionsDto {
    String userGroupType;
    List<UserGroupTypePermissionDto> userGroupTypePermissions;
}
