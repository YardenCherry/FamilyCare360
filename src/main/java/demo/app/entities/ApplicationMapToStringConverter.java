package demo.app.entities;

import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;

public class ApplicationMapToStringConverter
		implements AttributeConverter<Map<String, Object>, String> {
	// this utility class is part of JACKSON dependency that performs marshalling /
	// unmarshalling from Java objects to JSON and vice versa
	private ObjectMapper jackson;

	public ApplicationMapToStringConverter() {
		super();
		this.jackson = new ObjectMapper();
	}

	@Override
	public String convertToDatabaseColumn(Map<String, Object> attribute) {
		try {
			return this.jackson
					.writeValueAsString(attribute);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> convertToEntityAttribute(String dbData) {
		try {
			return this.jackson
					.readValue(dbData, Map.class);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}