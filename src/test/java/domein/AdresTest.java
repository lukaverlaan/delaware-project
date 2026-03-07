package domein;

import com.example._026javag03.domein.Adres;
import com.example._026javag03.exceptions.AdresException;
import com.example._026javag03.util.gebruiker.AdresAttributes;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static com.example._026javag03.util.gebruiker.AdresAttributes.*;
import static org.junit.jupiter.api.Assertions.*;

class AdresTest {

    private static final String GELDIGE_STRAAT = "Laarnesteenweg";
    private static final String GELDIG_HUISNR = "52";
    private static final String GELDIGE_POSTBUS = "52A";
    private static final int GELDIGE_POSTCODE = 9000;
    private static final String GELDIGE_STAD = "Gent";

    private static Stream<Arguments> geldigeAdressen() {
        return Stream.of(
                Arguments.of(GELDIGE_STRAAT, GELDIG_HUISNR,GELDIGE_POSTBUS, GELDIGE_POSTCODE, GELDIGE_STAD),
                Arguments.of(GELDIGE_STRAAT, "1",GELDIGE_POSTBUS, 9000, GELDIGE_STAD),
                Arguments.of(GELDIGE_STRAAT, "52",GELDIGE_POSTBUS, 9230, GELDIGE_STAD)
        );
    }

    private static Stream<Arguments> ongeldigeAdressen() {
        return Stream.of(
                Arguments.of("  ", GELDIG_HUISNR,GELDIGE_POSTBUS, GELDIGE_POSTCODE, GELDIGE_STAD, STRAAT),
                Arguments.of(null, GELDIG_HUISNR,GELDIGE_POSTBUS, GELDIGE_POSTCODE, GELDIGE_STAD, STRAAT),
                Arguments.of("", GELDIG_HUISNR,GELDIGE_POSTBUS, GELDIGE_POSTCODE, GELDIGE_STAD, STRAAT),
                Arguments.of(GELDIGE_STRAAT, "0",GELDIGE_POSTBUS, GELDIGE_POSTCODE, GELDIGE_STAD, HUISNUMMER),
                Arguments.of(GELDIGE_STRAAT, "-23",GELDIGE_POSTBUS, GELDIGE_POSTCODE, GELDIGE_STAD, HUISNUMMER),
                Arguments.of(GELDIGE_STRAAT, "-1",GELDIGE_POSTBUS, GELDIGE_POSTCODE, GELDIGE_STAD, HUISNUMMER),
                Arguments.of(GELDIGE_STRAAT, GELDIG_HUISNR,GELDIGE_POSTBUS, 0, GELDIGE_STAD, POSTCODE),
                Arguments.of(GELDIGE_STRAAT, GELDIG_HUISNR,GELDIGE_POSTBUS, 123, GELDIGE_STAD, POSTCODE),
                Arguments.of(GELDIGE_STRAAT, GELDIG_HUISNR,GELDIGE_POSTBUS, 12345, GELDIGE_STAD, POSTCODE),
                Arguments.of(GELDIGE_STRAAT, GELDIG_HUISNR,GELDIGE_POSTBUS, -1234, GELDIGE_STAD, POSTCODE),
                Arguments.of(GELDIGE_STRAAT, GELDIG_HUISNR,GELDIGE_POSTBUS, GELDIGE_POSTCODE, "", STAD),
                Arguments.of(GELDIGE_STRAAT, GELDIG_HUISNR,GELDIGE_POSTBUS, GELDIGE_POSTCODE, "    ", STAD),
                Arguments.of(GELDIGE_STRAAT, GELDIG_HUISNR,GELDIGE_POSTBUS, GELDIGE_POSTCODE, null, STAD)
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
                            AdresAttributes expected) {

        AdresException ex = assertThrows(AdresException.class, () ->
                Adres.adresBuilder()
                        .buildStraat(straat)
                        .buildHuisnr(huisnr)
                        .buildPostbus(postbus)
                        .buildPostcode(postcode)
                        .buildStad(stad)
                        .buildAdres()
        );

        assertTrue(ex.getRequired().containsKey(expected));
    }

}