package qoraa.net.modules.usergroup.controller.model.dto;

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
public class UserGroupNameTicketDetailsDto {
    boolean isDefault;
    String name;
    @Ticket
    Long ticket;
    String type;
}
