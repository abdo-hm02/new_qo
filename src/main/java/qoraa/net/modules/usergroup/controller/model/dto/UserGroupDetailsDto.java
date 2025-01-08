package qoraa.net.modules.usergroup.controller.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import qoraa.net.common.annotation.Ticket;
import qoraa.net.modules.permission.dto.PermissionDto;
import qoraa.net.modules.user.controller.model.dto.UserForUserGroupDetailsDto;
import qoraa.net.modules.usergroup.model.enumeration.UserGroupType;

import java.time.LocalDateTime;
import java.util.Set;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserGroupDetailsDto {
    String createdBy;
    LocalDateTime createdOn;
    String description;
    String modifiedBy;
    LocalDateTime modifiedOn;
    String name;
    @Ticket
    Long ticket;
    UserGroupType type;
    Set<UserForUserGroupDetailsDto> users;
    Set<PermissionDto> permissions;
}
