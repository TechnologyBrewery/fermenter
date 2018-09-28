package org.bitbucket.fermenter.stout.page;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.bitbucket.fermenter.stout.page.json.PageDeserializer;
import org.bitbucket.fermenter.stout.page.json.PageMixIn;
import org.bitbucket.fermenter.stout.page.json.PageSerializer;
import org.bitbucket.fermenter.stout.page.json.SortDeserializer;
import org.bitbucket.fermenter.stout.transfer.PageResponse;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate5.Hibernate5Module;

public class PageSerializeTest {

    private static ObjectMapper objectMapper;

    static {
        objectMapper = new ObjectMapper();
        configureObjectMapper();
    }

    /**
     * Adds in any custom modules desired for this project's
     * {@link ObjectMapper}.
     */
    private static void configureObjectMapper() {
        SimpleModule module = new SimpleModule();

        module.addAbstractTypeMapping(Page.class, PageResponse.class);
        module.setMixInAnnotation(Page.class, PageMixIn.class);
        module.addSerializer(Page.class, new PageSerializer());
        module.addDeserializer(Page.class, new PageDeserializer());
        module.addDeserializer(Sort.class, new SortDeserializer());

        objectMapper.registerModule(module);
        objectMapper.registerModule(new Hibernate5Module());
    }

    @Test
    public void serializeSort() throws JsonGenerationException, JsonMappingException, IOException {
        Writer jsonWriter = new StringWriter();
        String property = "name";
        Sort sort = new Sort(Sort.Direction.ASC, property);
        objectMapper.writeValue(jsonWriter, sort);
        String sortAsStr = jsonWriter.toString();
        assertEquals(sortAsStr,
                "[{\"direction\":\"ASC\",\"property\":\"name\",\"ignoreCase\":false,\"nullHandling\":\"NATIVE\",\"ascending\":true}]");

        Sort sortParsed = objectMapper.readValue(sortAsStr, Sort.class);
        assertEquals(sort.getOrderFor(property), sortParsed.getOrderFor(property));
    }
    
    
}
