package demo.app.boundaries;

import demo.app.entities.ObjectEntity;
import demo.app.objects.CreatedBy;
import demo.app.objects.Location;
import demo.app.objects.ObjectId;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;



public class ObjectBoundary {

    private ObjectId objectID;
    private String type;
    private String alias;
    private Location location;
    private Boolean active;
    private Date creationTimeStamp;
    private CreatedBy createdBy;
    private Map<String, Object> objectDetails;

    	
        public ObjectBoundary() {
        	objectDetails = new HashMap<>();
    	}

    	public ObjectId getObjectId() {
    		return objectID;
    	}

    	public void setObjectId(ObjectId objectId) {
    		this.objectID = objectId;
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

    	public Date getCreationTimesTamp() {
    		return creationTimeStamp;
    	}

    	public void setCreationTimesTamp(Date creationTimeStamp) {
    		this.creationTimeStamp = creationTimeStamp;
    	}

    	public CreatedBy getCreatedBy() {
    		return createdBy;
    	}

    	public void setCreatedBy(CreatedBy createdBy) {
    		this.createdBy = createdBy;
    	}

    	public Map<String, Object> getObjectDetails() {
    		return objectDetails;
    	}

    	public void setObjectDetails(Map<String, Object> objectDetails) {
    		this.objectDetails = objectDetails;
    	}

    	public Location getLocation() {
    		return location;
    	}
    	
    	public void setLocation(Location location) {
    		this.location = location;
    	}
    		
    	@Override
    	public String toString() {
    		return "ObjectBoundary [objectId=" + objectID 
    				+ ", type=" + type 
    				+ ", alias=" + alias 
    				+ ", active=" + active
    				+ ", location= " + location
    				+ ", creationTimeStamp=" + creationTimeStamp 
    				+ ", createdBy=" + createdBy 
    				+ ", objectDetails=" + objectDetails + "]";
    		
    	}
}
