package pl.meetingapp.intershiptask.dto;

import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.List;
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
public class SwiftCodeResponse {

    private String address;
    private String bankName;
    private String countryISO2;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String countryName;
    private Boolean isHeadquarter;
    private String swiftCode;

    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<SwiftCodeResponse> branches;

    public SwiftCodeResponse(String address, String bankName, String countryISO2, String countryName, Boolean isHeadquarter, String swiftCode, List<SwiftCodeResponse> branches) {
        this.address = address;
        this.bankName = bankName;
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.isHeadquarter = isHeadquarter;
        this.swiftCode = swiftCode;
        this.branches = branches;
    }

    public SwiftCodeResponse() {

    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBankName() {
        return bankName;
    }

    public void setBankName(String bankName) {
        this.bankName = bankName;
    }

    public String getCountryISO2() {
        return countryISO2;
    }

    public void setCountryISO2(String countryISO2) {
        this.countryISO2 = countryISO2;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public Boolean getIsHeadquarter() {
        return isHeadquarter;
    }

    public void setIsHeadquarter(Boolean isHeadquarter) {
        this.isHeadquarter = isHeadquarter;
    }

    public String getSwiftCode() {
        return swiftCode;
    }

    public void setSwiftCode(String swiftCode) {
        this.swiftCode = swiftCode;
    }

    public List<SwiftCodeResponse> getBranches() {
        return branches;
    }

    public void setBranches(List<SwiftCodeResponse> branches) {
        this.branches = branches;
    }
}
