package govone.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import govone.backend.entity.ApplicationDocument;

public interface ApplicationDocumentRepository extends JpaRepository<ApplicationDocument, Long> {

	List<ApplicationDocument> findByApplicationId(String applicationId);

	Optional<ApplicationDocument> findByIdAndApplicationId(Long id, String applicationId);
}