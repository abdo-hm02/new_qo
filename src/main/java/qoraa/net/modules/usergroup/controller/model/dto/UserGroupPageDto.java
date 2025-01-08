package qoraa.net.modules.usergroup.controller.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import qoraa.net.common.annotation.Ticket;

import java.time.LocalDateTime;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserGroupPageDto {
    String createdBy;
    LocalDateTime createdOn;
    String id;
    String modifiedBy;
    LocalDateTime modifiedOn;
    String name;
    Integer numberOfUsers;
    @Ticket
    Long ticket;
    String type;
}
