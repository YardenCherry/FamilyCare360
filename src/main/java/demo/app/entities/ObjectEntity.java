package demo.app.entities;

import java.util.Date;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "OBJECTS")
public class ObjectEntity {

	@Id
	private String objectId;
	private String type;
	private String alias;
	private Boolean active;
	private Date creationTimestamp;
	private String createdBy;
	@Convert(converter = ApplicationMapToStringConverter.class)
	@Column(name = "objectDetails", columnDefinition = "TEXT")
	private Map<String, Object> objectDetails;

	private double latitude;
	private double longitude;

	public ObjectEntity() {

	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public String getObjectId() {
		return objectId;
	}

	public void setObjectId(String objectId) {
		this.objectId = objectId;
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
		return "ObjectEntity{" + "objectId='" + objectId + '\'' + ", type='" + type + '\'' + ", alias='" + alias + '\''
				+ ", latitude='" + latitude + ", longitude='" + longitude + '\'' + ", active=" + active
				+ ", creationTimestamp=" + creationTimestamp + ", createdBy=" + createdBy + ", objectDetails="
				+ objectDetails + '}';
	}

	public void setLocation(double latitude, double longitude) {
		setLatitude(latitude);
		setLongitude(longitude);
	}

}
