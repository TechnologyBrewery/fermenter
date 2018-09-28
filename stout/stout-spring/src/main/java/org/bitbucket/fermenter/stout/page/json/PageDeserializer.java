package org.bitbucket.fermenter.stout.page.json;

import java.io.IOException;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class PageDeserializer extends JsonDeserializer<Page> {

    @Override
    public Page deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        
        Integer number = null;
        Integer size = null;
        Integer totalPages;
        Integer numberOfElements;
        Long totalElements = null;
        Boolean previousPage = null;
        Boolean first;
        Boolean nextPage;
        Boolean last;
        List content = null;
        Sort sort = null;
        

        while (jp.nextToken() != JsonToken.END_OBJECT) {
            String fieldName = jp.getCurrentName();
            switch(fieldName) {
            case PageMixIn.NUMBER:
                number = jp.getIntValue();
                break;
            case PageMixIn.SIZE:
                size = jp.getIntValue();
                break;
            case PageMixIn.TOTAL_PAGES:
                totalPages = jp.getIntValue();
                break;
            case PageMixIn.NUMBER_OF_ELEMENTS:
                numberOfElements = jp.getIntValue();
                break;
            case PageMixIn.TOTAL_ELEMENTS:
                totalElements = jp.getLongValue();
                break;
            case PageMixIn.PREVIOUS_PAGE:
                previousPage = jp.getBooleanValue();
                break;
            case PageMixIn.NEXT_PAGE:
                nextPage = jp.getBooleanValue();
                break;
            case PageMixIn.LAST:
                last = jp.getBooleanValue();
                break;
            case PageMixIn.CONTENT:
                content = convertJsonToContent(jp);
                break;
            case PageMixIn.SORT:
                sort = ctxt.readValue(jp, Sort.class);
                break;
            }
        }
        
        Pageable pageable = new PageableImpl(number, size, 0, previousPage, sort);
        PageImpl page = new PageImpl(content, pageable, totalElements);
        
        return page;
    }
    
    private List convertJsonToContent(JsonParser jp) {
        // TODO Auto-generated method stub
        return null;
    }

    protected void handleRequiredFieldMissing(String fieldName) throws IOException {
        throw new IOException("Required field [" + fieldName + "] from serialized Message JSON missing");
    }
}
