package com.simao.perfumehub.specifications;

import com.simao.perfumehub.entities.StockMovement;
import com.simao.perfumehub.entities.enums.MovementType;
import org.springframework.data.jpa.domain.Specification;

public class StockMovementSpecification {

    public static Specification<StockMovement> hasPerfumeId(Long perfumeId) {
        return (root, query, cb) ->
                perfumeId == null ? null : cb.equal(root.get("perfume").get("id"), perfumeId);
    }

    public static Specification<StockMovement> hasType(String type) {
            return (root, query, cb) ->
                    type == null ? null : cb.equal(root.get("movementType"), MovementType.valueOf(type.toUpperCase()));
    }
}
