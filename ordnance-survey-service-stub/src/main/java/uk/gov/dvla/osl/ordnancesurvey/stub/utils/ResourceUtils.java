package uk.gov.dvla.osl.ordnancesurvey.stub.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URLDecoder;
import java.util.Optional;

public final class ResourceUtils {
    private static final String RESPONSE_FOLDER = "responses";

    private ResourceUtils() {
        throw new AssertionError("Attempting to instantiate a static utilities class");
    }

    public static Optional<String> loadResponseAsString(final String data) {
        try {
            // Define our response placeholder for our content.
            StringBuilder content = new StringBuilder();

            // Prepare the resource for in-memory streaming.
            String resourceToLoad = String.format("%s/%s.json", RESPONSE_FOLDER, URLDecoder.decode(data, "UTF-8").trim().replaceAll("[,\\s]+", "_"));

            InputStream resourceStream = ClassLoader.getSystemClassLoader().getResourceAsStream(resourceToLoad);
            if (resourceStream != null) {
                try (BufferedReader READER = new BufferedReader(new InputStreamReader(resourceStream))) {
                    // Read the resource content into memory.
                    String currentLine;
                    while ((currentLine = READER.readLine()) != null) {
                        content.append(currentLine);
                    }
                }
            }

            // Check we actually found some content worth processing.
            if (content.toString().trim().length() == 0) {
                throw new IOException(String.format("No content found for [%s]...", resourceToLoad));
            }

            // Return the pre-defined response.
            return Optional.of(content.toString().trim());

        } catch (Exception ex) {
            return Optional.empty();
        }
    }
}
