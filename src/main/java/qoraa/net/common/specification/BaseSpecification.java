package qoraa.net.common.specification;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Expression;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Method;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

import static qoraa.net.common.auditing.AuditMetadataHelper.AUDIT_METADATA;
import static qoraa.net.common.auditing.AuditMetadata_.CREATED_BY;
import static qoraa.net.common.auditing.AuditMetadata_.CREATED_ON;
import static qoraa.net.common.auditing.AuditMetadata_.MODIFIED_BY;
import static qoraa.net.common.auditing.AuditMetadata_.MODIFIED_ON;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class BaseSpecification {

    protected static void addLikePredicateForSearchName(String searchName, Path<String> field,
	    CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
	if (StringUtils.isNotEmpty(searchName)) {
	    predicates.add(criteriaBuilder.like(criteriaBuilder.lower(field), "%" + searchName.toLowerCase() + "%"));
	}
    }

    protected static <T> void addPredicateIfNotBlank(T value, Function<T, Predicate> predicateBuilder,
	    List<Predicate> predicates) {
	if (Objects.nonNull(value)) {
	    predicates.add(predicateBuilder.apply(value));
	}
    }

    protected static LocalDateTime parseDateTime(String dateTimeString) {
	if (StringUtils.isBlank(dateTimeString)) {
	    return null;
	}
	DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
	DateTimeFormatter dateFormatter = DateTimeFormatter.ISO_LOCAL_DATE;
	try {
	    return LocalDateTime.parse(dateTimeString, dateTimeFormatter);
	} catch (DateTimeException e) {
	    try {
		LocalDate date = LocalDate.parse(dateTimeString, dateFormatter);
		return date.atStartOfDay();
	    } catch (DateTimeException ex) {
		throw new IllegalArgumentException("Invalid date-time format: " + dateTimeString, ex);
	    }
	}
    }

    protected static void validateDateRange(Object fromDate, Object toDate, String fieldName) {
	LocalDateTime from = fromDate instanceof String string ? parseDateTime(string) : (LocalDateTime) fromDate;
	LocalDateTime to = toDate instanceof String string ? parseDateTime(string) : (LocalDateTime) toDate;

	if (from != null && to != null && from.isAfter(to)) {
	    throw new IllegalArgumentException(fieldName + "From cannot be after " + fieldName + "To");
	}
    }

    protected static void addDateTimeRangePredicate(LocalDateTime from, LocalDateTime to,
	    Expression<LocalDateTime> expression, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
	if (from != null) {
	    predicates.add(criteriaBuilder.greaterThanOrEqualTo(expression, from));
	}
	if (to != null) {
	    predicates.add(criteriaBuilder.lessThanOrEqualTo(expression, to));
	}
	if (from != null && to != null) {
	    predicates.add(criteriaBuilder.between(expression, from, to));
	}
    }

    protected static void addAuditPredicates(Object searchRequest, Root<?> root, CriteriaBuilder criteriaBuilder,
	    List<Predicate> predicates) {
	try {
	    Method getCreatedBy = searchRequest.getClass().getMethod("getCreatedBy");
	    Method getModifiedBy = searchRequest.getClass().getMethod("getModifiedBy");
	    Method getCreatedFrom = searchRequest.getClass().getMethod("getCreatedFrom");
	    Method getCreatedTo = searchRequest.getClass().getMethod("getCreatedTo");
	    Method getModifiedFrom = searchRequest.getClass().getMethod("getModifiedFrom");
	    Method getModifiedTo = searchRequest.getClass().getMethod("getModifiedTo");

	    Object createdFrom = getCreatedFrom.invoke(searchRequest);
	    Object createdTo = getCreatedTo.invoke(searchRequest);
	    Object modifiedFrom = getModifiedFrom.invoke(searchRequest);
	    Object modifiedTo = getModifiedTo.invoke(searchRequest);

	    // Validate date ranges first
	    validateDateRange(createdFrom, createdTo, "createdOn");
	    validateDateRange(modifiedFrom, modifiedTo, "modifiedOn");

	    LocalDateTime createdFromDate = createdFrom instanceof String string ? parseDateTime(string)
		    : (LocalDateTime) createdFrom;
	    LocalDateTime createdToDate = createdTo instanceof String string ? parseDateTime(string)
		    : (LocalDateTime) createdTo;
	    LocalDateTime modifiedFromDate = modifiedFrom instanceof String string ? parseDateTime(string)
		    : (LocalDateTime) modifiedFrom;
	    LocalDateTime modifiedToDate = modifiedTo instanceof String string ? parseDateTime(string)
		    : (LocalDateTime) modifiedTo;

	    addPredicateIfNotBlank(getCreatedBy.invoke(searchRequest),
		    createdBy -> criteriaBuilder.in(root.get(AUDIT_METADATA).get(CREATED_BY)).value(createdBy),
		    predicates);

	    addPredicateIfNotBlank(getModifiedBy.invoke(searchRequest),
		    modifiedBy -> criteriaBuilder.in(root.get(AUDIT_METADATA).get(MODIFIED_BY)).value(modifiedBy),
		    predicates);

	    addDateTimeRangePredicate(createdFromDate, createdToDate, root.get(AUDIT_METADATA).get(CREATED_ON),
		    criteriaBuilder, predicates);

	    addDateTimeRangePredicate(modifiedFromDate, modifiedToDate, root.get(AUDIT_METADATA).get(MODIFIED_ON),
		    criteriaBuilder, predicates);
	} catch (Exception e) {
	    throw new IllegalArgumentException("Search request does not support audit fields", e);
	}
    }
}
