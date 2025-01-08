package qoraa.net.common.auditing;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import static qoraa.net.common.auditing.AuditMetadata_.CREATED_BY;
import static qoraa.net.common.auditing.AuditMetadata_.CREATED_ON;
import static qoraa.net.common.auditing.AuditMetadata_.MODIFIED_BY;
import static qoraa.net.common.auditing.AuditMetadata_.MODIFIED_ON;

@Component
public class AuditMetadataHelper {
    public static final String AUDIT_METADATA = "auditMetadata";
    public static final String AUDIT_METADATA_FIELD_SEPARATOR = ".";

    public Pageable applyAuditMetadataSort(Pageable pageable) {
        Sort sort = pageable.getSort();
        for (Sort.Order order : sort) {
            String property = order.getProperty();
            if (property.equals(CREATED_ON) || property.equals(MODIFIED_ON) || property.equals(CREATED_BY)
                    || property.equals(MODIFIED_BY)) {
                Sort.Direction direction = order.getDirection();
                Sort modifiedSort = Sort.by(direction, AUDIT_METADATA + AUDIT_METADATA_FIELD_SEPARATOR + property);
                pageable = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), modifiedSort);
            }
        }
        return pageable;
    }
}
