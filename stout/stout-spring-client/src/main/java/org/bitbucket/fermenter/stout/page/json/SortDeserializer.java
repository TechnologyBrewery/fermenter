package org.bitbucket.fermenter.stout.page.json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.domain.Sort.Order;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.ObjectCodec;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class SortDeserializer extends JsonDeserializer<Sort> {

    @Override
    public Sort deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        ObjectCodec oc = jp.getCodec();
        ArrayNode node = oc.readTree(jp);

        List<Order> orders = new ArrayList<>();
        for (JsonNode aNode : node) {
            Direction direction = Direction.fromString(aNode.get("direction").asText());
            String property = aNode.get("property").asText();
            Boolean ignoreCase = aNode.get("ignoreCase").asBoolean();
            Order order = null;
            if (ignoreCase) {
                order = new Order(direction, property).ignoreCase();
            } else {
                order = new Order(direction, property);
            }

            orders.add(order);
        }

        Sort sort = new Sort(orders);

        return sort;
    }

    @Override
    public Class<Sort> handledType() {
        return Sort.class;
    }
}
