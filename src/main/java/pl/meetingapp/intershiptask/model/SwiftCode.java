package pl.meetingapp.intershiptask.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GenerationType;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;

/**
 * @Getter
 * @Setter
 *
 * @Lombok: Chciałem użyć biblioteki Lombok do automatycznego generowania getterów i setterów,
 * ale napotkałem problem, ponieważ moja aplikacja nie rozpoznaje tych metod,
 * mimo że Maven oraz sama aplikacja budują się poprawnie.
 * Próbowałem wielu sposobów, aby to naprawić, ale problem leży chyba w kompilatorze
 * IntelliJ, którego używam. Dlatego ręcznie dodaję getter i setter.
 **/
@Entity
@Table(name = "swift_codes")
@NoArgsConstructor
public class SwiftCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "swift_code", nullable = false)
    private String swiftCode;

    @Column(name = "bank_name", nullable = false)
    private String bankName;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "country_iso2", nullable = false)
    private String countryISO2;

    @Column(name = "country_name", nullable = false)
    private String countryName;

    @Column(name = "is_headquarter", nullable = false)
    private Boolean isHeadquarter;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public String getCountryISO2() {
        return countryISO2;
    }

    public void setCountryISO2(String countryISO2) {
        this.countryISO2 = countryISO2;
    }

    public Boolean isHeadquarter() {
        return isHeadquarter;
    }

    public void setHeadquarter(Boolean headquarter) {
        this.isHeadquarter = headquarter;
    }

}
