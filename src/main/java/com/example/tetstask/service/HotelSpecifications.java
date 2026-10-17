package com.example.tetstask.service;

import com.example.tetstask.domain.Hotel;
import jakarta.persistence.criteria.JoinType;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public final class HotelSpecifications {

    private static final char LIKE_ESCAPE = '\\';

    private HotelSpecifications() {
    }

    public static Specification<Hotel> matches(HotelSearchCriteria criteria) {
        return (root, query, builder) -> {
            List<Predicate> predicates = new ArrayList<>();
            addContains(predicates, builder, root.get("name"), criteria.name());
            addContains(predicates, builder, root.get("brand"), criteria.brand());
            addContains(predicates, builder, root.get("address").get("city"), criteria.city());
            addContains(predicates, builder, root.get("address").get("country"), criteria.country());

            if (hasText(criteria.amenities())) {
                query.distinct(true);
                addContains(predicates, builder,
                        root.join("amenities", JoinType.INNER), criteria.amenities());
            }

            return builder.and(predicates.toArray(Predicate[]::new));
        };
    }

    private static void addContains(List<Predicate> predicates,
                                    jakarta.persistence.criteria.CriteriaBuilder builder,
                                    jakarta.persistence.criteria.Expression<String> field,
                                    String value) {
        if (hasText(value)) {
            predicates.add(builder.like(
                    builder.lower(field),
                    "%" + escapeLike(value.trim().toLowerCase(Locale.ROOT)) + "%",
                    LIKE_ESCAPE
            ));
        }
    }

    private static String escapeLike(String value) {
        return value.replace("\\", "\\\\").replace("%", "\\%").replace("_", "\\_");
    }

    private static boolean hasText(String value) {
        return value != null && !value.isBlank();
    }
}
