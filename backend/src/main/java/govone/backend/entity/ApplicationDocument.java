package govone.backend.entity;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "application_documents")
public class ApplicationDocument {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String applicationId;
	private String originalFilename;
	private String storedFilename;
	private String contentType;
	private long size;
	private Instant uploadedAt;

	protected ApplicationDocument() {
	}

	public ApplicationDocument(String applicationId, String originalFilename, String storedFilename,
			String contentType, long size) {
		this.applicationId = applicationId;
		this.originalFilename = originalFilename;
		this.storedFilename = storedFilename;
		this.contentType = contentType;
		this.size = size;
		this.uploadedAt = Instant.now();
	}

	public Long getId() {
		return id;
	}

	public String getApplicationId() {
		return applicationId;
	}

	public String getOriginalFilename() {
		return originalFilename;
	}

	public String getStoredFilename() {
		return storedFilename;
	}

	public String getContentType() {
		return contentType;
	}

	public long getSize() {
		return size;
	}

	public Instant getUploadedAt() {
		return uploadedAt;
	}
}