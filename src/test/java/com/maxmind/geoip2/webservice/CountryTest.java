package com.maxmind.geoip2.webservice;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.LowLevelHttpRequest;
import com.google.api.client.http.LowLevelHttpResponse;
import com.google.api.client.testing.http.MockHttpTransport;
import com.google.api.client.testing.http.MockLowLevelHttpRequest;
import com.google.api.client.testing.http.MockLowLevelHttpResponse;
import static org.junit.Assert.*;

import com.maxmind.geoip2.exception.GeoIP2Exception;
import com.maxmind.geoip2.model.Country;
import com.maxmind.geoip2.webservice.Client;


public class CountryTest{
    Country country;

    @Before
    public void setUp() throws GeoIP2Exception {
        final String body = "{\"continent\":{" + "\"continent_code\":\"NA\","
                + "\"geoname_id\":42," + "\"names\":{\"en\":\"North America\"}"
                + "}," + "\"country\":{" + "\"geoname_id\":1,"
                + "\"iso_code\":\"US\"," + "\"confidence\":56,"
                + "\"names\":{\"en\":\"United States\"}" + "},"
                + "\"registered_country\":{" + "\"geoname_id\":2,"
                + "\"iso_code\":\"CA\"," + "\"names\":{\"en\":\"Canada\"}"
                + "}," + "\"traits\":{" + "\"ip_address\":\"1.2.3.4\"" + "}}";
        
        HttpTransport transport = new MockHttpTransport() {
            @Override
            public LowLevelHttpRequest buildRequest(String method, String url) throws IOException {
              return new MockLowLevelHttpRequest() {
                @Override
                public LowLevelHttpResponse execute() throws IOException {
                  MockLowLevelHttpResponse response = new MockLowLevelHttpResponse();
                  response.addHeader("Content-Length", String.valueOf(body.length()));
                  response.setStatusCode(200);
                  response.setContentType("application/vnd.maxmind.com-country"
                          + "+json; charset=UTF-8; version=1.0);");
                  response.setContent(body);
                  return response;
                }
              };
            }
          };
          Client client = new Client(42, "012345689", transport);
          
          country = client.country("1.1.1.1");
    }

    @Test
    public void testCountry() {
        assertEquals("country.getContinent().getCode() does not return NA",
                "NA", country.getContinent().getCode());
        assertEquals(
                "country.getContinent().getGeoNameId() does not return 42", 42,
                (int) country.getContinent().getGeoNameId());
        assertEquals(
                "country.getContinent().getName(\"en\") does not return North America",
                "North America", country.getContinent().getName("en"));
        assertEquals("country.getCountry().getCode() does not return US", "US",
                country.getCountry().getIsoCode());
        assertEquals("country.getCountry().getGeoNameId() does not return 1",
                1, (int) country.getCountry().getGeoNameId());
        assertEquals("country.getCountry().getConfidence() does not return 56",
                new Integer(56), country.getCountry().getConfidence());
        assertEquals(
                "country.getCountry().getName(\"en\") does not return United States",
                "United States", country.getCountry().getName("en"));

        assertEquals(
                "country.getRegisteredCountry().getCode() does not return CA",
                "CA", country.getRegisteredCountry().getIsoCode());
        assertEquals(
                "country.getRegisteredCountry().getGeoNameId() does not return 2",
                2, (int) country.getRegisteredCountry().getGeoNameId());
        assertEquals(
                "country.getRegisteredCountry().getName(\"en\") does not return United States",
                "Canada", country.getRegisteredCountry().getName("en"));
        assertEquals(
                "country.getTraits().getIpAddress does not return 1.2.3.4",
                "1.2.3.4", country.getTraits().getIpAddress());

    }

}