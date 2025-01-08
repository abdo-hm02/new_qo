package qoraa.net.modules.usergroup.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Collection;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserGroupSearchRequest {
    private Collection<String> createdBy;
    private LocalDateTime createdFrom;
    private LocalDateTime createdTo;
    private Collection<String> id;
    private Collection<String> modifiedBy;
    private LocalDateTime modifiedFrom;
    private LocalDateTime modifiedTo;
    private String name;
    private Collection<UserGroupType> type;
    private Collection<String> userGroupTypes;
    private String userTicket;
    private String username;
}
