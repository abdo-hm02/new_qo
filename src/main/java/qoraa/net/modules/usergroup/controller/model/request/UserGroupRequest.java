package qoraa.net.modules.usergroup.controller.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import qoraa.net.common.annotation.Ticket;
import qoraa.net.modules.usergroup.model.enumeration.UserGroupType;

import java.util.Set;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserGroupRequest {

    @NotBlank(message = "{user.group.name.is.blank}")
    @Size(max = 50, message = "{invalid.user.group.name.max.size}")
    private String name;

    @NotNull(message = "{user.group.name.is.null}")
    private UserGroupType type;

    @Size(max = 280, message = "{invalid.user.group.description.max.size}")
    private String description;

    @Ticket
    private Set<Long> userTickets;

    @Ticket
    private Set<Long> permissionTickets;
}
