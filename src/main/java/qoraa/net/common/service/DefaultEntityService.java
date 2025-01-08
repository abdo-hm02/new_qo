package qoraa.net.common.service;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import qoraa.net.common.domain.ActiveEntity;
import qoraa.net.common.domain.DefaultEntity;
import qoraa.net.common.exception.ApplicationException;

import java.util.List;
import java.util.function.Function;

import static qoraa.net.modules.usergroup.model.enumeration.UserGroupType.SUPER_ADMIN;

@Service
public class DefaultEntityService {

    /**
     * The isDeactivationDefault method is a generic method that checks if a given entity is a default and active entity.
     * It takes one parameter: an entity of type T that extends both DefaultEntity and ActiveEntity interfaces.
     * The method checks if the entity is a default entity and if it's active.
     * If both conditions are met, it throws an ApplicationException with a detail message indicating that the default entity cannot be deactivated.
     */

    public <T extends DefaultEntity & ActiveEntity> void isDeactivationDefault(T entity) {
        if (entity.isDefault() && entity.isActive()) {
            throw ApplicationException.builder(HttpStatus.UNAUTHORIZED.value())
                    .detail("You cannot deactivate the default " + entity.getEntityName() + ".").build();
        }
    }

    /**
     * The isNonEditableDefault method is a generic method that checks if a given entity is a non-editable default entity.
     * It takes three parameters: an entity of type T that extends DefaultEntity, a username of type String which is the username of the authenticated user, and a Function named getGroupTypesByCurrentUser that takes a String and returns a List<String>.
     * The getGroupTypesByCurrentUser function is applied to the name parameter to get a list of group types for the current user.
     * The method then checks if the list of group types for the current user does not contain SUPER_ADMIN and if the entity is a default entity.
     * If both conditions are met, it throws an ApplicationException with a detail message indicating that the default entity cannot be edited.
     * If the conditions are not met, the method returns false, indicating that the entity is not a non-editable default entity.
     */

    public <T extends DefaultEntity> boolean isNonEditableDefault(T entity, String username, Function<String, List<String>> getGroupTypesByCurrentUser) {
        List<String> groupTypesByCurrentUser = getGroupTypesByCurrentUser.apply(username);
        if (!(groupTypesByCurrentUser.contains(SUPER_ADMIN.name())) && entity.isDefault()) {
            throw ApplicationException.builder(HttpStatus.UNAUTHORIZED.value())
                    .detail("You cannot edit the default " + entity.getEntityName() + ".").build();
        }
        return false;
    }
}
