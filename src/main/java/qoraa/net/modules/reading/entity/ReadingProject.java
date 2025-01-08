package qoraa.net.modules.reading.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import qoraa.net.common.auditing.AuditMetadata;
import qoraa.net.common.domain.Identifiable;
import qoraa.net.modules.project.entity.ProjectBook;
import qoraa.net.modules.project.entity.ProjectParticipant;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "reading_project")
public class ReadingProject extends AbstractAggregateRoot<ReadingProject> implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "project_id")
    private Long id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description")
    private String description;

    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    @Column(name = "end_date", nullable = false)
    private LocalDate endDate;

    @Column(name = "max_participants", nullable = false)
    private Integer maxParticipants;

    @Column(name = "daily_goal_pages", nullable = false)
    private Integer dailyGoalPages;

    @Column(name = "difficulty_level", nullable = false)
    @Enumerated(EnumType.STRING)
    private DifficultyLevel difficultyLevel;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ProjectStatus status;

    @OneToMany(mappedBy = "project")
    private Set<ProjectBook> books;

    @OneToMany(mappedBy = "project")
    private Set<ProjectParticipant> participants;

    @Column(name = "sharing_link")
    private String sharingLink;

    @Embedded
    private AuditMetadata auditMetadata = new AuditMetadata();

    @Override
    public Long getId() {
        return id;
    }
}
