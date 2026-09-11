package govone.backend.service;

import java.util.concurrent.ThreadLocalRandom;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import govone.backend.entity.Application;
import govone.backend.repository.ApplicationRepository;

@Service
public class ApplicationService {

	private final ApplicationRepository applicationRepository;

	public ApplicationService(ApplicationRepository applicationRepository) {
		this.applicationRepository = applicationRepository;
	}

	public Application create(String serviceName) {
		if (serviceName == null || serviceName.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "serviceName is required");
		}

		String applicationId;
		do {
			applicationId = "GOV" + ThreadLocalRandom.current().nextInt(100000, 1000000);
		} while (applicationRepository.existsByApplicationId(applicationId));

		return applicationRepository.save(new Application(applicationId, serviceName.trim()));
	}

	public Application findByApplicationId(String applicationId) {
		return applicationRepository.findByApplicationId(applicationId.trim().toUpperCase())
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Application not found"));
	}

	public List<Application> findAll() {
		return applicationRepository.findAll();
	}

	public Application updateStatus(String applicationId, String status) {
		if (status == null || status.isBlank()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "status is required");
		}

		String normalizedStatus = status.trim().toUpperCase();
		if (!normalizedStatus.equals("SUBMITTED") && !normalizedStatus.equals("VERIFIED")
				&& !normalizedStatus.equals("PROCESSING") && !normalizedStatus.equals("APPROVED")) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unsupported application status");
		}

		Application application = findByApplicationId(applicationId);
		application.updateStatus(normalizedStatus);
		return applicationRepository.save(application);
	}
}