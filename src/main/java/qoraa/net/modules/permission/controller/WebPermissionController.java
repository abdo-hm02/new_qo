package qoraa.net.modules.permission.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qoraa.net.modules.permission.dto.PermissionDto;
import qoraa.net.modules.permission.dto.UserGroupTypeAndPermissionsDto;
import qoraa.net.modules.permission.dto.UserGroupTypePermissionDto;
import qoraa.net.modules.permission.entity.PermissionGroup;
import qoraa.net.modules.permission.mapper.PermissionMapper;
import qoraa.net.modules.permission.service.PermissionService;
import qoraa.net.modules.usergroup.model.UserGroupType;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/webapi/v0/permissions")
public class WebPermissionController {

    private final PermissionMapper permissionMapper;
    private final PermissionService permissionService;

    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Map<PermissionGroup, List<PermissionDto>> viewPermissions() {
	return permissionMapper.toDtos(permissionService.getAllPermissions());
    }

    @GetMapping(value = "/{userGroupType}", produces = APPLICATION_JSON_VALUE)
    @Operation(summary = "View Permissions", description = "Get permissions by user group type")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Returns permissions by user group type", useReturnTypeSchema = true),
	    @ApiResponse(responseCode = "400", description = "The required parameter is missing or invalid", content = @Content(schema = @Schema)),
	    @ApiResponse(responseCode = "401", description = "Requires authentication", content = @Content(schema = @Schema)),
	    @ApiResponse(responseCode = "403", description = "Access denied", content = @Content(schema = @Schema)),
	    @ApiResponse(responseCode = "404", description = "User Group Type not found!", content = @Content(schema = @Schema)),
	    @ApiResponse(responseCode = "500", description = "Internal server error", content = @Content(schema = @Schema)) })
    public UserGroupTypeAndPermissionsDto viewPermissionsByUserGroupType(@PathVariable String userGroupType) {
	UserGroupType type = permissionService.getUserGroupTypeByName(userGroupType);

	List<UserGroupTypePermissionDto> permissionsList = Optional.ofNullable(type.getUserGroupTypePermissions())
		.orElse(Collections.emptyList()).stream().map(permissionMapper::toUserGroupTypePermissionDto).toList();

	return UserGroupTypeAndPermissionsDto.builder().userGroupType(type.getName())
		.userGroupTypePermissions(permissionsList).build();
    }
}
