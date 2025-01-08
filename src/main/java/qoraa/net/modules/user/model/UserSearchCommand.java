package qoraa.net.modules.user.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class UserSearchCommand {
    private Collection<Boolean> active;
    private String createdFrom;
    private String createdTo;
    private Collection<String> creationUser;
    private String email;
    private String filterName;
    private String firstName;
    private String lastLoginFrom;
    private String lastLoginTo;
    private String lastName;
    private Collection<String> modificationUser;
    private String modifiedFrom;
    private String modifiedTo;
    private Collection<String> username;
    private String partialUserInput;
}
