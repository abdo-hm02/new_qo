package qoraa.net.modules.usergroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import qoraa.net.modules.usergroup.model.UserGroup;

import java.util.List;
import java.util.Optional;

public interface UserGroupRepository extends JpaRepository<UserGroup, Long>, JpaSpecificationExecutor<UserGroup> {

    @Transactional
    @Query("SELECT ug FROM UserGroup ug WHERE ug.isDefault = true")
    Optional<UserGroup> findDefaultUserGroup();

    @Transactional
    @Query(value = """
        SELECT * FROM user_group
        WHERE type = :type
    """, nativeQuery = true)
    List<UserGroup> findByType(@Param("type") String type);

    Optional<UserGroup> findByIdOrderByNameAscPermissionsAsc(Long id);
}
