package com.rdv.server.core.util;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * @author davidgarcia
 */
@Component
public class JsonUtil {

    private static final String JSON_EXTENSION = ".json";
    private static final String TEXTS_DIRECTORY = "texts";

    private final ResourceLoader resourceLoader;

    public JsonUtil(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }


    public String readJSonFile(String languageCode) throws IOException {
        String text;
        String jsonFileName = TEXTS_DIRECTORY + File.separator + languageCode + JSON_EXTENSION;
        Resource textFile = resourceLoader.getResource("classpath:"+jsonFileName);

        try (Scanner scanner = new Scanner(textFile.getInputStream(), StandardCharsets.UTF_8)) {
            text = scanner.useDelimiter("\\A").next();
        }

        return text;
    }

}
