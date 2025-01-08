package qoraa.net.modules.user.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import qoraa.net.common.mappers.TicketMapper;
import qoraa.net.modules.user.controller.model.dto.UserDto;
import qoraa.net.modules.user.controller.model.request.CreateUserRequest;
import qoraa.net.modules.user.controller.model.request.UpdateUserRequest;
import qoraa.net.modules.user.model.User;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public abstract class UserMapper extends TicketMapper {

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "req.username", target = "username")
    @Mapping(source = "req.firstName", target = "firstName")
    @Mapping(source = "req.lastName", target = "lastName")
    @Mapping(source = "req.email", target = "email")
    @Mapping(source = "req.active", target = "active")
    public abstract User fromDto(CreateUserRequest req);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(source = "updateUserRequest.active", target = "active")
    public abstract void update(@MappingTarget User user, UpdateUserRequest updateUserRequest);

    @Named(value = "toDto")
    @Mapping(target = "ticket", expression = "java(toTicket(user.getId()))")
    @Mapping(source = "auditMetadata.createdBy", target = "createdBy")
    @Mapping(source = "auditMetadata.createdOn", target = "createdOn")
    @Mapping(source = "auditMetadata.modifiedBy", target = "modifiedBy")
    @Mapping(source = "auditMetadata.modifiedOn", target = "modifiedOn")
    @Mapping(target = "isDefault", expression = "java(mapBoolean(user.isDefault()))")
    public abstract UserDto toDto(User user);

    Boolean mapBoolean(boolean isDefault) {
        return isDefault;
    }
}



