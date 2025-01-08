package qoraa.net.common.security.authorization;

import java.util.Set;

public interface PermissionLoader {
    Set<String> loadPermissions(String email);
}
