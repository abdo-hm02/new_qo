package qoraa.net.modules.project.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
import qoraa.net.modules.reading.entity.ReadingProject;
import qoraa.net.modules.user.model.User;

import java.time.LocalDateTime;

@Entity
@Setter @Getter
@NoArgsConstructor @AllArgsConstructor
@Table(name = "project_participant")
public class ProjectParticipant implements Identifiable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "participant_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "project_id", nullable = false)
    private ReadingProject project;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "joined_at", nullable = false)
    private LocalDateTime joinedAt;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private ParticipantStatus status;

    @Override
    public Long getId() {
        return id;
    }
}
