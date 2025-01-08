package qoraa.net.modules.permission.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import qoraa.net.common.annotation.Ticket;
import qoraa.net.modules.permission.entity.PermissionGroup;

@JsonInclude(JsonInclude.Include.NON_NULL)
public record PermissionDto(@Getter PermissionGroup category, @Getter String name, @Getter @Ticket long ticket) {
}
