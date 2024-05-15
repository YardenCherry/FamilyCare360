package Boundaries;

import demo.integrative.Documents.ObjectEntity;
import demo.integrative.Documents.UserEntity;
import demo.integrative.Objects.CreatedBy;
import demo.integrative.Objects.ObjectId;


import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ObjectBoundary {

    private ObjectId objectID;
    private String type;
    private String alias;
    private Boolean active;
    private Date creationTimeStamp;
    private CreatedBy createdBy;
    private Map<String, Object> objectDetails;

    public ObjectBoundary() {
    }

    public ObjectBoundary(ObjectEntity objectEntity) {
        String[] splitId = objectEntity.getObjectID().split("_");
        this.objectID = new ObjectId();
        this.objectID.setId(splitId[0]);
        this.objectID.setSuperApp(splitId[1]);
        setObjectDetails(objectEntity.getObjectDetails());
        setActive(objectEntity.getActive());
        setCreationTimeStamp(objectEntity.getCreationTimeStamp());
        setCreatedBy(objectEntity.getCreatedBy());
        setType(objectEntity.getType());
        setAlias(objectEntity.getAlias());
    }

    public ObjectId getObjectID() {
        return objectID;
    }

    public void setObjectID(ObjectId objectID) {
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

    public void setActive(Boolean active) {
        this.active = active;
    }

    public Date getCreationTimeStamp() {
        return creationTimeStamp;
    }

    public void setCreationTimeStamp(Date creationTimeStamp) {
        this.creationTimeStamp = creationTimeStamp;
    }

    public CreatedBy getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(CreatedBy createdBy) {
        this.createdBy = createdBy;
    }

    public Boolean getActive() {
        return active;
    }

    public Map<String, Object> getObjectDetails() {
        return objectDetails;
    }

    public void setObjectDetails(Map<String, Object> objectDetails) {
        this.objectDetails = objectDetails;
    }

    public ObjectEntity toEntity() {
        ObjectEntity objectEntity = new ObjectEntity();

        objectEntity.setObjectID(this.getObjectID().getId() + "_" + this.getObjectID().getSuperApp());
        objectEntity.setType(this.getType());
        objectEntity.setCreationTimeStamp(this.getCreationTimeStamp());
        objectEntity.setCreatedBy(this.getCreatedBy());
        objectEntity.setActive(this.getActive() == null || this.getActive());
        objectEntity.setAlias(this.getAlias() == null ? "demo instance" : this.getAlias());
        objectEntity.setObjectDetails(this.getObjectDetails() == null ? new HashMap<>() : this.getObjectDetails());


        return objectEntity;
    }

    @Override
    public String toString() {
        return "ObjectBoundary{" +
                "objectID=" + objectID +
                ", type='" + type + '\'' +
                ", alias='" + alias + '\'' +
                ", active=" + active +
                ", creationTimeStamp=" + creationTimeStamp +
                ", createdBy=" + createdBy +
                ", objectDetails=" + objectDetails +
                '}';
    }
}
