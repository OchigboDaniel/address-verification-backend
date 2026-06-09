package com.address_verification.addressVerificationApp.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "addresses")
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    @Column(
            nullable = false
    )
    private Double latitude;
    @Column(
            nullable = false
    )
    private Double longitude;
    @Column(
            nullable = false
    )
    private String state;
    @Column(
            nullable = false
    )
    private String country;
    @Column(
            nullable = false
    )
    private String formattedAddress;


    @OneToOne
    @JoinColumn(name = "user_id", unique = true)
    private User user;
}
