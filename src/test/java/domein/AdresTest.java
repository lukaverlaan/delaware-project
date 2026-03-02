package domein;

import com.example._026javag03.domein.Adres;
import com.example._026javag03.exceptions.AdresException;
import com.example._026javag03.util.AdresAtrributes;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Set;
import java.util.stream.Stream;

import static com.example._026javag03.util.AdresAtrributes.*;
import static org.junit.jupiter.api.Assertions.*;

class AdresTest {

    private static Stream<org.junit.jupiter.params.provider.Arguments> geldigeAdressen() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of("Kerkstraat", "10", null, 1000, "Brussel"),
                org.junit.jupiter.params.provider.Arguments.of("Stationstraat", "25", "A", 2000, "Antwerpen")
        );
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> ongeldigeAdressen() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(null, "10", null, 1000, "Brussel", Set.of(STRAAT)),
                org.junit.jupiter.params.provider.Arguments.of("Kerkstraat", "", null, 1000, "Brussel", Set.of(HUISNUMMER)),
                org.junit.jupiter.params.provider.Arguments.of("Kerkstraat", "10", null, 999, "Brussel", Set.of(POSTCODE)),
                org.junit.jupiter.params.provider.Arguments.of("Kerkstraat", "10", null, 1000, "", Set.of(STAD))
        );
    }

    @ParameterizedTest
    @MethodSource("geldigeAdressen")
    void maakAdres_geldig(String straat, String huisnr, String postbus,
                          int postcode, String stad) throws AdresException {

        Adres adres = Adres.adresBuilder()
                .buildStraat(straat)
                .buildHuisnr(huisnr)
                .buildPostbus(postbus)
                .buildPostcode(postcode)
                .buildStad(stad)
                .buildAdres();

        assertNotNull(adres);
        assertEquals(straat, adres.getStraat());
        assertEquals(huisnr, adres.getHuisnr());
        assertEquals(postcode, adres.getPostcode());
        assertEquals(stad, adres.getStad());
    }

    @ParameterizedTest
    @MethodSource("ongeldigeAdressen")
    void maakAdres_ongeldig(String straat, String huisnr, String postbus,
                            int postcode, String stad,
                            Set<AdresAtrributes> expected) {

        AdresException ex = assertThrows(AdresException.class, () ->
                Adres.adresBuilder()
                        .buildStraat(straat)
                        .buildHuisnr(huisnr)
                        .buildPostbus(postbus)
                        .buildPostcode(postcode)
                        .buildStad(stad)
                        .buildAdres()
        );

        assertEquals(expected, ex.getRequired());
    }
}