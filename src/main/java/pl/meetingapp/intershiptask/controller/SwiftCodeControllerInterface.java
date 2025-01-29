package pl.meetingapp.intershiptask.controller;

import pl.meetingapp.intershiptask.dto.AddSwiftCodeRequest;
import pl.meetingapp.intershiptask.dto.CountrySwiftCodesResponse;
import pl.meetingapp.intershiptask.dto.SwiftCodeResponse;
import org.springframework.http.ResponseEntity;

public interface SwiftCodeControllerInterface {

    /**
     * Pobiera szczegóły dotyczące kodu SWIFT.
     *
     * @param swiftCode Kod SWIFT, dla którego mają być pobrane szczegóły.
     * @return Szczegóły dotyczące danego kodu SWIFT.
     */
    SwiftCodeResponse getSwiftCodeDetails(String swiftCode);

    /**
     * Pobiera listę kodów SWIFT dla danego kraju.
     *
     * @param countryISO2code Kod ISO2 kraju, dla którego mają być pobrane kody SWIFT.
     * @return Lista kodów SWIFT przypisanych do danego kraju.
     */
    CountrySwiftCodesResponse getSwiftCodesByCountry(String countryISO2code);

    /**
     * Dodaje nowy kod SWIFT do bazy danych.
     *
     * @param request Obiekt zawierający dane kodu SWIFT do dodania (walidowane).
     * @return Informacja o sukcesie dodania kodu SWIFT.
     */
    ResponseEntity<?> addSwiftCode(AddSwiftCodeRequest request);

    /**
     * Usuwa kod SWIFT z bazy danych na podstawie parametrów.
     *
     * @param swiftCode  Kod SWIFT do usunięcia.
     * @param bankName   Nazwa banku, do którego przypisany jest kod SWIFT.
     * @param countryISO2 Kod ISO2 kraju, w którym działa bank z tym kodem SWIFT.
     * @return Informacja o sukcesie usunięcia kodu SWIFT.
     */
    ResponseEntity<?> deleteSwiftCode(String swiftCode, String bankName, String countryISO2);
}
