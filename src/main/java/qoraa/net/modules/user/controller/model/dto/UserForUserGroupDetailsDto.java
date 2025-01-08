package qoraa.net.modules.user.controller.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import qoraa.net.common.annotation.Ticket;

@Getter
@Setter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class UserForUserGroupDetailsDto {
    String username;
    String firstName;
    String lastName;
    @Ticket
    Long userTicket;
}
