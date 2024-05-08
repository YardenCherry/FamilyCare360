package demo;

import java.util.Date;
import java.util.Map;

public class DemoBoundary {
	private String id;
	private String message;
	private Date messageTimestamp;
	private Double version;
	private StatusEnum status;
	private Map<String, Object> demoData;
	private NameBoundary name;

	public DemoBoundary() {
	}

	public DemoBoundary(String message) {
		this.message = message;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Date getMessageTimestamp() {
		return messageTimestamp;
	}

	public void setMessageTimestamp(Date messageTimestamp) {
		this.messageTimestamp = messageTimestamp;
	}

	public Double getVersion() {
		return version;
	}

	public void setVersion(Double version) {
		this.version = version;
	}

	public StatusEnum getStatus() {
		return status;
	}

	public void setStatus(StatusEnum status) {
		this.status = status;
	}

	public Map<String, Object> getDemoData() {
		return demoData;
	}

	public void setDemoData(Map<String, Object> demoData) {
		this.demoData = demoData;
	}

	public NameBoundary getName() {
		return name;
	}

	public void setName(NameBoundary name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "DemoBoundary ["
			+ "id=" + id 
			+ ", message=" + message 
			+ ", messageTimestamp=" + messageTimestamp
			+ ", version=" + version 
			+ ", status=" + status 
			+ ", demoData=" + demoData
			+ ", name=" + name
			+ "]";
	}

}
