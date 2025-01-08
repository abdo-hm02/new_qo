package qoraa.net.modules.permission.mapper;

import org.apache.commons.collections4.CollectionUtils;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import qoraa.net.modules.permission.dto.PermissionDto;
import qoraa.net.modules.permission.dto.UserGroupTypePermissionDto;
import qoraa.net.modules.permission.entity.Permission;
import qoraa.net.modules.permission.entity.PermissionGroup;
import qoraa.net.modules.usergroup.model.UserGroupTypePermission;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Mapper(componentModel = "spring")
public interface PermissionMapper {

    @InheritInverseConfiguration(name = "toPermission")
    @Mapping(target = "ticket", source = "id")
    PermissionDto toDto(Permission permission);

    default Map<PermissionGroup, List<PermissionDto>> toDtos(List<Permission> permissions) {
	if (CollectionUtils.isEmpty(permissions)) {
	    return Map.of();
	}
	var permissionsByGroup = new LinkedHashMap<PermissionGroup, List<PermissionDto>>(permissions.size());
	for (Permission permission : permissions) {
	    PermissionDto permissionDto = toDto(permission);
	    permissionsByGroup.computeIfAbsent(permissionDto.category(), k -> new ArrayList<>()).add(permissionDto);
	}
	return permissionsByGroup;
    }

    Permission toPermission(PermissionDto permissionDto);

    default Collection<Permission> toPermissions(Collection<PermissionDto> dtos) {
	if (CollectionUtils.isEmpty(dtos)) {
	    return Set.of();
	}
	var permissions = new HashSet<Permission>(dtos.size());
	for (PermissionDto dto : dtos) {
	    permissions.add(toPermission(dto));
	}
	return permissions;
    }

    @Mapping(source = "permission.name", target = "name")
    @Mapping(source = "permission.category", target = "category")
    @Mapping(source = "permission.id", target = "ticket")
    UserGroupTypePermissionDto toUserGroupTypePermissionDto(UserGroupTypePermission userGroupTypePermission);
}
