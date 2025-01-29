package pl.meetingapp.intershiptask.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

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
public class AddSwiftCodeRequest {

    private String address;

    @NotBlank(message = "Bank name cannot be blank or empty")
    private String bankName;

    @NotBlank(message = "Country ISO2 cannot be blank or empty")
    private String countryISO2;

    @NotBlank(message = "Country name cannot be blank or empty")
    private String countryName;

    private boolean isHeadquarter;

    @NotBlank(message = "SWIFT code cannot be blank or empty")
    // Przeczytałem ze SwiftCode musza miec min 8 znakow oraz max 11, nie wiem czy to prawda na potrzeby aplikacji uznajmy że tak
    @Size(min = 8, max = 11 ,message = "SWIFT code must be between 8 and 11 characters long")
    private String swiftCode;

    public AddSwiftCodeRequest(String address, String bankName, String countryISO2, String countryName, boolean isHeadquarter, String swiftCode) {
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.isHeadquarter = isHeadquarter;
        this.swiftCode = swiftCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public @NotBlank String getBankName() {
        return bankName;
    }

    public void setBankName(@NotBlank String bankName) {
        this.bankName = bankName;
    }

    public @NotBlank String getCountryISO2() {
        return countryISO2;
    }

    public void setCountryISO2(@NotBlank String countryISO2) {
        this.countryISO2 = countryISO2;
    }

    public @NotBlank String getCountryName() {
        return countryName;
    }

    public void setCountryName(@NotBlank String countryName) {
        this.countryName = countryName;
    }

    public boolean isHeadquarter() {
        return isHeadquarter;
    }

    public void setHeadquarter(boolean headquarter) {
        isHeadquarter = headquarter;
    }

    public @NotBlank String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(@NotBlank String swiftCode) {
        this.swiftCode = swiftCode;
    }
}
