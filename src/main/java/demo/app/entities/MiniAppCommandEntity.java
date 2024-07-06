package demo.app.entities;

import java.util.Date;
import java.util.Map;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Entity
@Table(name = "COMMAND_TABLE")
public class MiniAppCommandEntity {

	@Id
	private String commandId;
	private String miniAppName; // to search specific miniapp by name
	private String command;
	private String targetObject;
	@Temporal(TemporalType.TIMESTAMP)
	private Date invocationTimeStamp;

	private String invokedBy;

	@Convert(converter = ApplicationMapToStringConverter.class)
	@Column(name = "commandAttributes", columnDefinition = "TEXT")
	private Map<String, Object> commandAttributes;

	public MiniAppCommandEntity() {
	}

	public String getCommandId() {
		return commandId;
	}

	public void setCommandId(String commandId) {
		this.commandId = commandId;
	}

	public String getMiniAppName() {
		return miniAppName;
	}

	public void setMiniAppName(String miniAppName) {
		this.miniAppName = miniAppName;
	}

	public String getTargetObject() {
		return targetObject;
	}

	public void setTargetObject(String targetObject) {
		this.targetObject = targetObject;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public Date getInvocationTimeStamp() {
		return invocationTimeStamp;
	}

	public void setInvocationTimeStamp(Date invocationTimeStamp) {
		this.invocationTimeStamp = invocationTimeStamp;
	}

	public String getInvokedBy() {
		return invokedBy;
	}

	public void setInvokedBy(String invokedBy) {
		this.invokedBy = invokedBy;
	}

	public Map<String, Object> getCommandAttributes() {
		return commandAttributes;
	}

	public void setCommandAttributes(Map<String, Object> commandAttributes) {
		this.commandAttributes = commandAttributes;
	}

	@Override
	public String toString() {
		return "CommandEntity [commandId=" + commandId + ", miniAppName=" + miniAppName + ", targtObject="
				+ targetObject + ", command=" + command + ", invocationTimeStamp=" + invocationTimeStamp
				+ ", invokedBy=" + invokedBy + ", commandAttributes=" + commandAttributes + "]";
	}

}
