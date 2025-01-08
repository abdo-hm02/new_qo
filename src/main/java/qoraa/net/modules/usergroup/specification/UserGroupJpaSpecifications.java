package qoraa.net.modules.usergroup.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.Specification;
import qoraa.net.modules.usergroup.model.UserGroup;
import qoraa.net.modules.usergroup.model.UserGroupSearchRequest;
import qoraa.net.common.specification.BaseSpecification;

import java.util.ArrayList;
import java.util.List;

import static qoraa.net.modules.user.model.User_.USERNAME;
import static qoraa.net.modules.user.model.User_.USER_ID;
import static qoraa.net.modules.usergroup.model.UserGroup_.NAME;
import static qoraa.net.modules.usergroup.model.UserGroup_.TYPE;
import static qoraa.net.modules.usergroup.model.UserGroup_.USERS;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UserGroupJpaSpecifications extends BaseSpecification {

    public static Specification<UserGroup> searchUserGroup(UserGroupSearchRequest searchRequest) {
	return (root, query, criteriaBuilder) -> {
	    List<Predicate> predicates = new ArrayList<>();

	    addBasicPredicates(searchRequest, root, criteriaBuilder, predicates);
	    addAuditPredicates(searchRequest, root, criteriaBuilder, predicates);
	    addUserPredicates(searchRequest, root, criteriaBuilder, predicates);

	    return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
	};
    }

    private static void addBasicPredicates(UserGroupSearchRequest searchRequest, Root<UserGroup> root,
					   CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
	addLikePredicateForSearchName(searchRequest.getName(), root.get(NAME), criteriaBuilder, predicates);
	addPredicateIfNotBlank(searchRequest.getType(), types -> criteriaBuilder.in(root.get(TYPE)).value(types),
		predicates);
    }

    private static void addUserPredicates(UserGroupSearchRequest searchRequest, Root<UserGroup> root,
	    CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
	addPredicateIfNotBlank(searchRequest.getUsername(),
		username -> criteriaBuilder.in(root.join(USERS).get(USERNAME)).value(username), predicates);
	addPredicateIfNotBlank(searchRequest.getUserTicket(),
		userId -> criteriaBuilder.in(root.join(USERS).get(USER_ID)).value(userId), predicates);
    }
}
