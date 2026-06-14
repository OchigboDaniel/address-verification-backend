package com.address_verification.addressVerificationApp.model;

import com.address_verification.addressVerificationApp.VerificationStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Verification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private LocalDate billDate;

    private String billAddress;

    @Enumerated(EnumType.STRING)
    private VerificationStatus status;


    @Column(length = 2000)
    private String aiComment;

    @Column(length = 2000)
    private String billImagePath;

    private LocalDateTime verifiedAt;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
}
