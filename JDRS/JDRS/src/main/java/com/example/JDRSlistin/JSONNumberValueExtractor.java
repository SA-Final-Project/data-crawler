package com.example.JDRSlistin;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JSONNumberValueExtractor {
    private JSONNumberValueExtractor() {

    }

    public static List<String[]> extractNumericValues(String jsonString) {
        List<String[]> numericValues = new ArrayList<>();

        Pattern pattern = Pattern.compile("\"(\\w+)\":\\s*([0-9]+(?:\\.[0-9]+)?)");
        Matcher patternMatcher = pattern.matcher(jsonString);
        while (patternMatcher.find()) {
            numericValues.add(new String[] { patternMatcher.group(1), patternMatcher.group(2) });
        }

        // extractNumericValues(json);

        // t
        // ObjectMapper objectMapper = new ObjectMapper();
        // JsonNode rootNode = objectMapper.readTree(jsonStri

        // extractNumericValuesFromNode(rootNode, "", numericValu
        // } catch (IOException e) {
        // e.printStackTrace();
        // }

        return numericValues;

    }

    private static void extractNumericValuesFromNode(JsonNode node, String currentPath,
            List<String[]> numericValues) {
        Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
        while (fields.hasNext()) {
            Map.Entry<String, JsonNode> entry = fields.next();
            String fieldName = entry.getKey();
            JsonNode fieldValue = entry.getValue();
            String fullPath = currentPath.isEmpty() ? fieldName : currentPath + "." + fieldName;

            if (fieldValue.isNumber()) {
                numericValues.add(new String[] { fullPath, fieldValue.toString() });
            } else if (fieldValue.isObject()) {
                extractNumericValuesFromNode(fieldValue, fullPath, numericValues);

            } else if (fieldValue.isArray()) {
                for (JsonNode field : fieldValue) {
                    extractNumericValuesFromNode(field, fullPath, numericValues);
                }
            }
        }
    }
}
