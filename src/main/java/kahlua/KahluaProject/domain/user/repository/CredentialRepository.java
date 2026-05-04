package kahlua.KahluaProject.domain.user.repository;

import kahlua.KahluaProject.domain.user.entity.Credential;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CredentialRepository extends JpaRepository<Credential, Long> {
}