package qoraa.net.modules.usergroup.repository;

import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.jdbc.Sql;
import qoraa.net.AbstractPostgresRepositoryTest;
import qoraa.net.modules.usergroup.model.UserGroup;
import qoraa.net.modules.usergroup.model.enumeration.UserGroupType;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Sql("/usergroup/insert_usergroup.sql")
class UserGroupPostgresIntegrationTest extends AbstractPostgresRepositoryTest {

    @Autowired
    private UserGroupRepository userGroupRepository;

    @Test
    void findByTypeTest() {
	UserGroupType userGroupType = UserGroupType.SYSTEM_ADMIN;
	List<UserGroup> userGroup = userGroupRepository.findByType(String.valueOf(userGroupType));

	assertThat(userGroup).hasSize(1).first().satisfies(ug -> {
	    assertThat(ug.getName()).isEqualTo("test user group 1");
	    assertThat(ug.getType()).isEqualTo(userGroupType);
	    assertThat(ug.getDescription()).isEqualTo("Test group description");
	});
    }
}
