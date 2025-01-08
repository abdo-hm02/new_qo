package qoraa.net.modules.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import qoraa.net.common.security.authorization.UserAccountDetails;
import qoraa.net.common.security.authorization.UserAccountDetailsLoader;
import qoraa.net.modules.user.model.User;

@Repository
interface UserDetailsRepository extends JpaRepository<User, Long>, UserAccountDetailsLoader {

    @Override
    @Transactional(readOnly = true)
    @Query(value = """
            SELECT u.active AS enabled, u.first_name AS firstName, u.last_name AS lastName, u.username AS username,
                   STRING_AGG(p.name, ',') AS permissions
            FROM "user" u
            LEFT JOIN user_group_user ugu ON u.user_id = ugu.user_id
            LEFT JOIN user_group ug ON ugu.user_group_id = ug.user_group_id
            LEFT JOIN user_group_permission ugp ON ug.user_group_id = ugp.user_group_id
            LEFT JOIN permission p ON ugp.permission_id = p.permission_id
            WHERE u.email = :email
            GROUP BY u.user_id, u.active, u.first_name, u.last_name""", nativeQuery = true)
    UserAccountDetails load(String email);
}

