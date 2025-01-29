package pl.meetingapp.intershiptask.dto;

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
public class CountrySwiftCodesResponse {

    private String countryISO2;
    private String countryName;
    private List<SwiftCodeResponse> swiftCodes;

    public CountrySwiftCodesResponse(String countryISO2, String countryName, List<SwiftCodeResponse> swiftCodes) {
        this.countryISO2 = countryISO2;
        this.countryName = countryName;
        this.swiftCodes = swiftCodes;
    }

    public CountrySwiftCodesResponse() {

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

    public List<SwiftCodeResponse> getSwiftCodes() {
        return swiftCodes;
    }

    public void setSwiftCodes(List<SwiftCodeResponse> swiftCodes) {
        this.swiftCodes = swiftCodes;
    }
}
