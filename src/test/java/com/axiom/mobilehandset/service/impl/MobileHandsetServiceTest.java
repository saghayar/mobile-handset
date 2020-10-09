package com.axiom.mobilehandset.service.impl;

import com.axiom.mobilehandset.DataLoader;
import com.axiom.mobilehandset.client.JsonAPIClient;
import com.axiom.mobilehandset.model.MobileHandset;
import com.axiom.mobilehandset.repository.MobileHandsetRepository;
import com.axiom.mobilehandset.service.IMobileHandsetService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Rule;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import static java.util.Objects.requireNonNull;
import static org.hamcrest.CoreMatchers.equalTo;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class MobileHandsetServiceTest {
    private static final Integer PRICE_200 = 200;
    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private DataLoader dataLoader;
    @Autowired
    private MobileHandsetRepository repository;
    @Autowired
    private IMobileHandsetService handsetService;

    @Rule
    public ErrorCollector errorCollector = new ErrorCollector();

    @DisplayName("given list of mobile handset json file"
            + " when save object using MongoDB template"
            + " then object is saved")
    @Test
    public void shouldInsertGivenJsonDataSuccessfully() throws URISyntaxException, IOException {
        // given
        final List<MobileHandset> handsets = fromJsonString();

        // when
        saveRecordsToDb(handsets);

        // then
        final List<MobileHandset> result = handsetService.search(new LinkedMultiValueMap<>());
        errorCollector.checkThat(result.size(), equalTo(105));
    }

    @DisplayName("given list of mobile handset records"
            + " when search data with parameter ?priceEur=200"
            + " then retrieve 10 devices")
    @Test
    public void shouldReturnTenRecordsWhenFilteredByPriceEurIsEqualToTwoHundred() throws URISyntaxException, IOException {
        // given
        saveRecordsToDb(fromJsonString());

        // when
        final List<MobileHandset> result = handsetService.search(priceEurIsEqualTo200());

        // then
        errorCollector.checkThat(result.size(), equalTo(10));
    }

    @DisplayName("given list of mobile handset records in DB"
            + " when search data with parameter ?sim=eSim"
            + " then retrieve 18 devices")
    @Test
    public void shouldReturnEighteenRecordsWhenFilteredBySimIsEqualToESim() throws URISyntaxException, IOException {
        // given
        saveRecordsToDb(fromJsonString());

        // when
        final List<MobileHandset> result = handsetService.search(simIsEqualToESim());

        // then
        errorCollector.checkThat(result.size(), equalTo(18));
    }

    @DisplayName("given list of mobile handset records in DB"
            + " when search data with parameter ?announceDate=1999&price=200"
            + " then retrieve 2 devices")
    @Test
    public void shouldReturnTwoRecordsWhenFilteredByAnnounceDateAndPrice() throws URISyntaxException, IOException {
        // given
        saveRecordsToDb(fromJsonString());

        // when
        final List<MobileHandset> result = handsetService.search(announceDateAndPrice());

        // then
        errorCollector.checkThat(result.size(), equalTo(2));
    }

    private List<MobileHandset> fromJsonString() throws IOException, URISyntaxException {
        return mapper.readValue(readJsonToString(), new TypeReference<List<MobileHandset>>() {
        });
    }

    private void saveRecordsToDb(List<MobileHandset> handsets) {
        handsets.forEach(h -> repository.save(h));
    }

    private String readJsonToString() throws IOException, URISyntaxException {
        return new String(Files.readAllBytes(Paths.get(requireNonNull(getClass().getClassLoader()
                .getResource("list.json")).toURI())));
    }

    private MultiValueMap<String, Object> priceEurIsEqualTo200() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("priceEur", PRICE_200);
        return map;
    }

    private MultiValueMap<String, Object> simIsEqualToESim() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("sim", "eSim");
        return map;
    }

    private MultiValueMap<String, Object> announceDateAndPrice() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("announceDate", "1999");
        map.add("price", PRICE_200);
        return map;
    }
}