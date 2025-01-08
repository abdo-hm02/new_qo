package qoraa.net.modules.usergroup.model;

import qoraa.net.modules.permission.entity.Permission;
import qoraa.net.modules.user.model.User;

import java.util.List;

public record AllUserGroupLists(List<Permission> permissionList, List<User> userList) {
}

