package qoraa.net.modules.user.specification;


import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.jpa.domain.Specification;
import qoraa.net.modules.user.model.User;
import qoraa.net.modules.user.model.UserSearchCommand;
import qoraa.net.common.specification.BaseSpecification;

import java.util.ArrayList;
import java.util.List;

import static qoraa.net.common.auditing.AuditMetadataHelper.AUDIT_METADATA;
import static qoraa.net.common.auditing.AuditMetadata_.CREATED_BY;
import static qoraa.net.common.auditing.AuditMetadata_.CREATED_ON;
import static qoraa.net.common.auditing.AuditMetadata_.MODIFIED_BY;
import static qoraa.net.common.auditing.AuditMetadata_.MODIFIED_ON;
import static qoraa.net.modules.user.model.User_.ACTIVE;
import static qoraa.net.modules.user.model.User_.EMAIL;
import static qoraa.net.modules.user.model.User_.FIRST_NAME;
import static qoraa.net.modules.user.model.User_.LAST_LOGIN;
import static qoraa.net.modules.user.model.User_.LAST_NAME;
import static qoraa.net.modules.user.model.User_.USERNAME;


@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class UserJpaSpecifications extends BaseSpecification {

    public static Specification<User> search(UserSearchCommand command) {
	return (root, query, criteriaBuilder) -> {
	    List<Predicate> predicates = new ArrayList<>();
	    List<Predicate> orPredicates = new ArrayList<>();

	    addBasicPredicates(command, root, criteriaBuilder, predicates);
	    addAuditPredicates(command, root, criteriaBuilder, predicates);

	    query.where(criteriaBuilder.and(predicates.toArray(new Predicate[0])));

	    // Handle name predicate for partial search by username, first name, last name, email
	    if (StringUtils.isNotBlank(command.getPartialUserInput())) {
		addPartialSearchPredicates(command.getPartialUserInput(), root, criteriaBuilder, orPredicates);
		Predicate combinedOrPredicate = criteriaBuilder.or(orPredicates.toArray(new Predicate[0]));
		Predicate combinedAndPredicate = criteriaBuilder.and(predicates.toArray(new Predicate[0]));
		query.where(criteriaBuilder.and(combinedAndPredicate, combinedOrPredicate));
	    }

	    return query.getRestriction();
	};
    }

    private static void addAuditPredicates(UserSearchCommand command, Root<User> root, CriteriaBuilder criteriaBuilder,
	    List<Predicate> predicates) {
	addPredicateIfNotBlank(command.getCreationUser(),
		creator -> criteriaBuilder.in(root.get(AUDIT_METADATA).get(CREATED_BY)).value(creator), predicates);
	addPredicateIfNotBlank(command.getModificationUser(),
		modifier -> criteriaBuilder.in(root.get(AUDIT_METADATA).get(MODIFIED_BY)).value(modifier), predicates);

	validateDateRange(command.getCreatedFrom(), command.getCreatedTo(), "createdOn");
	validateDateRange(command.getModifiedFrom(), command.getModifiedTo(), "modifiedOn");
	validateDateRange(command.getLastLoginFrom(), command.getLastLoginTo(), "lastLogin");

	addDateTimeRangePredicate(parseDateTime(command.getCreatedFrom()), parseDateTime(command.getCreatedTo()),
		root.get(AUDIT_METADATA).get(CREATED_ON), criteriaBuilder, predicates);

	addDateTimeRangePredicate(parseDateTime(command.getModifiedFrom()), parseDateTime(command.getModifiedTo()),
		root.get(AUDIT_METADATA).get(MODIFIED_ON), criteriaBuilder, predicates);

	addDateTimeRangePredicate(parseDateTime(command.getLastLoginFrom()), parseDateTime(command.getLastLoginTo()),
		root.get(LAST_LOGIN), criteriaBuilder, predicates);
    }

    private static void addBasicPredicates(UserSearchCommand command, Root<User> root, CriteriaBuilder criteriaBuilder,
	    List<Predicate> predicates) {
	addPredicateIfNotBlank(command.getActive(), active -> criteriaBuilder.in(root.get(ACTIVE)).value(active),
		predicates);
	addPredicateIfNotBlank(command.getUsername(), active -> criteriaBuilder.in(root.get(USERNAME)).value(active),
		predicates);
    }

    private static void addPartialSearchPredicates(String partialInput, Root<User> root, CriteriaBuilder criteriaBuilder,
	    List<Predicate> predicates) {
	addLikePredicateForSearchName(partialInput, root.get(USERNAME), criteriaBuilder, predicates);
	addLikePredicateForSearchName(partialInput, root.get(FIRST_NAME), criteriaBuilder, predicates);
	addLikePredicateForSearchName(partialInput, root.get(LAST_NAME), criteriaBuilder, predicates);
	addLikePredicateForSearchName(partialInput, root.get(EMAIL), criteriaBuilder, predicates);
    }
}
