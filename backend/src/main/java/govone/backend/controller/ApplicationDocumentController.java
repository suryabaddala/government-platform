package govone.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import govone.backend.entity.ApplicationDocument;
import govone.backend.service.ApplicationDocumentService;

@RestController
@RequestMapping("/api/applications/{applicationId}/documents")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class ApplicationDocumentController {

	private final ApplicationDocumentService documentService;

	public ApplicationDocumentController(ApplicationDocumentService documentService) {
		this.documentService = documentService;
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
}