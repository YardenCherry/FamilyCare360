package demo.app.entities;

import java.util.Map;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class ApplicationMapToStringConverter implements AttributeConverter<Map<String, Object>, String> {
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
			return this.jackson.writeValueAsString(attribute);
		} catch (Exception e) {
			throw new RuntimeException("Error converting map to JSON string.", e);
		}
	}

	@Override
	public Map<String, Object> convertToEntityAttribute(String dbData) {
		try {
			return this.jackson.readValue(dbData, new TypeReference<Map<String, Object>>() {
			});
		} catch (Exception e) {
			throw new RuntimeException("Error converting JSON string to map.", e);
		}
	}
}