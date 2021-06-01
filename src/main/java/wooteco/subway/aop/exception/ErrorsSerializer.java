package wooteco.subway.aop.exception;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.springframework.boot.jackson.JsonComponent;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.io.IOException;

@JsonComponent
public class ErrorsSerializer extends JsonSerializer<Errors> {

    @Override
    public void serialize(Errors errors, JsonGenerator gen, SerializerProvider serializers)
        throws IOException {
        gen.writeStartArray();
        errors.getFieldErrors().forEach(error -> {
            try {
                gen.writeStartObject();
                fieldError(gen, error);
                gen.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        errors.getGlobalErrors().forEach(error -> {
            try {
                gen.writeStartObject();
                globalError(gen, error);
                gen.writeEndObject();
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
        gen.writeEndArray();
    }

    private void fieldError(JsonGenerator gen, FieldError error) throws IOException {
        gen.writeStringField("field", error.getField());
        globalError(gen, error);
    }

    private void globalError(JsonGenerator gen, ObjectError error) throws IOException {
        gen.writeStringField("code", error.getCode());
        gen.writeStringField("errorMessage", error.getDefaultMessage());
    }
}
