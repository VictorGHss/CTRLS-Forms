package br.dev.ctrls.api.domain.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.OffsetDateTime;
import java.util.UUID;

@Entity
@Table(name = "doctor_clinics", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"doctor_id", "clinic_id"})
})
@Data
@NoArgsConstructor
public class DoctorClinic {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "link_token")
    private UUID linkToken;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "doctor_id", nullable = false)
    private Doctor doctor;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "clinic_id", nullable = false)
    private Clinic clinic;

    @Column(name = "role_in_clinic")
    private String roleInClinic;

    @Column(name = "is_primary")
    private Boolean isPrimary = false;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}