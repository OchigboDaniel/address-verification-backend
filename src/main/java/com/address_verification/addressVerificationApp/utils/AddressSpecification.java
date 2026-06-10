package com.address_verification.addressVerificationApp.utils;

import com.address_verification.addressVerificationApp.model.Address;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;


public class AddressSpecification {

    public static Specification<Address> withFilters(
            String country, String state, String email) {

        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();

            if (country != null && !country.isEmpty()) {
                predicates.add(cb.like(
                        cb.lower(root.get("country")),
                        "%" + country.toLowerCase() + "%"
                ));
            }

            if (state != null && !state.isEmpty()) {
                predicates.add(cb.like(
                        cb.lower(root.get("state")),
                        "%" + state.toLowerCase() + "%"
                ));
            }

            if (email != null && !email.isEmpty()) {
                predicates.add(cb.equal(cb.lower(root.get("user").get("email")), email.toLowerCase()));
            }

            return cb.and(predicates.toArray(new Predicate[0]));
        };
    }
}
