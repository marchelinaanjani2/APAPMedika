package apap.ti.hospitalization2206082770.config.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import apap.ti.hospitalization2206082770.model.Facility;

import java.io.IOException;
import java.util.UUID;

public class FacilityDeserializer extends JsonDeserializer<Facility> {

    @Override
    public Facility deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        String idAsString = p.getValueAsString();

        if (idAsString == null || idAsString.isEmpty()) {
            throw new JsonProcessingException("Invalid UUID string: null or empty") {
                private static final long serialVersionUID = 1L;
            };
        }

        UUID id;
        try {
            id = UUID.fromString(idAsString);
        } catch (IllegalArgumentException e) {
            throw new JsonProcessingException("Invalid UUID string: " + idAsString, e) {
                private static final long serialVersionUID = 1L;
            };
        }

        Facility facility = new Facility();
        facility.setId(id); 

        return facility;
    }
}
