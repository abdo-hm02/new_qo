package qoraa.net.modules.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import qoraa.net.common.auditing.AuditMetadata;
import qoraa.net.common.domain.Identifiable;
import qoraa.net.modules.reading.entity.ReadingProject;

@Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "project_book")
public class ProjectBook implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "book_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ReadingProject project;

    @Column(name = "isbn", nullable = false)
    private String isbn;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "author", nullable = false)
    private String author;

    @Column(name = "total_pages", nullable = false)
    private Integer totalPages;

    @Embedded
    private AuditMetadata auditMetadata = new AuditMetadata();

    @Override
    public Long getId() {
        return id;
    }
}
