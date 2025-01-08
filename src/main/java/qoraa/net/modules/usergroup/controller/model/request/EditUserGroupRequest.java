package qoraa.net.modules.usergroup.controller.model.request;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import qoraa.net.common.annotation.Ticket;

@Setter
@Getter
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EditUserGroupRequest extends UserGroupRequest {
    @Ticket
    Long ticket;
}
