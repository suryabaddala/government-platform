package govone.backend.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "applications")
public class Application {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String applicationId;
	private String serviceName;
	private String status;
	private Instant createdAt;

	protected Application() {
	}

	public Application(String applicationId, String serviceName) {
		this.applicationId = applicationId;
		this.serviceName = serviceName;
		this.status = "PROCESSING";
		this.createdAt = Instant.now();
	}

	public String getApplicationId() {
		return applicationId;
	}

	public Long getId() {
		return id;
	}

	public String getServiceName() {
		return serviceName;
	}

	public String getStatus() {
		return status;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}
}