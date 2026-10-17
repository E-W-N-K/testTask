package com.example.tetstask;

import com.example.tetstask.repository.HotelRepository;
import com.jayway.jsonpath.JsonPath;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class HotelApiIntegrationTests {

    private static final String MINSK_HOTEL = """
            {
              "name": "DoubleTree by Hilton Minsk",
              "description": "Hotel in the center of Minsk",
              "brand": "Hilton",
              "address": {
                "houseNumber": 9,
                "street": "Pobediteley Avenue",
                "city": "Minsk",
                "country": "Belarus",
                "postCode": "220004"
              },
              "contacts": {
                "phone": "+375 17 309-80-00",
                "email": "minsk@example.com"
              },
              "arrivalTime": {
                "checkIn": "14:00",
                "checkOut": "12:00"
              }
            }
            """;

    private static final String BREST_HOTEL = """
            {
              "name": "Hilton Garden Inn Brest",
              "brand": "Hilton",
              "address": {
                "houseNumber": 1,
                "street": "Central Street",
                "city": "Brest",
                "country": "Belarus",
                "postCode": "224000"
              },
              "contacts": {
                "phone": "+375 16 200-00-00",
                "email": "brest@example.com"
              },
              "arrivalTime": {
                "checkIn": "15:00"
              }
            }
            """;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private HotelRepository hotelRepository;

    @BeforeEach
    void cleanDatabase() {
        hotelRepository.deleteAll();
    }

    @Test
    void supportsFullHotelLifecycleAndQueryEndpoints() throws Exception {
        long minskHotelId = createHotel(MINSK_HOTEL);
        long brestHotelId = createHotel(BREST_HOTEL);

        mockMvc.perform(get("/property-view/hotels"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(2)))
                .andExpect(jsonPath("$[0].id", is((int) minskHotelId)))
                .andExpect(jsonPath("$[0].address", is("9 Pobediteley Avenue, Minsk, 220004, Belarus")))
                .andExpect(jsonPath("$[1].id", is((int) brestHotelId)));

        mockMvc.perform(get("/property-view/hotels/{id}", minskHotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is("DoubleTree by Hilton Minsk")))
                .andExpect(jsonPath("$.brand", is("Hilton")))
                .andExpect(jsonPath("$.address.city", is("Minsk")))
                .andExpect(jsonPath("$.arrivalTime.checkIn", is("14:00")))
                .andExpect(jsonPath("$.amenities", hasSize(0)));

        addAmenities(minskHotelId, "[\"Free WiFi\", \"Fitness center\", \"free wifi\"]");
        addAmenities(brestHotelId, "[\"Free WiFi\", \"Free parking\"]");

        mockMvc.perform(get("/property-view/hotels/{id}", minskHotelId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.amenities", containsInAnyOrder("Free WiFi", "Fitness center")));

        mockMvc.perform(get("/property-view/search")
                        .queryParam("name", "doubletree")
                        .queryParam("city", "MIN")
                        .queryParam("amenities", "wifi"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].id", is((int) minskHotelId)));

        mockMvc.perform(get("/property-view/histogram/brand"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Hilton", is(2)));

        mockMvc.perform(get("/property-view/histogram/city"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.Minsk", is(1)))
                .andExpect(jsonPath("$.Brest", is(1)));

        mockMvc.perform(get("/property-view/histogram/amenities"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$['Free WiFi']", is(2)))
                .andExpect(jsonPath("$['Fitness center']", is(1)))
                .andExpect(jsonPath("$['Free parking']", is(1)));
    }

    @Test
    void returnsStructuredErrorsForInvalidRequests() throws Exception {
        mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", is("Request validation failed")))
                .andExpect(jsonPath("$.validationErrors.name").exists())
                .andExpect(jsonPath("$.validationErrors.address").exists());

        mockMvc.perform(get("/property-view/hotels/999999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message", is("Hotel with id 999999 was not found")));

        mockMvc.perform(get("/property-view/histogram/name"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(
                        "Unsupported histogram parameter 'name'. Allowed values: brand, city, country, amenities"));
    }

    private long createHotel(String request) throws Exception {
        MvcResult result = mockMvc.perform(post("/property-view/hotels")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNumber())
                .andReturn();
        Number id = JsonPath.read(result.getResponse().getContentAsString(), "$.id");
        return id.longValue();
    }

    private void addAmenities(long hotelId, String request) throws Exception {
        mockMvc.perform(post("/property-view/hotels/{id}/amenities", hotelId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(request))
                .andExpect(status().isOk());
    }
}
