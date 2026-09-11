package govone.backend.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import govone.backend.entity.ApplicationDocument;
import govone.backend.repository.ApplicationDocumentRepository;

@Service
public class ApplicationDocumentService {

	private final ApplicationDocumentRepository documentRepository;
	private final ApplicationService applicationService;
	private final Path uploadDirectory = Path.of("data", "uploads");

	public ApplicationDocumentService(ApplicationDocumentRepository documentRepository,
			ApplicationService applicationService) {
		this.documentRepository = documentRepository;
		this.applicationService = applicationService;
	}

	public ApplicationDocument store(String applicationId, MultipartFile file) {
		applicationService.findByApplicationId(applicationId);
		if (file == null || file.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "A document file is required");
		}

		String originalFilename = file.getOriginalFilename() == null ? "document" : file.getOriginalFilename();
		String safeFilename = Path.of(originalFilename).getFileName().toString().replaceAll("[^a-zA-Z0-9._-]", "_");
		String storedFilename = UUID.randomUUID() + "-" + safeFilename;

		try {
			Files.createDirectories(uploadDirectory);
			Files.copy(file.getInputStream(), uploadDirectory.resolve(storedFilename), StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException exception) {
			throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not store document", exception);
		}

		return documentRepository.save(new ApplicationDocument(
				applicationId.trim().toUpperCase(), safeFilename, storedFilename,
				file.getContentType(), file.getSize()));
	}

	public List<ApplicationDocument> findByApplicationId(String applicationId) {
		applicationService.findByApplicationId(applicationId);
		return documentRepository.findByApplicationId(applicationId.trim().toUpperCase());
	}

	public Path getStoredPath(String applicationId, Long documentId) {
		ApplicationDocument document = documentRepository
				.findByIdAndApplicationId(documentId, applicationId.trim().toUpperCase())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
		Path path = uploadDirectory.resolve(document.getStoredFilename()).normalize();
		if (!path.startsWith(uploadDirectory.normalize()) || !Files.exists(path)) {
			throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Document file not found");
		}
		return path;
	}

	public ApplicationDocument findDocument(String applicationId, Long documentId) {
		return documentRepository.findByIdAndApplicationId(documentId, applicationId.trim().toUpperCase())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Document not found"));
	}
}