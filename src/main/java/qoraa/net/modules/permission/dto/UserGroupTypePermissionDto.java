package qoraa.net.modules.permission.dto;

import lombok.Builder;
import lombok.Value;
import qoraa.net.common.annotation.Ticket;
import qoraa.net.modules.permission.entity.PermissionGroup;

@Value
@Builder
public class UserGroupTypePermissionDto {
    String name;
    PermissionGroup category;
    boolean disabled;
    boolean preselected;
    @Ticket
    long ticket;
}
