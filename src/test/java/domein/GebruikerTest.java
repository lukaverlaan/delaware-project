package domein;

import com.example._026javag03.domein.Gebruiker;
import com.example._026javag03.exceptions.AdresException;
import com.example._026javag03.util.Rol;
import com.example._026javag03.util.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Test cases voor Gebruiker (Builder Pattern met AdresBuilder)")
class GebruikerTest {

    private static final String GELDIGE_NAAM = "Janssen";
    private static final String GELDIGE_VOORNAAM = "Jan";
    private static final String GELDIG_EMAIL = "jan.janssen@example.com";
    private static final String GELDIG_GSM = "0470123456";
    private static final LocalDate GELDIGE_GEBOORTEDATUM = LocalDate.of(1990, 5, 15);
    private static final LocalDate MINDERJARIGE_GEBOORTEDATUM = LocalDate.now().minusYears(16);
    private static final String GELDIGE_STRAAT = "Kerkstraat";
    private static final int GELDIGE_HUISNR = 10;
    private static final int GELDIGE_POSTCODE = 1000;
    private static final String GELDIGE_STAD = "Brussel";

//    private AdresBuilder buildGeldigAdres() {
//        AdresBuilder adresBuilder = new AdresBuilder();
//        adresBuilder.createAdres();
//        adresBuilder.buildStraat("Kerkstraat");
//        adresBuilder.buildHuisnr(10);
//        adresBuilder.buildPostcode(1000);
//        adresBuilder.buildStad("Brussel");
//        return adresBuilder;
//    }

    @Nested
    @DisplayName("Geldige Gebruiker creatie tests")
    class GeldigeCreatie {

        @Test
        @DisplayName("Builder maakt geldige gebruiker met alle velden")
        void testBuilderMetAlleVelden() throws AdresException {


            Gebruiker gebruiker = new Gebruiker.Builder(
                    GELDIGE_NAAM,
                    GELDIGE_VOORNAAM,
                    GELDIGE_GEBOORTEDATUM,

                    GELDIGE_STRAAT,
                    GELDIGE_HUISNR,
                    GELDIGE_POSTCODE,
                    GELDIGE_STAD,
                    GELDIG_EMAIL,
                    Rol.WERKNEMER,
                    Status.ACTIEF
            )
                    .gsm(GELDIG_GSM)
                    .build();

            assertEquals(GELDIGE_NAAM, gebruiker.getNaam());
            assertEquals(GELDIGE_VOORNAAM, gebruiker.getVoornaam());
            assertNotNull(gebruiker.getAdres());
            assertEquals("Kerkstraat", gebruiker.getAdres().getStraat());
            assertEquals(10, gebruiker.getAdres().getHuisnr());
            assertEquals(1000, gebruiker.getAdres().getPostcode());
            assertEquals("Brussel", gebruiker.getAdres().getStad());
            assertEquals(GELDIG_EMAIL, gebruiker.getEmail());
            assertEquals(GELDIG_GSM, gebruiker.getGsm());
            assertEquals(GELDIGE_GEBOORTEDATUM, gebruiker.getGeboortedatum());
            assertEquals(Rol.WERKNEMER, gebruiker.getRol());
            assertEquals(Status.ACTIEF, gebruiker.getStatus());
        }

        @Test
        @DisplayName("Builder maakt geldige gebruiker zonder optioneel gsm (ADMINISTRATOR rol)")
        void testBuilderZonderGsmVoorAdministrator() throws AdresException {


            Gebruiker gebruiker = new Gebruiker.Builder(
                    GELDIGE_NAAM,
                    GELDIGE_VOORNAAM,
                    GELDIGE_GEBOORTEDATUM,

                    GELDIGE_STRAAT,
                    GELDIGE_HUISNR,
                    GELDIGE_POSTCODE,
                    GELDIGE_STAD,
                    GELDIG_EMAIL,
                    Rol.ADMINISTRATOR,
                    Status.ACTIEF
            )
                    .build();

            assertNotNull(gebruiker);
            assertNull(gebruiker.getGsm());
            assertEquals(Rol.ADMINISTRATOR, gebruiker.getRol());
        }

        @Test
        @DisplayName("Builder methode chaining werkt correct")
        void testMethodChaining() throws AdresException {


            Gebruiker gebruiker = new Gebruiker.Builder(
                    "Peeters", "Annie",
                    LocalDate.of(1985, 3, 20),
                    "Stationstraat",
                    25,
                    2000,
                    "Antwerpen",
                    "annie@test.com",
                    Rol.ADMINISTRATOR, Status.INACTIEF
            )
                    .gsm("0499876543")
                    .build();

            assertNotNull(gebruiker);
            assertEquals("0499876543", gebruiker.getGsm());
            assertEquals("Stationstraat", gebruiker.getAdres().getStraat());
        }
    }

    @Nested
    @DisplayName("Validatie tests - Verplichte velden")
    class ValidatieVerplichteVelden {

        @Test
        @DisplayName("IllegalArgumentException bij ontbrekende naam")
        void testOntbrekendeNaam() throws AdresException {


            Gebruiker.Builder builder = new Gebruiker.Builder(
                    null,
                    GELDIGE_VOORNAAM,
                    GELDIGE_GEBOORTEDATUM,
                    GELDIGE_STRAAT,
                    GELDIGE_HUISNR,
                    GELDIGE_POSTCODE,
                    GELDIGE_STAD,
                    GELDIG_EMAIL,
                    Rol.WERKNEMER,
                    Status.ACTIEF
            ).gsm(GELDIG_GSM);

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    builder::build
            );
            assertEquals("Niet alle verplichte velden zijn ingevuld.", exception.getMessage());
        }

        @Test
        @DisplayName("IllegalArgumentException bij lege naam")
        void testLegeNaam() throws AdresException {


            Gebruiker.Builder builder = new Gebruiker.Builder(
                    "   ",
                    GELDIGE_VOORNAAM,
                    GELDIGE_GEBOORTEDATUM,
                    GELDIGE_STRAAT,
                    GELDIGE_HUISNR,
                    GELDIGE_POSTCODE,
                    GELDIGE_STAD,
                    GELDIG_EMAIL,
                    Rol.WERKNEMER,
                    Status.ACTIEF
            ).gsm(GELDIG_GSM);

            assertThrows(IllegalArgumentException.class, builder::build);
        }

        @Test
        @DisplayName("IllegalArgumentException bij ontbrekende voornaam")
        void testOntbrekendeVoornaam() throws AdresException {


            Gebruiker.Builder builder = new Gebruiker.Builder(
                    GELDIGE_NAAM,
                    null,
                    GELDIGE_GEBOORTEDATUM,
                    GELDIGE_STRAAT,
                    GELDIGE_HUISNR,
                    GELDIGE_POSTCODE,
                    GELDIGE_STAD,
                    GELDIG_EMAIL,
                    Rol.WERKNEMER,
                    Status.ACTIEF
            ).gsm(GELDIG_GSM);

            assertThrows(IllegalArgumentException.class, builder::build);
        }

        @Test
        @DisplayName("IllegalArgumentException bij ontbrekende voornaam (lege string)")
        void testLegeVoornaam() throws AdresException {

            Gebruiker.Builder builder = new Gebruiker.Builder(
                    GELDIGE_NAAM,
                    "   ",
                    GELDIGE_GEBOORTEDATUM,
                    GELDIGE_STRAAT,
                    GELDIGE_HUISNR,
                    GELDIGE_POSTCODE,
                    GELDIGE_STAD,
                    GELDIG_EMAIL,
                    Rol.WERKNEMER,
                    Status.ACTIEF
            ).gsm(GELDIG_GSM);

            assertThrows(IllegalArgumentException.class, builder::build);
        }

        @Test
        @DisplayName("IllegalArgumentException bij ontbrekend email")
        void testOntbrekendEmail() throws AdresException {


            Gebruiker.Builder builder = new Gebruiker.Builder(
                    GELDIGE_NAAM,
                    GELDIGE_VOORNAAM,
                    GELDIGE_GEBOORTEDATUM,
                    GELDIGE_STRAAT,
                    GELDIGE_HUISNR,
                    GELDIGE_POSTCODE,
                    GELDIGE_STAD,
                    null,
                    Rol.WERKNEMER,
                    Status.ACTIEF
            ).gsm(GELDIG_GSM);

            assertThrows(IllegalArgumentException.class, builder::build);
        }

        @Test
        @DisplayName("IllegalArgumentException bij lege email")
        void testLegeEmail() throws AdresException {

            Gebruiker.Builder builder = new Gebruiker.Builder(
                    GELDIGE_NAAM,
                    GELDIGE_VOORNAAM,
                    GELDIGE_GEBOORTEDATUM,
                    GELDIGE_STRAAT,
                    GELDIGE_HUISNR,
                    GELDIGE_POSTCODE,
                    GELDIGE_STAD,
                    "   ",
                    Rol.WERKNEMER,
                    Status.ACTIEF
            ).gsm(GELDIG_GSM);

            assertThrows(IllegalArgumentException.class, builder::build);
        }

        @Test
        @DisplayName("IllegalArgumentException bij ontbrekende geboortedatum")
        void testOntbrekendeGeboortedatum() throws AdresException {

            Gebruiker.Builder builder = new Gebruiker.Builder(
                    GELDIGE_NAAM,
                    GELDIGE_VOORNAAM,
                    null,
                    GELDIGE_STRAAT,
                    GELDIGE_HUISNR,
                    GELDIGE_POSTCODE,
                    GELDIGE_STAD,
                    GELDIG_EMAIL,
                    Rol.WERKNEMER,
                    Status.ACTIEF
            ).gsm(GELDIG_GSM);

            assertThrows(IllegalArgumentException.class, builder::build);
        }
    }

    @Nested
    @DisplayName("Validatie tests - Leeftijd")
    class ValidatieLeeftijd {

        @Test
        @DisplayName("IllegalArgumentException bij minderjarige gebruiker")
        void testMinderjarigeGebruiker() throws AdresException {

            Gebruiker.Builder builder = new Gebruiker.Builder(
                    GELDIGE_NAAM,
                    GELDIGE_VOORNAAM,
                    MINDERJARIGE_GEBOORTEDATUM,
                    GELDIGE_STRAAT,
                    GELDIGE_HUISNR,
                    GELDIGE_POSTCODE,
                    GELDIGE_STAD,
                    GELDIG_EMAIL,
                    Rol.WERKNEMER,
                    Status.ACTIEF
            );

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    builder::build
            );
            assertEquals("Gebruiker moet minstens 18 jaar oud zijn.", exception.getMessage());
        }

        @Test
        @DisplayName("Exact 18 jaar is toegestaan")
        void testExact18Jaar() throws AdresException {
            LocalDate exact18Jaar = LocalDate.now().minusYears(18);

            Gebruiker.Builder builder = new Gebruiker.Builder(
                    GELDIGE_NAAM,
                    GELDIGE_VOORNAAM,
                    exact18Jaar,
                    GELDIGE_STRAAT,
                    GELDIGE_HUISNR,
                    GELDIGE_POSTCODE,
                    GELDIGE_STAD,
                    GELDIG_EMAIL,
                    Rol.ADMINISTRATOR,
                    Status.ACTIEF
            );

            Gebruiker gebruiker = builder.build();

            assertNotNull(gebruiker);
        }
    }

    @Nested
    @DisplayName("Validatie tests - GSM verplichting")
    class ValidatieGsm {

        @Test
        @DisplayName("IllegalArgumentException bij ontbrekend gsm voor WERKNEMER")
        void testGsmVerplichtVoorWerknemer() throws AdresException {

            Gebruiker.Builder builder = new Gebruiker.Builder(
                    GELDIGE_NAAM,
                    GELDIGE_VOORNAAM,
                    GELDIGE_GEBOORTEDATUM,
                    GELDIGE_STRAAT,
                    GELDIGE_HUISNR,
                    GELDIGE_POSTCODE,
                    GELDIGE_STAD,
                    GELDIG_EMAIL,
                    Rol.WERKNEMER,
                    Status.ACTIEF
            );

            IllegalArgumentException exception = assertThrows(
                    IllegalArgumentException.class,
                    builder::build
            );
            assertEquals("Gsm is verplicht voor werknemers.", exception.getMessage());
        }

        @Test
        @DisplayName("Gsm is optioneel voor ADMINISTRATOR rol")
        void testGsmOptioneelVoorClient() throws AdresException {

            Gebruiker.Builder builder = new Gebruiker.Builder(
                    GELDIGE_NAAM,
                    GELDIGE_VOORNAAM,
                    GELDIGE_GEBOORTEDATUM,
                    GELDIGE_STRAAT,
                    GELDIGE_HUISNR,
                    GELDIGE_POSTCODE,
                    GELDIGE_STAD,
                    GELDIG_EMAIL,
                    Rol.ADMINISTRATOR,
                    Status.ACTIEF
            );

            Gebruiker gebruiker = builder.build();

            assertNotNull(gebruiker);
            assertNull(gebruiker.getGsm());
        }

        @Test
        @DisplayName("Lege gsm string is ongeldig voor WERKNEMER")
        void testLegeGsmString() throws AdresException {

            Gebruiker.Builder builder = new Gebruiker.Builder(
                    GELDIGE_NAAM,
                    GELDIGE_VOORNAAM,
                    GELDIGE_GEBOORTEDATUM,
                    GELDIGE_STRAAT,
                    GELDIGE_HUISNR,
                    GELDIGE_POSTCODE,
                    GELDIGE_STAD,
                    GELDIG_EMAIL,
                    Rol.WERKNEMER,
                    Status.ACTIEF
            ).gsm("  ");

            assertThrows(IllegalArgumentException.class, builder::build);
        }
    }

    @Nested
    @DisplayName("Equals en HashCode tests")
    class EqualsEnHashCode {

        @Test
        @DisplayName("Twee gebruikers met zelfde email zijn equal")
        void testEqualsZelfdeEmail() throws AdresException {

            Gebruiker g1 = new Gebruiker.Builder(
                    "Peeters", "Annie",
                    LocalDate.of(1985, 3, 20),
                    "stationstaraat",
                    25,
                    2000,
                    "Antwerpen",
                    "annie@test.com",
                    Rol.ADMINISTRATOR, Status.INACTIEF
            ).build();

            Gebruiker g2 = new Gebruiker.Builder(
                    "Peeters", "Annie",
                    LocalDate.of(1985, 3, 20),
                    "stationstaraat",
                    25,
                    2000,
                    "Antwerpen",
                    "annie@test.com",
                    Rol.ADMINISTRATOR, Status.INACTIEF
            ).build();

            assertEquals(g1, g2);
            assertEquals(g1.hashCode(), g2.hashCode());
        }

        @Test
        @DisplayName("Twee gebruikers met verschillende email zijn niet equal")
        void testEqualsVerschillendeEmail() throws AdresException {

            Gebruiker g1 = new Gebruiker.Builder(
                    "Peeters", "Annie",
                    LocalDate.of(1985, 3, 20),
                    "stationstaraat",
                    25,
                    2000,
                    "Antwerpen",
                    "annie@test.com",
                    Rol.ADMINISTRATOR, Status.INACTIEF
            ).build();

            Gebruiker g2 = new Gebruiker.Builder(
                    "Peeters", "Annie",
                    LocalDate.of(1985, 3, 20),
                    "stationstaraat",
                    25,
                    2000,
                    "Antwerpen",
                    "annie2@test.com",
                    Rol.ADMINISTRATOR, Status.INACTIEF
            ).build();

            assertNotEquals(g1, g2);
        }

        @Test
        @DisplayName("Gebruiker is equal aan zichzelf")
        void testEqualsZelf() throws AdresException {

            Gebruiker gebruiker = new Gebruiker.Builder(
                    "Peeters", "Annie",
                    LocalDate.of(1985, 3, 20),
                    "stationstaraat",
                    25,
                    2000,
                    "Antwerpen",
                    "annie@test.com",
                    Rol.ADMINISTRATOR, Status.INACTIEF
            ).build();

            assertEquals(gebruiker, gebruiker);
        }

        @Test
        @DisplayName("Gebruiker is niet equal aan null")
        void testEqualsMetNull() throws AdresException {

            Gebruiker gebruiker = new Gebruiker.Builder(
                    "Peeters", "Annie",
                    LocalDate.of(1985, 3, 20),
                    "stationstaraat",
                    25,
                    2000,
                    "Antwerpen",
                    "annie@test.com",
                    Rol.ADMINISTRATOR, Status.INACTIEF
            ).build();

            assertNotEquals(null, gebruiker);
        }

        @Test
        @DisplayName("Gebruiker is niet equal aan ander type object")
        void testEqualsAnderType() throws AdresException {

            Gebruiker gebruiker = new Gebruiker.Builder(
                    "Peeters", "Annie",
                    LocalDate.of(1985, 3, 20),
                    "stationstaraat",
                    25,
                    2000,
                    "Antwerpen",
                    "annie@test.com",
                    Rol.ADMINISTRATOR, Status.INACTIEF
            ).build();

            assertNotEquals("Dit is een string", gebruiker);
            assertNotEquals(123, gebruiker);
        }
    }

    @Nested
    @DisplayName("Adres gerelateerde tests")
    class AdresTests {

        @Test
        @DisplayName("Adres velden worden correct opgeslagen in Gebruiker")
        void testAdresVelden() throws AdresException {

            Gebruiker gebruiker = new Gebruiker.Builder(
                    "Peeters", "Annie",
                    LocalDate.of(1985, 3, 20),
                    "Hoofdstraat",
                    42,
                    8000,
                    "Brugge",
                    "annie@test.com",
                    Rol.ADMINISTRATOR, Status.INACTIEF
            ).build();

            assertEquals("Hoofdstraat", gebruiker.getAdres().getStraat());
            assertEquals(42, gebruiker.getAdres().getHuisnr());
            assertEquals(8000, gebruiker.getAdres().getPostcode());
            assertEquals("Brugge", gebruiker.getAdres().getStad());
        }

    }
}