package pl.meetingapp.intershiptask.unit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;
import pl.meetingapp.intershiptask.controller.impl.SwiftCodeController;
import pl.meetingapp.intershiptask.dto.AddSwiftCodeRequest;
import pl.meetingapp.intershiptask.dto.CountrySwiftCodesResponse;
import pl.meetingapp.intershiptask.dto.SwiftCodeResponse;
import pl.meetingapp.intershiptask.service.impl.SwiftCodeService;

import java.util.Objects;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class SwiftCodeControllerUnitTest {

    @Mock
    private SwiftCodeService swiftCodeService;

    @InjectMocks
    private SwiftCodeController swiftCodeController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

     /**
     Test dla poprawnego pobierania szczegółów
     **/
    @Test
    void testGetSwiftCodeDetails() {
        String swiftCode = "12345678";
        SwiftCodeResponse response = new SwiftCodeResponse();
        when(swiftCodeService.getSwiftCodeDetails(swiftCode)).thenReturn(response);

        SwiftCodeResponse result = swiftCodeController.getSwiftCodeDetails(swiftCode);

        assertNotNull(result);
        verify(swiftCodeService, times(1)).getSwiftCodeDetails(swiftCode);
    }

    /**
     Test dla poprawnego pobierania banków dla kraju
     **/
    @Test
    void testGetSwiftCodesByCountry() {
        String countryISO2code = "PL";
        CountrySwiftCodesResponse response = new CountrySwiftCodesResponse();
        when(swiftCodeService.getSwiftCodesByCountry(countryISO2code)).thenReturn(response);

        CountrySwiftCodesResponse result = swiftCodeController.getSwiftCodesByCountry(countryISO2code);

        assertNotNull(result);
        verify(swiftCodeService, times(1)).getSwiftCodesByCountry(countryISO2code);
    }

    /**
     Test dla poprawnego dodawania do nowych rekordów do bazy danych
     **/
    @Test
    void testAddSwiftCode() {
        AddSwiftCodeRequest request = new AddSwiftCodeRequest("Krakowaska 11", "Testowy Bank", "PL", "Poland", true, "SWFT1234XXX");
        doNothing().when(swiftCodeService).addSwiftCode(request);

        ResponseEntity<?> responseEntity = swiftCodeController.addSwiftCode(request);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(responseEntity.getBody().toString().contains("SWIFT code added successfully"));
        verify(swiftCodeService, times(1)).addSwiftCode(request);
    }

     /**
     Test dla poprawnego usuwania rekordów z bazy danych
     **/
    @Test
    void testDeleteSwiftCode() {
        String swiftCode = "12345678";
        String bankName = "Testowy Bank";
        String countryISO2 = "PL";
        doNothing().when(swiftCodeService).deleteSwiftCode(swiftCode, bankName, countryISO2);

        ResponseEntity<?> responseEntity = swiftCodeController.deleteSwiftCode(swiftCode, bankName, countryISO2);

        assertEquals(200, responseEntity.getStatusCodeValue());
        assertTrue(Objects.requireNonNull(responseEntity.getBody()).toString().contains("SWIFT code deleted successfully"));
        verify(swiftCodeService, times(1)).deleteSwiftCode(swiftCode, bankName, countryISO2);
    }

    /**
     Test przypadku, gdy nie istnieje rekord w bazie dancyh do usunięcia
     **/
    @Test
    void testDeleteNonExistentSwiftCode() {
        // Arrange
        String swiftCode = "12";
        String bankName = "Testowy bank";
        String countryISO2 = "PL";
        doThrow(new RuntimeException("SWIFT code not found or does not match the provided details."))
                .when(swiftCodeService).deleteSwiftCode(swiftCode, bankName, countryISO2);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> swiftCodeController.deleteSwiftCode(swiftCode, bankName, countryISO2));

        assertEquals("SWIFT code not found or does not match the provided details.", exception.getMessage());
        verify(swiftCodeService, times(1)).deleteSwiftCode(swiftCode, bankName, countryISO2);
    }

     /**
     Test przypadku, gdy nieprawidłowy kod kraju jest przekazany
     **/
    @Test
    void testGetSwiftCodesByInvalidCountry() {
        String countryISO2code = " ";
        doThrow(new IllegalArgumentException("Country ISO2 code not found in the database"))
                .when(swiftCodeService).getSwiftCodesByCountry(countryISO2code);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> swiftCodeController.getSwiftCodesByCountry(countryISO2code));

        assertEquals("Country ISO2 code not found in the database", exception.getMessage());
        verify(swiftCodeService, times(1)).getSwiftCodesByCountry(countryISO2code);
    }
}
