package com.address_verification.addressVerificationApp.userAddress.model;

import com.address_verification.addressVerificationApp.Role;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(
            nullable = false
    )
    private String fullName;

    @Column(
            unique = true,
            nullable = false
    )
    private String email;

    @Column(
            nullable = false
    )
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

//    @Column(
//            nullable = false
//    )
//    private Double longitude;
//
//    @Column(
//            nullable = false
//    )
//    private Double latitude;

    @OneToOne(mappedBy = "user")
    private Address address;
}
