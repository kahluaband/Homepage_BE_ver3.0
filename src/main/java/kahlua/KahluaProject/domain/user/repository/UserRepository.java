package kahlua.KahluaProject.domain.user.repository;

import kahlua.KahluaProject.domain.user.entity.User;
import kahlua.KahluaProject.domain.user.entity.UserType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmailAndDeletedAtIsNull(String email);

    Optional<User> findByIdAndDeletedAtIsNull(Long userId);

    Page<User> findAllByUserTypeNot(UserType userType, Pageable pageable);

    Page<User> findAllByUserType(UserType userType, Pageable pageable);

    Page<User> findAllByUserTypeIn(Collection<UserType> types, Pageable pageable);

    long countByUserType(UserType userType);
    long countByUserTypeIn(Collection<UserType> userTypes);
}
