package govone.backend.service;

import java.util.concurrent.ThreadLocalRandom;

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
}