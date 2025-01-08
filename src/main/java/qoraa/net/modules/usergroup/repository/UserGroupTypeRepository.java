package qoraa.net.modules.usergroup.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import qoraa.net.modules.usergroup.model.UserGroupType;

public interface UserGroupTypeRepository extends JpaRepository<UserGroupType, String> {

    @Query("""
		SELECT ugt
		FROM UserGroupType ugt
		JOIN FETCH ugt.userGroupTypePermissions ugp
		JOIN FETCH ugp.permission p
		WHERE ugt.name = :name
		ORDER BY p.category ASC, p.name ASC
		""")
    UserGroupType findByNameOrderByCategoryAscNameAsc(String name);
}
