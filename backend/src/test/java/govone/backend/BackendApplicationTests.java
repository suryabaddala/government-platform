package govone.backend;

import govone.backend.entity.Application;
import govone.backend.service.ApplicationService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.springframework.web.server.ResponseStatusException;

@SpringBootTest
class BackendApplicationTests {

	@Autowired
	private ApplicationService applicationService;

	@Test
	void contextLoads() {
	}

	@Test
	void createsAndUpdatesApplicationStatus() {
		Application created = applicationService.create("Certificates");

		assertEquals("PROCESSING", created.getStatus());

		Application updated = applicationService.updateStatus(created.getApplicationId(), "approved");

		assertEquals("APPROVED", updated.getStatus());
		assertEquals("APPROVED", applicationService.findByApplicationId(created.getApplicationId()).getStatus());
	}

	@Test
	void rejectsUnsupportedApplicationStatus() {
		Application created = applicationService.create("Housing");

		assertThrows(ResponseStatusException.class,
				() -> applicationService.updateStatus(created.getApplicationId(), "REJECTED"));
	}

}
