package fr.insee.queen.application.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class JsonTestHelper {

    private final static String DATA_FOLDER = "data/";

    public JsonTestHelper() {
        throw new IllegalArgumentException("Utility class");
    }

    /**
     * Reads given resource file as a string.
     *
     * @param fileName relative path to the resource file from dataset folder
     * @return the file's contents
     * @throws IOException if read fails for any reason
     */
    public static String getResourceFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(DATA_FOLDER + fileName)) {
            if (is == null) return null;
            try (InputStreamReader isr = new InputStreamReader(is);
                 BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    public static ArrayNode getResourceFileAsArrayNode(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(classLoader.getResourceAsStream(DATA_FOLDER + fileName), ArrayNode.class);
    }

    public static ObjectNode getResourceFileAsObjectNode(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(classLoader.getResourceAsStream(DATA_FOLDER + fileName), ObjectNode.class);
    }

    public static String getObjectAsJsonString(Object object) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.writeValueAsString(object);
    }
}
