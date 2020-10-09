package com.axiom.mobilehandset.util;

import com.axiom.mobilehandset.model.MobileHandset;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.util.MultiValueMap;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

public class Utils {

    private static final Class<?> DOCUMENT_TYPE = MobileHandset.class;
    private static final String PACKAGE_NAME = "com.axiom.mobilehandset.model";

    private Utils() {
    }

    /**
     * @param key  field name
     * @param type Document type
     * @return data type of field , works all inner fields dynamically , ex: priceEur -> release.priceEur->Integer
     */
    public static List<Class<?>> getFieldDataType(String key, Class type) {
        final List<Class<?>> types = new ArrayList<>();
        Arrays.stream(type.getDeclaredFields()).forEach(field -> {
            final String pckName = field.getType().getPackage().getName();
            if (pckName.equals(PACKAGE_NAME)) {
                types.addAll(getFieldDataType(key, field.getType()));
            } else {
                if (extractKey(key).equals(field.getName())) {
                    types.add(field.getType());
                }
            }
        });
        return types;
    }

    /**
     * @param queryParameters query param from user
     * @return parameters with sub document name , ex : priceEur -> release.priceEur
     */
    public static Map<String, Object> formattedParams(MultiValueMap<String, Object> queryParameters) {
        Map<String, Object> formattedParams = new HashMap<>();
        populateParams(formattedParams, queryParameters, DOCUMENT_TYPE);
        return formattedParams;
    }

    /**
     * @param parameters      result to be added
     * @param queryParameters query param from user
     * @param type            can be Document or SubDocument type
     *                        Method iterates through all fields recursively ,setting subdocument
     *                        name to key in order to build dynamic query
     */
    private static void populateParams(Map<String, Object> parameters,
                                       MultiValueMap<String, Object> queryParameters,
                                       Class<?> type) {
        //query param key list
        final Set<String> keyList = queryParameters.keySet();

        Arrays.stream(type.getDeclaredFields()).forEach(field -> {
            //Using package name to identify wrapper type in order to keep login generic ,any new field can be added to
            // document/subdocument and it won't affect logic
            final String pckName = field.getType().getPackage().getName();
            if (!field.getType().isPrimitive() && pckName.equals(PACKAGE_NAME)) {
                //recursive call to match all inner fields dynamically
                populateParams(parameters, queryParameters, field.getType());
            } else {
                final Optional<String> matchingKeyOpt = checkForMatchingKey(keyList, field);
                if (queryParameters.containsKey(field.getName())) {
                    //Checking key equality first
                    parameters.put(getKey(field, type), queryParameters.getFirst(field.getName()));
                } else
                    //If it matches partially , ex: priceEur versus price
                    matchingKeyOpt.ifPresent(key -> parameters.put(getKey(field, type), queryParameters.getFirst(key)));
            }
        });
    }

    /**
     * @param keyList query param key set
     * @param field   field to compare with
     * @return checks key matching , ex : price -> priceEur ,
     */
    //Note : this is one of the test cases mentioned in assignment task document document
    private static Optional<String> checkForMatchingKey(Set<String> keyList, Field field) {
        return keyList.stream().filter(key -> field.getName().toLowerCase().contains(key.toLowerCase())).findFirst();
    }

    /**
     * @param field field
     * @param type  document/sub document type
     * @return append sub document name to field name ex: priceEur -> release.priceEur
     */
    private static String getKey(Field field, Class<?> type) {
        if (!type.isAnnotationPresent(Document.class)) {
            return type.getSimpleName().toLowerCase() + "." + field.getName();
        }
        return field.getName();
    }

    /**
     * @param key field name
     * @return removes sub document name from key if required
     */
    private static String extractKey(String key) {
        final int index = key.lastIndexOf('.');
        if (index != -1) {
            return key.substring(index + 1);
        }
        return key;
    }
}
