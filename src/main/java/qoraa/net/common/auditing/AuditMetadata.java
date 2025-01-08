package qoraa.net.common.auditing;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Setter @Getter
@ToString
@Embeddable
public class AuditMetadata {

    @Column(name = "created_by", nullable = false, updatable = false)
    @CreatedBy
    private String createdBy;

    @Column(name = "created_on", nullable = false, updatable = false)
    @CreatedDate
    private LocalDateTime createdOn;

    @Column(name = "modified_by", nullable = false)
    @LastModifiedBy
    private String modifiedBy;

    @Column(name = "modified_on", nullable = false)
    @LastModifiedDate
    private LocalDateTime modifiedOn;
}
