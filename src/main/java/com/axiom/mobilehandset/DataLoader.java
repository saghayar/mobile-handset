package com.axiom.mobilehandset;

import com.axiom.mobilehandset.client.JsonAPIClient;
import com.axiom.mobilehandset.model.MobileHandset;
import com.axiom.mobilehandset.repository.MobileHandsetRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Objects.requireNonNull;

@Component
@Slf4j
public class DataLoader implements CommandLineRunner {

    private JsonAPIClient apiClient;

    private MobileHandsetRepository repository;

    private final ObjectMapper mapper;

    @Value("${app.switchToLocalFile}")
    private boolean switchToLocalFile;

    public DataLoader(JsonAPIClient apiClient,
                      MobileHandsetRepository repository) {
        this.apiClient = apiClient;
        this.repository = repository;
        this.mapper = new ObjectMapper();
    }

    @Override
    public void run(String... args) throws IOException, URISyntaxException {
        try {
            final List<MobileHandset> handsets = apiClient.fetchDeviceList();
            handsets.forEach(h -> repository.save(h));
        } catch (Exception e) {
            if (switchToLocalFile) {
                log.info("SWITCHING BACK TO LOCAL DATA , handsets.json \n");
                final List<MobileHandset> handsets = loadFromClassPath();
                handsets.forEach(h -> repository.save(h));
            }
        }
    }

    private List<MobileHandset> loadFromClassPath() throws IOException, URISyntaxException {
        return mapper.readValue(readJsonToString(), new TypeReference<List<MobileHandset>>() {
        });
    }

    private String readJsonToString() throws IOException, URISyntaxException {
        return new String(Files.readAllBytes(Paths.get(requireNonNull(getClass().getClassLoader()
                .getResource("handsets.json")).toURI())));
    }
}
