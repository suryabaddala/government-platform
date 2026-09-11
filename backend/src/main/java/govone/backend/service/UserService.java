package govone.backend.service;

import govone.backend.entity.User;
import govone.backend.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UserService {

	private final UserRepository userRepository;
	private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	public UserService(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	public User register(String name, String email, String password) {
		validate(name, email, password);
		String normalizedEmail = email.trim().toLowerCase();
		if (userRepository.existsByEmail(normalizedEmail)) {
			throw new ResponseStatusException(HttpStatus.CONFLICT, "Email is already registered");
		}
		return userRepository.save(new User(name.trim(), normalizedEmail, passwordEncoder.encode(password)));
	}

	public User login(String email, String password) {
		if (email == null || password == null) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email and password are required");
		}
		User user = userRepository.findByEmail(email.trim().toLowerCase())
				.filter(candidate -> passwordEncoder.matches(password, candidate.getPasswordHash()))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password"));
		return user;
	}

	private void validate(String name, String email, String password) {
		if (name == null || name.isBlank() || email == null || email.isBlank() || password == null || password.length() < 6) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Name, email and a password of at least 6 characters are required");
		}
	}
}