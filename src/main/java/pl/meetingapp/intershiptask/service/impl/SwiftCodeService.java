package pl.meetingapp.intershiptask.service.impl;

import jakarta.annotation.PostConstruct;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.meetingapp.intershiptask.dto.AddSwiftCodeRequest;
import pl.meetingapp.intershiptask.dto.CountrySwiftCodesResponse;
import pl.meetingapp.intershiptask.dto.SwiftCodeResponse;
import pl.meetingapp.intershiptask.model.SwiftCode;
import pl.meetingapp.intershiptask.repository.SwiftCodeRepository;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import pl.meetingapp.intershiptask.service.SwiftCodeServiceInterface;

import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SwiftCodeService implements SwiftCodeServiceInterface {

    private final SwiftCodeRepository swiftCodeRepository;

    @Autowired
    public SwiftCodeService(SwiftCodeRepository swiftCodeRepository) {
        this.swiftCodeRepository = swiftCodeRepository;
    }

    /**
     * Wczytuje dane SWIFT z pliku Excel przy starcie aplikacji.
     */
    @Override
    @PostConstruct
    public void loadSwiftCodes() throws Exception {
        Resource resource = new ClassPathResource("Interns_2025_SWIFT_CODESzmala.xlsx");

        try (InputStream fis = resource.getInputStream();
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);
            for (Row row : sheet) {
                if (row.getRowNum() == 0) {
                    continue;
                }

                String countryISO2 = row.getCell(0).getStringCellValue().toUpperCase();
                String swiftCode = row.getCell(1).getStringCellValue().toUpperCase();
                String bankName = row.getCell(3).getStringCellValue();
                String address = row.getCell(4).getStringCellValue();
                String countryName = row.getCell(6).getStringCellValue().toUpperCase();
                Boolean isHeadquarter = swiftCode.endsWith("XXX");

                SwiftCode swiftCodeEntity = new SwiftCode();
                swiftCodeEntity.setCountryISO2(countryISO2);
                swiftCodeEntity.setSwiftCode(swiftCode);
                swiftCodeEntity.setBankName(bankName);
                swiftCodeEntity.setAddress(address);
                swiftCodeEntity.setCountryName(countryName);
                swiftCodeEntity.setHeadquarter(isHeadquarter);

                swiftCodeRepository.save(swiftCodeEntity);
            }
        }
    }


    /**
     * Pobiera szczegóły dotyczące kodu SWIFT.
     *
     * @param swiftCode Kod SWIFT do wyszukania.
     * @return Szczegóły kodu SWIFT.
     * @Walidacja: Sprawdzam, czy taki kod istnieje w bazie danych. Jeżeli nie, rzucany jest wyjątek: "SWIFT code not found".
     */
    @Override
    public SwiftCodeResponse getSwiftCodeDetails(String swiftCode) {
        String swiftCodeUpper = swiftCode.toUpperCase();

        SwiftCode swiftCodeEntity = swiftCodeRepository.findBySwiftCode(swiftCodeUpper)
                .orElseThrow(() -> new IllegalArgumentException("SWIFT code not found"));

        boolean isHeadquarter = isHeadquarter(swiftCodeEntity);

        SwiftCodeResponse dto = createSwiftCodeResponse(swiftCodeEntity, true);

        if (isHeadquarter) {
            List<SwiftCodeResponse> branches = swiftCodeRepository.findBySwiftCodeStartingWith(swiftCodeUpper.substring(0, 8))
                    .stream()
                    .filter(branch -> !isHeadquarter(branch))
                    .map(branch -> createSwiftCodeResponse(branch, false))
                    .collect(Collectors.toList());
            dto.setBranches(branches);
        } else {
            dto.setBranches(null);
        }

        return dto;
    }

    /**
     * Pobiera listę kodów SWIFT dla danego kraju.
     *
     * @param countryISO2code Kod ISO2 kraju.
     * @return Lista kodów SWIFT dla kraju.
     * @Walidacja: Sprawdzam, czy lista krajów dla podanego kodu ISO2 nie jest pusta.
     */
    @Override
    public CountrySwiftCodesResponse getSwiftCodesByCountry(String countryISO2code) {
        String countryCodeUpper = countryISO2code.toUpperCase();

        List<SwiftCode> countryExists = swiftCodeRepository.findByCountryISO2(countryCodeUpper);
        if (countryExists.isEmpty()) {
            throw new IllegalArgumentException("Country ISO2 code not found in the database");
        }

        List<SwiftCode> swiftCodes = swiftCodeRepository.findByCountryISO2(countryCodeUpper);

        List<SwiftCodeResponse> swiftCodeResponses = swiftCodes
                .stream()
                .map(swiftCode -> createSwiftCodeResponse(swiftCode, false))
                .collect(Collectors.toList());

        return new CountrySwiftCodesResponse(
                countryCodeUpper,
                swiftCodes.isEmpty() ? null : swiftCodes.get(0).getCountryName(),
                swiftCodeResponses
        );
    }

    /**
     * Dodaje nowy kod SWIFT do bazy danych.
     *
     * @param request Obiekt z danymi kodu SWIFT.
     * @Walidacja: Sprawdzam, czy kod SWIFT ma co najmniej 8 znaków lub czy istnieje już w bazie danych.
     */
    @Override
    public void addSwiftCode(AddSwiftCodeRequest request) {
        String swiftCodeUpper = request.getSwiftCode().toUpperCase();

        Optional<SwiftCode> existingSwiftCode = swiftCodeRepository.findBySwiftCode(swiftCodeUpper);

        if (existingSwiftCode.isPresent()) {
            throw new IllegalArgumentException("SWIFT code already exists");
        }

        SwiftCode swiftCode = new SwiftCode();
        swiftCode.setSwiftCode(swiftCodeUpper);
        swiftCode.setBankName(request.getBankName());
        swiftCode.setAddress(request.getAddress());
        swiftCode.setCountryISO2(request.getCountryISO2().toUpperCase());
        swiftCode.setCountryName(request.getCountryName().toUpperCase());
        swiftCode.setHeadquarter(request.isHeadquarter());

        swiftCodeRepository.save(swiftCode);
    }

    /**
     * Usuwa kod SWIFT na podstawie podanych szczegółów.
     *
     * @param swiftCode  Kod SWIFT do usunięcia.
     * @param bankName   Nazwa banku.
     * @param countryISO2 Kod ISO2 kraju.
     * @Walidacja: Sprawdzam, czy cały rekord z podanym kodem SWIFT, nazwą banku oraz kodem ISO2 kraju istnieje w bazie danych.
     */
    @Override
    public void deleteSwiftCode(String swiftCode, String bankName, String countryISO2) {
        String swiftCodeUpper = swiftCode.toUpperCase();
        String countryISO2Upper = countryISO2.toUpperCase();

        Optional<SwiftCode> existingSwiftCode = swiftCodeRepository.findBySwiftCodeAndBankNameAndCountryISO2(
                swiftCodeUpper, bankName, countryISO2Upper
        );

        if (existingSwiftCode.isEmpty()) {
            throw new RuntimeException("SWIFT code not found or does not match the provided details.");
        }

        swiftCodeRepository.delete(existingSwiftCode.get());
    }

    /**
     * Sprawdza, czy kod SWIFT dotyczy głównej siedziby.
     */
    private boolean isHeadquarter(SwiftCode swiftCode) {
        return swiftCode.getSwiftCode().endsWith("XXX");
    }

    /**
     * Konwertuje encję SwiftCode na obiekt DTO.
     *
     * @param swiftCode Encja SwiftCode, która ma zostać przekształcona na obiekt DTO.
     * @param includeCountryName Boolean, który decyduje, czy nazwa kraju (countryName) ma być uwzględniona odpowiedzi.
     * - Jeśli true, nazwa kraju będzie dołączona do odpowiedzi.
     * - Jeśli false, nazwa kraju nie będzie uwzględniona w odpowiedzi.
     * @return Obiekt DTO SwiftCodeResponse.
     */
    private SwiftCodeResponse createSwiftCodeResponse(SwiftCode swiftCode, boolean includeCountryName) {
        return new SwiftCodeResponse(
                swiftCode.getAddress(),
                swiftCode.getBankName(),
                swiftCode.getCountryISO2(),
                includeCountryName ? swiftCode.getCountryName().toUpperCase() : null,
                swiftCode.isHeadquarter(),
                swiftCode.getSwiftCode(),
                null
        );
    }
}
