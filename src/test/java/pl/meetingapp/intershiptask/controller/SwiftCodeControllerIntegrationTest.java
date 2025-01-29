package pl.meetingapp.intershiptask.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class SwiftCodeControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    /**
     * Testuje poprawne pobieranie szczegółów SWIFT code.
     * Sprawdza, czy odpowiedź zawiera odpowiednie pola i sprawdza ich typy.
     **/
    @Test
    void testGetSwiftCodeDetails() throws Exception {
        String swiftCode = "BPKOPLPWXXX";

        mockMvc.perform(get("/v1/swift-codes/{swift-code}", swiftCode))
                .andExpect(status().isOk())

                // Sprawdzam, czy pole "swiftCode" istnieje i jest typu string
                .andExpect(jsonPath("$.swiftCode").exists())
                .andExpect(jsonPath("$.swiftCode").isString())

                // Sprawdzam, czy pole "address" istnieje i jest typu string
                .andExpect(jsonPath("$.address").exists())
                .andExpect(jsonPath("$.address").isString())

                // Sprawdzam, czy pole "bankName" istnieje i jest typu string
                .andExpect(jsonPath("$.bankName").exists())
                .andExpect(jsonPath("$.bankName").isString())

                // Sprawdzam, czy pole "countryISO2" istnieje i jest typu string
                .andExpect(jsonPath("$.countryISO2").exists())
                .andExpect(jsonPath("$.countryISO2").isString())

                // Sprawdzam, czy pole "countryName" istnieje i jest typu string
                .andExpect(jsonPath("$.countryName").exists())
                .andExpect(jsonPath("$.countryName").isString())

                // Sprawdzam, czy pole "isHeadquarter" istnieje i jest typu boolean
                .andExpect(jsonPath("$.isHeadquarter").exists())
                .andExpect(jsonPath("$.isHeadquarter").isBoolean())

                // Sprawdzam, czy pole "branches" istnieje i jest tablicą
                .andExpect(jsonPath("$.branches").exists())
                .andExpect(jsonPath("$.branches").isArray());
    }

    /**
     * Testuje sytuację, gdy kod SWIFT nie istnieje w bazie danych.
     * Sprawdza, czy odpowiedź ma status 404 i zwraca komunikat o braku SWIFT code.
     **/
    @Test
    void testGetSwiftCodeDetailsNotFound() throws Exception {
        String invalidSwiftCode = "INVALIDCODE";

        mockMvc.perform(get("/v1/swift-codes/{swift-code}", invalidSwiftCode))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("SWIFT code not found"));
    }

    /**
     * Testuje pobieranie listy SWIFT codes dla danego kraju.
     * Sprawdza, czy odpowiedź zawiera odpowiednie pola i sprawdza ich typy.
     **/
    @Test
    void testGetSwiftCodesByCountry() throws Exception {
        String countryISO2code = "PL";

        mockMvc.perform(get("/v1/swift-codes/country/{countryISO2code}", countryISO2code))
                .andExpect(status().isOk())

                // Sprawdzam, czy pole "countryISO2" istnieje i jest typu string
                .andExpect(jsonPath("$.countryISO2").exists())
                .andExpect(jsonPath("$.countryISO2").isString())

                // Sprawdzam, czy pole "countryName" istnieje i jesty typu string
                .andExpect(jsonPath("$.countryName").exists())
                .andExpect(jsonPath("$.countryName").isString())

                // Sprawdzam, czy pole "swiftCodes" istnieje jest tablicą
                .andExpect(jsonPath("$.swiftCodes").exists())
                .andExpect(jsonPath("$.swiftCodes").isArray());
    }

    /**
     * Testuje sytuację, gdy nieprawidłowy kod kraju jest przekazany.
     * Sprawdza, czy odpowiedź ma status 404 i zwraca komunikat o braku kodu kraju w bazie danych.
     **/
    @Test
    void testGetSwiftCodesByCountryNotFound() throws Exception {
        String invalidCountryCode = "XX";

        mockMvc.perform(get("/v1/swift-codes/country/{countryISO2code}", invalidCountryCode))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Country ISO2 code not found in the database"));
    }

    /**
     * Testuje dodawanie nowego kodu SWIFT do bazy danych.
     * Sprawdza, czy odpowiedź zawiera status 200 i komunikat o powodzeniu.
     **/
    @Test
    void testAddSwiftCode() throws Exception {
        String requestBody = """
            {
                "swiftCode": "GRECE12345",
                "bankName": "Test Bank",
                "address": "Test Street",
                "countryISO2": "GR",
                "countryName": "Grece",
                "isHeadquarter": false
            }
            """;

        mockMvc.perform(post("/v1/swift-codes/")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SWIFT code added successfully"));
    }

    /**
     * Testuje sytuację, gdy przekazany kod SWIFT jest nieprawidłowy (w tym tescie za krótki).
     * Sprawdza, czy odpowiedź zawiera status 400 i komunikat o błędzie walidacji.
     **/
    @Test
    void testAddSwiftCodeWithInvalidSwiftCode() throws Exception {
        String invalidRequestBody = """
            {
                "swiftCode": "XX",
                "bankName": "Test Bank",
                "address": "Test Street",
                "countryISO2": "GR",
                "countryName": "Grece",
                "isHeadquarter": false
            }
            """;

        mockMvc.perform(post("/v1/swift-codes/")
                        .contentType("application/json")
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed: swiftCode - SWIFT code must be between 8 and 11 characters long;"));
    }

    /**
     * Testuje sytuację, gdy kod SWIFT jest pusty.
     * Sprawdza, czy odpowiedź zawiera status 400 i czy jest zgodna z oczekiwaniami,
     * czyli nie pozwala na przesłanie pustego pola "swiftCode".
     **/
    @Test
    void testAddSwiftCodeWithEmptySwiftCode() throws Exception {
        String invalidRequestBody = """
        {
            "swiftCode": "",
            "bankName": "Test Bank",
            "address": "Test Street",
            "countryISO2": "GR",
            "countryName": "Grece",
            "isHeadquarter": false
        }
        """;

        mockMvc.perform(post("/v1/swift-codes/")
                        .contentType("application/json")
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest());

    }

    /**
     * Testuje sytuacje, gdy brak jest wymaganego pola (w tym tescie brak swiftCode).
     * Sprawdza, czy odpowiedź zawiera status 400 i zwraca komunikat o błędzie walidacji.
     **/
    @Test
    void testAddSwiftCodeWithoutSwiftCode() throws Exception {
        String invalidRequestBody = """
        {
            "bankName": "Test Bank",
            "address": "Test Street",
            "countryISO2": "GR",
            "countryName": "Grece",
            "isHeadquarter": false
        }
        """;

        mockMvc.perform(post("/v1/swift-codes/")
                        .contentType("application/json")
                        .content(invalidRequestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed: swiftCode - SWIFT code cannot be blank or empty;"));
    }

    /**
     * Testuje sytuację, gdy pole swiftCode jest za długie.
     * Sprawdza, czy aplikacja reaguje na przekroczenie dopuszczalnej długości pola.
     **/
    @Test
    void testAddSwiftCodeWithLongSwiftCode() throws Exception {
        String longSwiftCode = "GREGTESTWITHLONGSWIFTCODE123";

        String requestBody = """
        {
            "swiftCode": "%s",
            "bankName": "Test Bank",
            "address": "Test Street",
            "countryISO2": "GR",
            "countryName": "Grece",
            "isHeadquarter": false
        }
        """.formatted(longSwiftCode);

        mockMvc.perform(post("/v1/swift-codes/")
                        .contentType("application/json")
                        .content(requestBody))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed: swiftCode - SWIFT code must be between 8 and 11 characters long;"));
    }

    /**
     * Testuje usuwanie istniejącego kodu SWIFT z bazy danych.
     * Sprawdza, czy odpowiedź zawiera status 200 i komunikat o powodzeniu.
     **/
    @Test
    void testDeleteSwiftCode() throws Exception {
        String swiftCode = "BPKOPLPWXXX";
        String bankName = "PKO BANK POLSKI S.A.";
        String countryISO2 = "PL";

        mockMvc.perform(delete("/v1/swift-codes/{swift-code}", swiftCode)
                        .param("bankName", bankName)
                        .param("countryISO2", countryISO2))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("SWIFT code deleted successfully"));
    }

    /**
     * Testuje sytuację, gdy próbujemy usunąć nieistniejący kod SWIFT.
     * Sprawdza, czy odpowiedź zawiera status 500 i komunikat o błędzie.
     **/
    @Test
    void testDeleteSwiftCodeNotFound() throws Exception {
        String invalidSwiftCode = "INVALIDCODE";
        String bankName = "Nonexistent Bank";
        String countryISO2 = "XX";

        mockMvc.perform(delete("/v1/swift-codes/{swift-code}", invalidSwiftCode)
                        .param("bankName", bankName)
                        .param("countryISO2", countryISO2))
                .andExpect(status().isInternalServerError())
                .andExpect(jsonPath("$.message").value("SWIFT code not found or does not match the provided details."));
    }
}