package govone.backend.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import govone.backend.entity.Application;

public interface ApplicationRepository extends JpaRepository<Application, Long> {

	Optional<Application> findByApplicationId(String applicationId);

	boolean existsByApplicationId(String applicationId);
}