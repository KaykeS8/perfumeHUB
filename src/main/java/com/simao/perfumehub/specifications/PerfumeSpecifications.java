package com.simao.perfumehub.specifications;

import com.simao.perfumehub.entities.Perfume;
import com.simao.perfumehub.entities.enums.Genre;
import org.springframework.data.jpa.domain.Specification;

import java.math.BigDecimal;

public class PerfumeSpecifications {

    public PerfumeSpecifications() {}

    public static Specification<Perfume> hasBrand(String brandName) {
        return (root, query, cb) ->
                brandName == null ? null : cb.equal(cb.lower(root.get("brand").get("name")), brandName.toLowerCase());
    }

    public static Specification<Perfume> hasGenre(String genre) {
        return (root, query, cb) ->
                genre == null ? null : cb.equal(root.get("genre"), Genre.valueOf(genre.toUpperCase()));
    }

    public static Specification<Perfume> hasConcentration(String concentration) {
        return (root, query, cb) ->
                concentration == null ? null : cb.equal(root.get("concentration"), Genre.valueOf(concentration.toUpperCase()));
    }

    public static Specification<Perfume> priceBetween(BigDecimal priceMin, BigDecimal priceMax) {
        return (root, query, cb) -> {
            if (priceMin == null && priceMax == null) return null;
            if (priceMin == null) return cb.lessThanOrEqualTo(root.get("price"), priceMax);
            if (priceMax == null) return cb.greaterThanOrEqualTo(root.get("price"), priceMin);
            return cb.between(root.get("price"), priceMin,priceMax);
        };
    }

    public static Specification<Perfume> nameContains(String name) {
        return (root, query, cb) ->
                name == null ? null : cb.like(cb.lower(root.get("name")), "%" + name.toLowerCase() + "%");
    }

}