package az.code.tourapi.utils;

import az.code.tourapi.enums.UserRequestStatus;
import az.code.tourapi.models.entities.UserRequest;
import org.springframework.data.jpa.domain.Specification;

public class Specifications {

    public static Specification<UserRequest> sameStatus(UserRequestStatus status) {
        return (root, query, criteriaBuilder) -> {
            if (status != null)
                return criteriaBuilder.equal(root.get("status"),
                        UserRequestStatus.values()[status.ordinal()]);
            return criteriaBuilder.conjunction();
        };
    }

    public static <T> Specification<UserRequest> sameValue(String paramName, T name) {
        return (root, query, criteriaBuilder) -> {
            if (name != null)
                return criteriaBuilder.equal(root.<T> get(paramName), name);
            return criteriaBuilder.conjunction();
        };
    }
}
