package govone.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import govone.backend.entity.ApplicationDocument;

public interface ApplicationDocumentRepository extends JpaRepository<ApplicationDocument, Long> {

	List<ApplicationDocument> findByApplicationId(String applicationId);
}