package org.bitbucket.fermenter.ale.mda.generator.angular;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class AngularGeneratorUtil {

    public static final String ANGULAR_SRC_FOLDER_FOR_APP = "src/app/";
    
    public static final String TYPE_NOT_FOUND = "TYPE_NOT_FOUND";
    private static final Map<String, String> fermenterTypeToAngularTypeMap;
    static {
        Map<String, String> map = new HashMap<>();
        map.put("date", "Date");
        map.put("timestamp", "Date");
        map.put("integer", "number");
        map.put("long", "number");
        map.put("big_decimal", "number");
        map.put("boolean", "boolean");
        map.put("numeric_boolean", "boolean");
        map.put("string", "string");
        map.put("character", "string");
        map.put("uuid", "string");
        map.put("geospatial_point", "string");
        map.put("blob", "string");
        map.put("void", "undefined");
        fermenterTypeToAngularTypeMap = Collections.unmodifiableMap(map);
    }

    public static final String getAngularType(String fermenterType) {
        String type = TYPE_NOT_FOUND;
        if(!StringUtils.isBlank(fermenterType) && fermenterTypeToAngularTypeMap.containsKey(fermenterType)) {
            type = fermenterTypeToAngularTypeMap.get(fermenterType);
        } else if(!StringUtils.isBlank(fermenterType)) {
            // if the type is passed in such as "SimpleDomain" then return that type
            type = fermenterType;
        }
        return type;
    }

    public static boolean isBaseType(String type) {
        return fermenterTypeToAngularTypeMap.containsKey(type);
    }
}
