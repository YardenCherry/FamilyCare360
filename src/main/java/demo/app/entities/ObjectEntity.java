package demo.app.entities;

import java.util.Date;
import java.util.Map;

import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "OBJECTS")
public class ObjectEntity {

	@Id
	private String objectID;
	private String type;
	private String alias;
	private Boolean active;
	private Date creationTimestamp;
	private String createdBy;
	@Lob
	@Convert(converter = ApplicationMapToStringConverter.class)
	private Map<String, Object> objectDetails;
	public String location;

	public ObjectEntity() {
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getObjectID() {
		return objectID;
	}

	public void setObjectID(String objectID) {
		this.objectID = objectID;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAlias() {
		return alias;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}

	public Boolean getActive() {
		return active;
	}

	public void setActive(Boolean active) {
		this.active = active;
	}

	public Date getCreationTimestamp() {
		return creationTimestamp;
	}

	public void setCreationTimestamp(Date creationTimestamp) {
		this.creationTimestamp = creationTimestamp;
	}

	public String getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(String createdBy) {
		this.createdBy = createdBy;
	}

	public Map<String, Object> getObjectDetails() {
		return objectDetails;
	}

	public void setObjectDetails(Map<String, Object> objectDetails) {
		this.objectDetails = objectDetails;
	}

	@Override
	public String toString() {
		return "ObjectEntity{" + "objectID='" + objectID + '\'' + ", type='" + type + '\'' + ", alias='" + alias + '\''
				+ ", location='" + location + '\'' + ", active=" + active + ", creationTimestamp=" + creationTimestamp
				+ ", createdBy=" + createdBy + ", objectDetails=" + objectDetails + '}';
	}
}
