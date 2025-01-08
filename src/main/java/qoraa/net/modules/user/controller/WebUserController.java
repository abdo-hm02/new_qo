package qoraa.net.modules.user.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import qoraa.net.common.exception.ApplicationException;
import qoraa.net.common.ticket.Ticket;
import qoraa.net.modules.user.controller.model.dto.UserDto;
import qoraa.net.modules.user.controller.model.request.CreateUserRequest;
import qoraa.net.modules.user.controller.model.request.UpdateUserRequest;
import qoraa.net.modules.user.mapper.UserMapper;
import qoraa.net.modules.user.model.User;
import qoraa.net.modules.user.model.UserSearchCommand;
import qoraa.net.modules.user.service.UserService;

import java.util.Locale;
import java.util.Optional;

import static org.springframework.http.HttpStatus.CONFLICT;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/webapi/v0/users")
@Tag(name = "User operations", description = "The User Web API")
public class WebUserController {

    private final MessageSource messageSource;
    private final UserMapper userMapper;
    private final UserService userService;

    private String getMessage(String key) {
        return messageSource.getMessage(key, null, Locale.getDefault());
    }

    @Operation(summary = "Check if email exists", description = "Check if the provided email exists in the system.")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Email is available"),
            @ApiResponse(responseCode = "400", description = "The required parameter is missing or invalid"),
            @ApiResponse(responseCode = "401", description = "Unauthorized! Requires authentication"),
            @ApiResponse(responseCode = "403", description = "Access denied", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "404", description = "User not found with the provided username", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))})})
    @RequestMapping(params = "email", method = RequestMethod.HEAD)
    @PreAuthorize("hasAuthority('User.View')")
    public ResponseEntity<Boolean> isEmailExists(@RequestParam @Valid String email) {
        return userService.isEmailExists(email) ? new ResponseEntity<>(OK) : new ResponseEntity<>(NOT_FOUND);
    }

    @Operation(summary = "Check if username unique", description = "Check if the provided username is unique in the system")
    @ApiResponses(value = { @ApiResponse(responseCode = "302", description = "User Is Unique"),
            @ApiResponse(responseCode = "400", description = "The required parameter is missing or invalid"),
            @ApiResponse(responseCode = "401", description = "Unauthorized! Requires authentication", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
            @ApiResponse(responseCode = "403", description = "Access denied", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
            @ApiResponse(responseCode = "404", description = "User not found with the provided username", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }) })
    @RequestMapping(params = "username", method = RequestMethod.HEAD)
    @PreAuthorize("hasAuthority('User.View')")
    ResponseEntity<Void> isUsernameUnique(@RequestParam String username) {
        Optional<User> user = userService.findUsername(username);
        if (user.isEmpty()) {
            String message = messageSource.getMessage("users.username.is.not.found", null, Locale.getDefault());
            log.debug(message);
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String message = messageSource.getMessage("users.username.is.found", null, Locale.getDefault());
        log.debug(message);
        return new ResponseEntity<>(OK);
    }

    @Operation(summary = "Activate or Deactivate User", description = "Activate or deactivate a user based on the provided username")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "User status updated successfully"),
            @ApiResponse(responseCode = "400", description = "The required parameter is missing or invalid"),
            @ApiResponse(responseCode = "401", description = "Unauthorized! Requires authentication"),
            @ApiResponse(responseCode = "403", description = "Access denied", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "404", description = "User not found with the provided username", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))}),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true))})})
    @PutMapping(value = "/{username}/activate-deactivate")
    @PreAuthorize("hasAnyAuthority('User.Activate/Deactivate','User.View')")
    ResponseEntity<Void> activateDeactivateUserStatus(@PathVariable String username) {
        userService.activateDeactivateUser(username);
        log.debug(getMessage("users.status.updated.successfully"));
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Delete User", description = "Delete a user based on the provided ticket")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User deleted successfully"),
            @ApiResponse(responseCode = "400", description = "The required parameter is missing or invalid"),
            @ApiResponse(responseCode = "401", description = "Unauthorized! Requires authentication"),
            @ApiResponse(responseCode = "403", description = "Access denied", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
            @ApiResponse(responseCode = "404", description = "User not found with the provided ticket", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }) })
    @DeleteMapping(params = "ticket")
    @PreAuthorize("hasAnyAuthority('User.Delete','User.View')")
    ResponseEntity<String> deleteUser(Ticket ticket) {
        userService.deleteUser(ticket.getId());
        return new ResponseEntity<>(getMessage("users.deleted.successfully"), OK);
    }

    @Operation(summary = "Get Current User", description = "Get the current user authenticated")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = UserDto.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized! Requires authentication"),
            @ApiResponse(responseCode = "404", description = "User not found", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }) })
    @GetMapping(value = "me", produces = APPLICATION_JSON_VALUE)
    ResponseEntity<UserDto> getCurrentUser(Authentication authentication) {
        if (authentication == null) {
            log.debug("The user is not authenticated");
            return new ResponseEntity<>(UNAUTHORIZED);
        } else {
            UserDto user = userService.getCurrentUser(authentication.getName()).map(userMapper::toDto).orElse(null);
            if (user == null) {
                throw new ApplicationException(ApplicationException.builder(NOT_FOUND.value())
                        .detail(messageSource.getMessage("users.is.not.found", null, Locale.getDefault())));
            }
            if (!user.isActive()) {
                throw new ApplicationException(ApplicationException.builder(HttpStatus.FORBIDDEN.value())
                        .detail(messageSource.getMessage("users.is.inactive", null, Locale.getDefault())));
            }
            log.debug("The username is authenticated");
            return new ResponseEntity<>(user, OK);
        }
    }

    @Operation(summary = "Create User", description = "Create a new user")
    @ApiResponses(value = { @ApiResponse(responseCode = "201", description = "User created successfully"),
            @ApiResponse(responseCode = "400", description = "The required parameter is missing or invalid"),
            @ApiResponse(responseCode = "401", description = "Unauthorized! Requires authentication"),
            @ApiResponse(responseCode = "403", description = "Access denied", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }) })
    @PostMapping(consumes = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('User.Create','User.View')")
    ResponseEntity<String> createUser(@RequestBody CreateUserRequest createUserRequest) {
        if (userService.findUsername(createUserRequest.getUsername()).isPresent()) {
            return new ResponseEntity<>(getMessage("username.exists"), CONFLICT);
        }
        var user = userMapper.fromDto(createUserRequest);
        userService.createUser(user, createUserRequest.getPassword());
        return new ResponseEntity<>(getMessage("users.created.successfully"), HttpStatus.CREATED);
    }

    @Operation(summary = "Update an existing user", description = "Update an existing user in the system")
    @ApiResponses(value = { @ApiResponse(responseCode = "200", description = "User updated successfully"),
            @ApiResponse(responseCode = "400", description = "The required parameter is missing or invalid"),
            @ApiResponse(responseCode = "401", description = "Unauthorized! Requires authentication", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
            @ApiResponse(responseCode = "403", description = "Access denied", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
            @ApiResponse(responseCode = "404", description = "User not found", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }) })
    @PutMapping(consumes = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAnyAuthority('User.Edit','User.View')")
    ResponseEntity<Void> updateUser(@RequestBody final UpdateUserRequest updateUserRequest) {
        User user = userService.findByIdOrThrow(updateUserRequest.getTicket());
        userService.checkIfDefaultUser(user);
        userService.isDeactivationDefaultUser(user);
        userMapper.update(user, updateUserRequest);
        userService.updateUser(user);
        return new ResponseEntity<>(OK);
    }

    @Operation(summary = "Get Users List", description = "Get a list of users based on the provided search command")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "List of users matching the search criteria", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(implementation = PageImpl.class)) }),
            @ApiResponse(responseCode = "401", description = "Unauthorized! Requires authentication"),
            @ApiResponse(responseCode = "403", description = "Access denied", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }),
            @ApiResponse(responseCode = "500", description = "Internal server error", content = {
                    @Content(mediaType = APPLICATION_JSON_VALUE, schema = @Schema(hidden = true)) }) })
    @GetMapping(produces = APPLICATION_JSON_VALUE)
    @PreAuthorize("hasAuthority('User.View')")
    Page<UserDto> getUsers(UserSearchCommand userSearchCommand, Pageable pageable) {
        return userService.getUsers(userSearchCommand, pageable)
                .map(userMapper::toDto);
    }
}
