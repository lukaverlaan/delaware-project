package domein;

import com.example._026javag03.domein.Gebruiker;
import com.example._026javag03.exceptions.AdresException;
import com.example._026javag03.util.gebruiker.Rol;
import com.example._026javag03.util.Status;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.Map;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class GebruikerTest {

    private static Stream<org.junit.jupiter.params.provider.Arguments> ongeldigeGebruikers() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(
                        null, "Jan", "jan@test.com", Rol.ADMINISTRATOR,
                        Map.of("naam", "Naam is verplicht.")
                ),
                org.junit.jupiter.params.provider.Arguments.of(
                        "Janssen", "Jan", "ongeldigEmail", Rol.ADMINISTRATOR,
                        Map.of("email", "Ongeldig e-mailadres.")
                ),
                org.junit.jupiter.params.provider.Arguments.of(
                        "Janssen", "Jan", "jan@test.com", Rol.WERKNEMER,
                        Map.of("gsm", "Gsm is verplicht voor werknemers.")
                )
        );
    }

    @ParameterizedTest
    @MethodSource("ongeldigeGebruikers")
    void builder_gooitIllegalArgumentException(String naam,
                                               String voornaam,
                                               String email,
                                               Rol rol,
                                               Map<String,String> expectedFouten)
            throws AdresException {

        Gebruiker.Builder builder = new Gebruiker.Builder(
                naam,
                voornaam,
                LocalDate.of(1990,1,1),
                "Kerkstraat",
                "10",
                null,
                1000,
                "Brussel",
                email,
                rol,
                Status.ACTIEF
        );

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                builder::build
        );

        assertTrue(ex.getMessage().contains(expectedFouten.keySet().iterator().next()));
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> geldigeGebruikers() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(
                        "Janssen","Jan","jan@test.com",Rol.ADMINISTRATOR,null
                ),
                org.junit.jupiter.params.provider.Arguments.of(
                        "Peeters","Piet","piet@test.com",Rol.WERKNEMER,"0470123456"
                )
        );
    }

    @ParameterizedTest
    @MethodSource("geldigeGebruikers")
    void builder_maaktCorrecteGebruiker(String naam,
                                        String voornaam,
                                        String email,
                                        Rol rol,
                                        String gsm)
            throws AdresException {

        Gebruiker.Builder builder = new Gebruiker.Builder(
                naam,
                voornaam,
                LocalDate.of(1990,1,1),
                "Kerkstraat",
                "10",
                null,
                1000,
                "Brussel",
                email,
                rol,
                Status.ACTIEF
        );

        if (gsm != null) {
            builder.gsm(gsm);
        }

        Gebruiker gebruiker = builder.build();

        assertEquals(naam, gebruiker.getNaam());
        assertEquals(voornaam, gebruiker.getVoornaam());
        assertEquals(email, gebruiker.getEmail());
        assertEquals(rol, gebruiker.getRol());
    }
}