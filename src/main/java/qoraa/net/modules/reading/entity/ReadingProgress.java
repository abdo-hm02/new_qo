package qoraa.net.modules.reading.entity;

import jakarta.persistence.Column;
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
import qoraa.net.common.domain.Identifiable;
import qoraa.net.modules.project.entity.ProjectParticipant;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "reading_progress")
public class ReadingProgress implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "participant_id", nullable = false)
    private ProjectParticipant participant;

    @Column(name = "date", nullable = false)
    private LocalDate date;

    @Column(name = "pages_read", nullable = false)
    private Integer pagesRead;

    @Column(name = "completed", nullable = false)
    private Boolean completed;

    @Column(name = "created_on", nullable = false)
    private LocalDateTime createdOn;

    @Column(name = "modified_on")
    private LocalDateTime modifiedOn;

    @Override
    public Long getId() {
        return id;
    }
}
