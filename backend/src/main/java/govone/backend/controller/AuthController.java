package govone.backend.controller;

import govone.backend.entity.User;
import govone.backend.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:5174"})
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	@ResponseStatus(HttpStatus.CREATED)
	public AuthResponse register(@RequestBody RegisterRequest request) {
		User user = userService.register(request.name(), request.email(), request.password());
		return new AuthResponse(user.getName(), user.getEmail());
	}

	@PostMapping("/login")
	public AuthResponse login(@RequestBody LoginRequest request) {
		User user = userService.login(request.email(), request.password());
		return new AuthResponse(user.getName(), user.getEmail());
	}

	public record RegisterRequest(String name, String email, String password) {
	}

	public record LoginRequest(String email, String password) {
	}

	public record AuthResponse(String name, String email) {
	}
}