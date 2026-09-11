package govone.backend.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BackendController {

	@GetMapping("/")
	public Map<String, String> home() {
		return Map.of(
				"application", "GovOne backend",
				"status", "running",
				"frontend", "http://localhost:5174",
				"api", "/api/applications");
	}
}