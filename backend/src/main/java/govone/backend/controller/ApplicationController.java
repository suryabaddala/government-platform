package govone.backend.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import govone.backend.entity.Application;
import govone.backend.service.ApplicationService;

@RestController
@RequestMapping("/api/applications")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class ApplicationController {

	private final ApplicationService applicationService;

	public ApplicationController(ApplicationService applicationService) {
		this.applicationService = applicationService;
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
	public Application updateStatus(@PathVariable String applicationId, @RequestBody UpdateStatusRequest request) {
		return applicationService.updateStatus(applicationId, request.status());
	}

	public record CreateApplicationRequest(String serviceName) {
	}

	public record UpdateStatusRequest(String status) {
	}
}