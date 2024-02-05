package com.example.JDRSlistin;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootApplication
public class JdrSlistinApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(JdrSlistinApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("hereeeee");
		String json = """
				{
					"glossary": {
						"title": "example glossary",
						"GlossDiv": {
							"title": 12.5,
							"GlossList": {
								"GlossEntry": {
									"ID": "SGML",
									"SortAs": 452,
									"GlossTerm": "Standard Generalized Markup Language",
									"Acronym": "SGML",
									"Abbrev": "ISO 8879:1986",
									"GlossDef": {
										"para": "A meta-markup language, used to create markup languages such as DocBook.",
										"GlossSeeAlso": ["GML", "XML"]
									},
									"GlossSee": "markup"
								}
							}
						}
					}
				}""";

		Map<String, String> matches = new HashMap<>();

		Pattern bracketOpenPattern = Pattern.compile(":\\s*\\{");
		Pattern quoteOpenPattern = Pattern.compile(":\\s*\\\"");

		Pattern pattern = Pattern.compile("\"(\\w+)\":\\s*([0-9]+(?:\\.[0-9]+)?)");
		Matcher patternMatcher = pattern.matcher(json);
		while (patternMatcher.find()) {
			matches.put(patternMatcher.group(1), patternMatcher.group(2));
		}

		extractNumericValues(json);
	}

	public Map<String, Number> extractNumericValues(String jsonString) {
		Map<String, Number> numericValues = new HashMap<>();

		try {
			ObjectMapper objectMapper = new ObjectMapper();
			JsonNode rootNode = objectMapper.readTree(jsonString);

			extractNumericValuesFromNode(rootNode, "", numericValues);
		} catch (IOException e) {
			e.printStackTrace();
		}

		return numericValues;
	}

	private void extractNumericValuesFromNode(JsonNode node, String currentPath, Map<String, Number> numericValues) {
		Iterator<Map.Entry<String, JsonNode>> fields = node.fields();
		while (fields.hasNext()) {
			Map.Entry<String, JsonNode> entry = fields.next();
			String fieldName = entry.getKey();
			JsonNode fieldValue = entry.getValue();
			String fullPath = currentPath.isEmpty() ? fieldName : currentPath + "." + fieldName;

			if (fieldValue.isNumber()) {
				numericValues.put(fullPath, fieldValue.numberValue());
				publishDataElement(fullPath, fieldValue.numberValue().toString());
			} else if (fieldValue.isObject()) {
				extractNumericValuesFromNode(fieldValue, fullPath, numericValues);

			} else if (fieldValue.isArray()) {
				for (JsonNode field : fieldValue) {
					extractNumericValuesFromNode(field, fullPath, numericValues);
				}
			}
		}
	}

	private void publishDataElement(String name, String value) {

		System.out.println(name + " " + value);
		// String topic = OUTPUT_TOPIC_PREFIX + name;
		// kafkaTemplate.send(topic, name, value);
	}

	public void findParentNodes(String j) {
		StringBuilder parents = new StringBuilder("");
		StringBuilder json = new StringBuilder(j);

		for (int i = json.length() - 1; i > 0; i--) {
			if (json.charAt(i) == '{') {

			}
		}
	}

}
