package qoraa.net.modules.usergroup.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import qoraa.net.common.exception.ApplicationException;
import qoraa.net.common.ticket.Ticket;
import qoraa.net.modules.usergroup.controller.model.dto.UserGroupDetailsDto;
import qoraa.net.modules.usergroup.controller.model.dto.UserGroupDto;
import qoraa.net.modules.usergroup.controller.model.dto.UserGroupPageDto;
import qoraa.net.modules.usergroup.controller.model.request.EditUserGroupRequest;
import qoraa.net.modules.usergroup.controller.model.request.UserGroupRequest;
import qoraa.net.modules.usergroup.mapper.UserGroupMapper;
import qoraa.net.modules.usergroup.model.AllUserGroupLists;
import qoraa.net.modules.usergroup.model.UserGroup;
import qoraa.net.modules.usergroup.model.UserGroupSearchRequest;
import qoraa.net.modules.usergroup.model.enumeration.UserGroupType;
import qoraa.net.modules.usergroup.service.UserGroupService;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/webapi/v0/user-group")
@Tag(name = "User group operations", description = "The User group Web API")
@PreAuthorize("hasAuthority('UserGroup.View')")
public class WebUserGroupController {

    private final MessageSource messageSource;
    private final UserGroupMapper userGroupMapper;
    private final UserGroupService userGroupService;

    private String getMessage(String key) {
		return messageSource.getMessage(key, null, Locale.getDefault());
	}

    @Operation(summary = "Create user group", description = "Create a new user group")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "User group created successfully", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
	    @ApiResponse(responseCode = "401", description = "Unauthorized! Requires authentication", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
	    @ApiResponse(responseCode = "403", description = "Access denied", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
	    @ApiResponse(responseCode = "500", description = "Internal server error", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }) })
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('UserGroup.Create')")
    ResponseEntity<Void> createUserGroup(@RequestBody UserGroupRequest createUserGroupRequest) {
	AllUserGroupLists allLists = userGroupService.getAllUserGroupLists(createUserGroupRequest);
	UserGroup userGroupToCreate = userGroupMapper.fromDto(createUserGroupRequest, allLists);
	userGroupService.createUserGroup(userGroupToCreate);
	return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @Operation(summary = "Delete User Group", description = "Delete a user group based on the provided ticket")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User Group deleted successfully"),
	    @ApiResponse(responseCode = "400", description = "The required parameter is missing or invalid"),
	    @ApiResponse(responseCode = "401", description = "Unauthorized! Requires authentication"),
	    @ApiResponse(responseCode = "403", description = "Access denied", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
	    @ApiResponse(responseCode = "404", description = "User Group not found with the provided ticket", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
	    @ApiResponse(responseCode = "500", description = "Internal server error", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }) })
    @DeleteMapping(params = "ticket")
    @PreAuthorize("hasAuthority('UserGroup.Delete')")
    ResponseEntity<String> deleteUserGroup(Ticket ticket) {
	UserGroup userGroupToDelete = userGroupService.findByIdOrThrow(ticket.getId());
	userGroupService.deleteUserGroup(userGroupToDelete);
	return new ResponseEntity<>(getMessage("users-groups.deleted.successfully"), OK);
    }

    @Operation(summary = "Get user group by ticket", description = "Get details of a user group by ticket")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Returns a user group details by ticket", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserGroupDetailsDto.class)) }),
	    @ApiResponse(responseCode = "401", description = "Unauthorized! Requires authentication", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
	    @ApiResponse(responseCode = "403", description = "Access denied", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
	    @ApiResponse(responseCode = "500", description = "Internal server error", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }) })
    @GetMapping(produces = APPLICATION_JSON_VALUE, params = "ticket")
    UserGroupDetailsDto getUserGroupDetails(Ticket ticket) {
	UserGroup userGroupById = userGroupService.findByIdOrThrow(ticket.getId());
	return userGroupMapper.toDetailsDto(userGroupById);
    }

    @Operation(summary = "Get user group page", description = "Get user group based on user group request for dashboard")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Returns a page of user group using a user group request", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
	    @ApiResponse(responseCode = "401", description = "Unauthorized! Requires authentication", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
	    @ApiResponse(responseCode = "403", description = "Access denied", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
	    @ApiResponse(responseCode = "500", description = "Internal server error", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }) })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    Page<UserGroupPageDto> getUserGroupPage(UserGroupSearchRequest searchRequest, Pageable pageable) {
	if (StringUtils.isNotBlank(searchRequest.getUserTicket())) {
	    searchRequest.setUserTicket(String.valueOf(userGroupMapper.fromTicket(searchRequest.getUserTicket())));
	}
	return userGroupService.getUserGroupPage(searchRequest, pageable)
		.map(userGroupMapper::toUserGroupPageDto);
    }

    @Operation(summary = "Get user groups by type", description = "Get a list of user groups by type")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "Returns a list of user groups", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserGroupDto.class)) }),
	    @ApiResponse(responseCode = "401", description = "Unauthorized! Requires authentication", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
	    @ApiResponse(responseCode = "403", description = "Access denied", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
	    @ApiResponse(responseCode = "500", description = "Internal server error", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }) })
    @GetMapping(value = "by-type", produces = APPLICATION_JSON_VALUE, params = "type")
    List<UserGroupDto> getUserGroupsByType(String type) {
	if (Arrays.stream(UserGroupType.values()).noneMatch(e -> e.name().equals(type))) {
	    throw ApplicationException.builder(HttpStatus.BAD_REQUEST.value()).detail("Invalid Type").build();
	}
	List<UserGroup> userGroupsByType = userGroupService.getUserGroupByType(type);
	return userGroupsByType.stream().map(userGroupMapper::toDto).collect(Collectors.toList());

    }

    @Operation(summary = "Update user group", description = "Update an existing user group")
    @ApiResponses(value = {
	    @ApiResponse(responseCode = "200", description = "User group updated successfully", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
	    @ApiResponse(responseCode = "401", description = "Unauthorized! Requires authentication", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
	    @ApiResponse(responseCode = "403", description = "Access denied", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
	    @ApiResponse(responseCode = "500", description = "Internal server error", content = {
		    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }) })
    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('UserGroup.Edit')")
    ResponseEntity<Void> updateUserGroup(Authentication authentication,
	    @RequestBody EditUserGroupRequest editUserGroupRequest) {
	UserGroup userGroup = userGroupService.findByIdOrThrow(editUserGroupRequest.getTicket());
	userGroupService.isNonEditableDefaultUserGroup(userGroup, authentication.getName());
	AllUserGroupLists allLists = userGroupService.getAllUserGroupLists(editUserGroupRequest);
	UserGroup updatedUserGroup = userGroupMapper.fromDto(editUserGroupRequest, allLists);
	userGroupService.editUserGroup(editUserGroupRequest.getTicket(), updatedUserGroup);
	return new ResponseEntity<>(OK);
    }
}
