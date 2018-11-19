package org.bitbucket.fermenter.stout.page.json;

import java.io.IOException;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class PageSerializer extends JsonSerializer<Page>{

    @Override
    public void serialize(Page page, JsonGenerator jsonGenerator, SerializerProvider arg2) throws IOException {
        jsonGenerator.writeStartObject();
        jsonGenerator.writeObjectField(PageMixIn.CONTENT, page.getContent());
        jsonGenerator.writeBooleanField(PageMixIn.FIRST, page.isFirst());
        jsonGenerator.writeBooleanField(PageMixIn.LAST, page.isLast());
        jsonGenerator.writeNumberField(PageMixIn.TOTAL_PAGES, page.getTotalPages());
        jsonGenerator.writeNumberField(PageMixIn.TOTAL_ELEMENTS, page.getTotalElements());
        jsonGenerator.writeNumberField(PageMixIn.NUMBER_OF_ELEMENTS, page.getNumberOfElements());
        jsonGenerator.writeNumberField(PageMixIn.SIZE, page.getSize());
        jsonGenerator.writeNumberField(PageMixIn.NUMBER, page.getNumber());
        
        Sort sort = page.getSort();
        if(sort != null) {
            jsonGenerator.writeArrayFieldStart(PageMixIn.SORT);
            for (Sort.Order order : sort) {
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("property", order.getProperty());
                jsonGenerator.writeStringField("direction", order.getDirection().name());
                jsonGenerator.writeBooleanField("ignoreCase", order.isIgnoreCase());
                jsonGenerator.writeStringField("nullHandling", order.getNullHandling().name());
                jsonGenerator.writeEndObject();
            }
            jsonGenerator.writeEndArray();    
        }
        
        jsonGenerator.writeEndObject();
    }

}
