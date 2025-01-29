package pl.meetingapp.intershiptask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.meetingapp.intershiptask.model.SwiftCode;

import java.util.List;
import java.util.Optional;

public interface SwiftCodeRepository extends JpaRepository<SwiftCode, Long> {
    Optional<SwiftCode> findBySwiftCode(String swiftCode);
    List<SwiftCode> findBySwiftCodeStartingWith(String swiftCodePrefix);
    List<SwiftCode> findByCountryISO2(String countryISO2);
    Optional<SwiftCode> findBySwiftCodeAndBankNameAndCountryISO2(String swiftCode, String bankName, String countryISO2);
}
