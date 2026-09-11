package govone.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;

import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.server.ResponseStatusException;

import govone.backend.entity.Application;
import govone.backend.service.ApplicationService;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class ApplicationController {

	private final ApplicationService applicationService;
	private final String adminKey;

	public ApplicationController(ApplicationService applicationService, @Value("${govone.admin.key}") String adminKey) {
		this.applicationService = applicationService;
		this.adminKey = adminKey;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public Application createApplication(@RequestBody CreateApplicationRequest request) {
		return applicationService.create(request.serviceName());
	}

	@GetMapping("/{applicationId}")
	public Application getApplication(@PathVariable String applicationId) {
		return applicationService.findByApplicationId(applicationId);
	}

	@PutMapping("/{applicationId}/status")
	public Application updateStatus(@PathVariable String applicationId,
			@RequestBody UpdateStatusRequest request,
			@RequestHeader(value = "X-Admin-Key", required = false) String requestAdminKey) {
		checkAdminKey(requestAdminKey);
		return applicationService.updateStatus(applicationId, request.status());
	}

	@GetMapping("/admin")
	public Iterable<Application> getAllApplications(
			@RequestHeader(value = "X-Admin-Key", required = false) String requestAdminKey) {
		checkAdminKey(requestAdminKey);
		return applicationService.findAll();
	}

	private void checkAdminKey(String requestAdminKey) {
		if (requestAdminKey == null || !adminKey.equals(requestAdminKey)) {
			throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Admin access required");
		}
	}

	public record CreateApplicationRequest(String serviceName) {
	}

	public record UpdateStatusRequest(String status) {
	}
}