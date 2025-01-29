package pl.meetingapp.intershiptask.controller.impl;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import pl.meetingapp.intershiptask.controller.SwiftCodeControllerInterface;
import pl.meetingapp.intershiptask.dto.AddSwiftCodeRequest;
import pl.meetingapp.intershiptask.dto.CountrySwiftCodesResponse;
import pl.meetingapp.intershiptask.dto.SwiftCodeResponse;
import pl.meetingapp.intershiptask.service.impl.SwiftCodeService;

import java.util.Map;

@RestController
@RequestMapping("/v1/swift-codes")
public class SwiftCodeController implements SwiftCodeControllerInterface {

    private final SwiftCodeService swiftCodeService;

    @Autowired // Wstrzykiwanie zależności przez konstruktor
    public SwiftCodeController(SwiftCodeService swiftCodeService) {
        this.swiftCodeService = swiftCodeService;
    }

    /**
     * Pobiera szczegóły dotyczące kodu SWIFT.
     *
     * @param swiftCode Kod SWIFT, dla którego mają być pobrane szczegóły.
     * @return Szczegóły dotyczące danego kodu SWIFT.
     */
    @GetMapping("/{swift-code}")
    public SwiftCodeResponse getSwiftCodeDetails(
            @PathVariable("swift-code") String swiftCode
    ) {
        return swiftCodeService.getSwiftCodeDetails(swiftCode);
    }

    /**
     * Pobiera listę kodów SWIFT dla danego kraju.
     *
     * @param countryISO2code Kod ISO2 kraju, dla którego mają być pobrane kody SWIFT.
     * @return Lista kodów SWIFT przypisanych do danego kraju.
     */
    @GetMapping("/country/{countryISO2code}")
    public CountrySwiftCodesResponse getSwiftCodesByCountry(
            @PathVariable String countryISO2code
    ) {
        return swiftCodeService.getSwiftCodesByCountry(countryISO2code);
    }

    /**
     * Dodaje nowy kod SWIFT do bazy danych.
     *
     * @param request Obiekt zawierający dane kodu SWIFT do dodania (walidowane).
     * @return Informacja o sukcesie dodania kodu SWIFT.
     */
    @PostMapping("/")
    public ResponseEntity<?> addSwiftCode(
            @Valid @RequestBody AddSwiftCodeRequest request
    ) {
        swiftCodeService.addSwiftCode(request);
        return ResponseEntity.ok(Map.of("message", "SWIFT code added successfully"));
    }

    /**
     * Usuwa kod SWIFT z bazy danych na podstawie parametrów.
     *
     * @param swiftCode  Kod SWIFT do usunięcia.
     * @param bankName   Nazwa banku, do którego przypisany jest kod SWIFT.
     * @param countryISO2 Kod ISO2 kraju, w którym działa bank z tym kodem SWIFT.
     * @return Informacja o sukcesie usunięcia kodu SWIFT.
     */
    @DeleteMapping("/{swift-code}")
    public ResponseEntity<?> deleteSwiftCode(
            @PathVariable("swift-code") String swiftCode,
            @RequestParam("bankName") String bankName,
            @RequestParam("countryISO2") String countryISO2
    ) {
        swiftCodeService.deleteSwiftCode(swiftCode, bankName, countryISO2);
        return ResponseEntity.ok(Map.of("message", "SWIFT code deleted successfully"));
    }
}
