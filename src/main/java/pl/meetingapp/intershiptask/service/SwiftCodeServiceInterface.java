package pl.meetingapp.intershiptask.service;

import pl.meetingapp.intershiptask.dto.AddSwiftCodeRequest;
import pl.meetingapp.intershiptask.dto.CountrySwiftCodesResponse;
import pl.meetingapp.intershiptask.dto.SwiftCodeResponse;

public interface SwiftCodeServiceInterface {

    /**
     * Ładuje kody SWIFT z pliku Excel przy starcie aplikacji.
     * Może wyrzucić wyjątek, jeśli coś pójdzie nie tak przy wczytywaniu danych.
     */
    void loadSwiftCodes() throws Exception;

    /**
     * Pobiera szczegóły dotyczące konkretnego kodu SWIFT.
     * @param swiftCode Kod SWIFT do wyszukania.
     * @return Szczegóły kodu SWIFT.
     */
    SwiftCodeResponse getSwiftCodeDetails(String swiftCode);

    /**
     * Pobiera listę kodów SWIFT dla danego kraju.
     * @param countryISO2code Kod ISO2 kraju.
     * @return Lista kodów SWIFT dla kraju.
     */
    CountrySwiftCodesResponse getSwiftCodesByCountry(String countryISO2code);

    /**
     * Dodaje nowy kod SWIFT do bazy danych.
     * @param request Obiekt z danymi kodu SWIFT.
     */
    void addSwiftCode(AddSwiftCodeRequest request);

    /**
     * Usuwa kod SWIFT z bazy danych na podstawie podanych szczegółów.
     * @param swiftCode Kod SWIFT do usunięcia.
     * @param bankName Nazwa banku.
     * @param countryISO2 Kod ISO2 kraju.
     */
    void deleteSwiftCode(String swiftCode, String bankName, String countryISO2);
}
