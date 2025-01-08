package qoraa.net.modules.permission.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;
import qoraa.net.modules.permission.entity.Permission;

import java.util.List;
import java.util.Set;

public interface PermissionRepository extends JpaRepository<Permission, Long> {
    List<Permission> findAllByOrderByCategoryAscNameAsc();

    @Transactional(readOnly = true)
    @Query(value = """
        SELECT p.permission_id, p.name, p.category
        FROM permission p, user_group_type_permission u
        WHERE p.permission_id = u.permission_id
        AND u.user_group_type_name = 'SYSTEM_ADMIN'
    """, nativeQuery = true)
    Set<Permission> findByDefaultPermissions();
}
