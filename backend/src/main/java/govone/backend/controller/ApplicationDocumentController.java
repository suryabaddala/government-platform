package govone.backend.controller;

import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.multipart.MultipartFile;

import govone.backend.entity.ApplicationDocument;
import govone.backend.service.ApplicationDocumentService;

@RestController
@RequestMapping("/api/applications/{applicationId}/documents")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class ApplicationDocumentController {

	private final ApplicationDocumentService documentService;
	private final String adminKey;

	public ApplicationDocumentController(ApplicationDocumentService documentService,
			@Value("${govone.admin.key}") String adminKey) {
		this.documentService = documentService;
		this.adminKey = adminKey;
	}

	@PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
	@ResponseStatus(HttpStatus.CREATED)
	public ApplicationDocument upload(@PathVariable String applicationId, @RequestParam("file") MultipartFile file) {
		return documentService.store(applicationId, file);
	}

	@GetMapping
	public List<ApplicationDocument> list(@PathVariable String applicationId) {
		return documentService.findByApplicationId(applicationId);
	}

	@GetMapping("/{documentId}/download")
	public ResponseEntity<Resource> download(@PathVariable String applicationId,
			@PathVariable Long documentId,
			@RequestHeader(value = "X-Admin-Key", required = false) String requestAdminKey) {
		if (requestAdminKey == null || !adminKey.equals(requestAdminKey)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin access required");
		}
		ApplicationDocument document = documentService.findDocument(applicationId, documentId);
		Path path = documentService.getStoredPath(applicationId, documentId);
		String contentType = document.getContentType() == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : document.getContentType();
		return ResponseEntity.ok()
				.contentType(MediaType.parseMediaType(contentType))
				.header("Content-Disposition", "attachment; filename=\"" + document.getOriginalFilename() + "\"")
				.body(new FileSystemResource(path));
	}
}