package az.code.tourapi.utils;

import az.code.tourapi.models.entities.UserRequest;
import org.springframework.data.jpa.domain.Specification;

public class Specifications {

    public static final String ID = "id";

    public static <T> Specification<UserRequest> sameValue(String fieldName, T value) {
        return (root, query, criteriaBuilder) -> {
            if (value != null)
                return criteriaBuilder.equal(root.<T>get(fieldName), value);
            return criteriaBuilder.conjunction();
        };
    }

    public static <T> Specification<UserRequest> sameValueWithId(String fieldName, T value) {
        return (root, query, criteriaBuilder) -> {
            if (value != null)
                return criteriaBuilder.equal(root.get(ID).<T>get(fieldName), value);
            return criteriaBuilder.conjunction();
        };
    }
}
