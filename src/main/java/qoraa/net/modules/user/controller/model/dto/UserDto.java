package qoraa.net.modules.user.controller.model.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

@Getter
@Setter
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@AllArgsConstructor
@Builder
public class UserDto {
    String ticket;
    String firstName;
    String lastName;
    String email;
    String username;
    boolean active;
    String createdBy;
    String createdOn;
    String modifiedBy;
    String modifiedOn;
    String lastLogin;
    Boolean isDefault;
}
