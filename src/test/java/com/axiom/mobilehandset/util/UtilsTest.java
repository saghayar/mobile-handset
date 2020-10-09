package com.axiom.mobilehandset.util;

import com.axiom.mobilehandset.DataLoader;
import com.axiom.mobilehandset.model.MobileHandset;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ErrorCollector;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

import java.util.List;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.notNullValue;

@SpringBootTest
@RunWith(SpringRunner.class)
public class UtilsTest {

    private static final Integer PRICE_EUR = 200;
    private static final String GPS_YES = "Yes";
    private static final String BRAND_NAME = "Apple";
    private static final String INVALID_VALUE = "INVALID VALUE";
    private static final String SIM = "e-Sim";
    //For multiple success/failed assertions
    @Rule
    public final ErrorCollector errorCollector = new ErrorCollector();
    @MockBean
    private DataLoader dataLoader;

    @Test
    public void getFieldDataTypeHappyScenario() {
        //Arrange
        Class<?>[] actualTypes = {Integer.class, String.class};

        //Act
        final Class<?> expectedType1 = Utils.getFieldDataType("priceEur", MobileHandset.class).get(0);
        final Class<?> expectedType2 = Utils.getFieldDataType("audioJack", MobileHandset.class).get(0);
        final Class<?> expectedType3 = Utils.getFieldDataType("resolution", MobileHandset.class).get(0);

        //Assert
        errorCollector.checkThat(expectedType1, is(equalTo(actualTypes[0])));
        errorCollector.checkThat(expectedType2, is(equalTo(actualTypes[1])));
        errorCollector.checkThat(expectedType3, is(equalTo(actualTypes[1])));
    }

    @Test
    public void getFieldDataTypeNegativeScenario() {
        //Act
        final List<Class<?>> types = Utils.getFieldDataType("abc123", MobileHandset.class);

        //Assert
        errorCollector.checkThat(types.size(), is(equalTo(0)));
    }

    @Test
    public void formattedParamsHappyScenario() {
        //Arrange
        final MultiValueMap<String, Object> inputParams = validInputParams();

        //Act
        final Map<String, Object> formattedParams = Utils.formattedParams(inputParams);

        //Assert
        final Integer price = Integer.parseInt(String.valueOf(formattedParams.get("release.priceEur")));
        final String gps = (String) formattedParams.get("hardware.gps");
        final String brand = (String) formattedParams.get("brand");

        errorCollector.checkThat(price, notNullValue());
        errorCollector.checkThat(price, is(equalTo(PRICE_EUR)));

        errorCollector.checkThat(gps, notNullValue());
        errorCollector.checkThat(gps, is(equalTo(GPS_YES)));

        errorCollector.checkThat(brand, notNullValue());
        errorCollector.checkThat(brand, is(equalTo(BRAND_NAME)));
    }

    @Test
    public void checkParamNameMatching() {
        //Arrange
        final MultiValueMap<String, Object> inputParams = validInputParamsMatchesPartially();

        //Act
        final Map<String, Object> formattedParams = Utils.formattedParams(inputParams);

        //Assert
        final Integer price = Integer.parseInt(String.valueOf(formattedParams.get("release.priceEur"))); // price -> release.priceEur
        final String sim = (String) formattedParams.get("sim");// SIM -> sim

        errorCollector.checkThat(price, notNullValue());
        errorCollector.checkThat(price, is(equalTo(PRICE_EUR)));

        errorCollector.checkThat(sim, notNullValue());
        errorCollector.checkThat(sim, is(equalTo(SIM)));
    }

    @Test
    public void formattedParamsCheckInvalidInput() {
        //Arrange
        final MultiValueMap<String, Object> inputParams = invalidInputParams();

        //Act
        final Map<String, Object> formattedParams = Utils.formattedParams(inputParams);

        //Assert
        errorCollector.checkThat(inputParams.size(), not(equalTo(0)));
        errorCollector.checkThat(formattedParams.size(), is(equalTo(0)));
    }

    private MultiValueMap<String, Object> validInputParams() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("brand", BRAND_NAME);
        map.add("priceEur", PRICE_EUR);
        map.add("gps", GPS_YES);
        return map;
    }

    //ex: price instead of priceEur (This case is mentioned in assignment task document)
    private MultiValueMap<String, Object> validInputParamsMatchesPartially() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("price", PRICE_EUR);
        map.add("SIM", SIM);
        return map;
    }

    private MultiValueMap<String, Object> invalidInputParams() {
        MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
        map.add("invalid_param_1", INVALID_VALUE);
        map.add("invalid_param_2", INVALID_VALUE);
        return map;
    }
}