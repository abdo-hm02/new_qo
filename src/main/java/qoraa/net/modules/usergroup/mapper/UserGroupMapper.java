package qoraa.net.modules.usergroup.mapper;

import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.mapstruct.ReportingPolicy;
import qoraa.net.common.mappers.TicketMapper;
import qoraa.net.modules.user.controller.model.dto.UserForUserGroupDetailsDto;
import qoraa.net.modules.user.mapper.UserMapper;
import qoraa.net.modules.user.model.User;
import qoraa.net.modules.usergroup.controller.model.dto.UserGroupDetailsDto;
import qoraa.net.modules.usergroup.controller.model.dto.UserGroupDto;
import qoraa.net.modules.usergroup.controller.model.dto.UserGroupPageDto;
import qoraa.net.modules.usergroup.controller.model.request.UserGroupRequest;
import qoraa.net.modules.usergroup.model.AllUserGroupLists;
import qoraa.net.modules.usergroup.model.UserGroup;

import java.util.Set;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = { UserMapper.class })
public abstract class UserGroupMapper extends TicketMapper {

    private static final String ID_LENGTH_FORMAT = "%07d";

    public static String formatId(Long id) {
	return String.format(ID_LENGTH_FORMAT, id);
    }

    @Named("toDto")
    @Mapping(target = "ticket", source = "id")
    @Mapping(source = "auditMetadata.createdBy", target = "createdBy")
    @Mapping(source = "auditMetadata.createdOn", target = "createdOn")
    @Mapping(source = "auditMetadata.modifiedBy", target = "modifiedBy")
    @Mapping(source = "auditMetadata.modifiedOn", target = "modifiedOn")
    public abstract UserGroupDto toDto(UserGroup userGroup);

    @Mapping(target = "ticket", source = "id")
    @Mapping(target = "id", expression = "java(formatId(userGroup.getId()))")
    @Mapping(target = "numberOfUsers", expression = "java(userGroup.getUsers().size())")
    @Mapping(target = "type", expression = "java(userGroup.getType().getDescription())")
    @Mapping(source = "auditMetadata.createdBy", target = "createdBy")
    @Mapping(source = "auditMetadata.createdOn", target = "createdOn")
    @Mapping(source = "auditMetadata.modifiedBy", target = "modifiedBy")
    @Mapping(source = "auditMetadata.modifiedOn", target = "modifiedOn")
    public abstract UserGroupPageDto toUserGroupPageDto(UserGroup userGroup);

    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "users", source = "allLists.userList")
    @Mapping(target = "permissions", source = "allLists.permissionList")
    @Mapping(target = "name", source = "req.name")
    @Mapping(target = "type", source = "req.type")
    @Mapping(target = "description", source = "req.description")
    public abstract UserGroup fromDto(UserGroupRequest req, AllUserGroupLists allLists);

    @Named("toDetailsDto")
    @BeanMapping(ignoreByDefault = true)
    @Mapping(target = "ticket", source = "userGroup.id")
    @Mapping(target = "name", source = "userGroup.name")
    @Mapping(target = "type", source = "userGroup.type")
    @Mapping(target = "description", source = "userGroup.description")
    @Mapping(target = "users", source = "userGroup.users", qualifiedByName = "toDtoForUserGroupDetails")
    @Mapping(target = "permissions", source = "userGroup.permissions")
    @Mapping(target = "createdBy", source = "userGroup.auditMetadata.createdBy")
    @Mapping(target = "createdOn", source = "userGroup.auditMetadata.createdOn")
    @Mapping(target = "modifiedBy", source = "userGroup.auditMetadata.modifiedBy")
    @Mapping(target = "modifiedOn", source = "userGroup.auditMetadata.modifiedOn")
    public abstract UserGroupDetailsDto toDetailsDto(UserGroup userGroup);

    @Named(value = "toDtoForUserGroupDetails")
    @Mapping(target = "userTicket", source = "user.userId")
    public abstract UserForUserGroupDetailsDto toDtoForUserGroupDetails(User user);
}
