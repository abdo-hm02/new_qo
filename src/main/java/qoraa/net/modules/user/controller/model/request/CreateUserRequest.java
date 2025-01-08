package qoraa.net.modules.user.controller.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class CreateUserRequest {

    @NotBlank(message = "{user.firstName.is.blank}")
    @Size(max = 255, message = "{invalid.user.firstName.max.size}")
    String firstName;

    @NotBlank
    private String password;

    @NotBlank(message = "{user.lastName.is.blank}")
    @Size(max = 255, message = "{invalid.user.lastName.max.size}")
    String lastName;

    String username;

    @NotBlank(message = "{user.email.is.blank}")
    @Size(max = 255, message = "{invalid.user.email.max.size}")
    String email;

    Boolean active;
}
