package domein.site;

import com.example._026javag03.domein.Site;
import com.example._026javag03.exceptions.SiteException;
import com.example._026javag03.util.site.ProductieStatus;
import com.example._026javag03.util.site.SiteAttributes;
import com.example._026javag03.util.Status;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Site Domeinklasse Tests")
class SiteTest {

    private static Site.SiteBuilder createValidBuilder() {
        return Site.siteBuilder()
                .buildNaam("Hoofdkantoor")
                .buildLocatie("Brussel")
                .buildCapaciteit(500)
                .buildOperationeleStatus(Status.ACTIEF)
                .buildProductieStatus(ProductieStatus.GEZOND);
    }

    @Nested
    @DisplayName("Successful Creation Tests")
    class SuccessfulCreationTests {

        static Collection<Status> operationeleStatusProvider() {
            return Arrays.asList(Status.values());
        }

        static Collection<ProductieStatus> productieStatusProvider() {
            return Arrays.asList(ProductieStatus.values());
        }

        @Test
        @DisplayName("Maak geldige site met alle verplichte velden")
        void createValidSite() throws SiteException {
            Site.SiteBuilder builder = createValidBuilder();

            Site site = builder.buildSite();

            assertNotNull(site, "Site mag niet null zijn");
            assertEquals("Hoofdkantoor", site.getNaam());
            assertEquals("Brussel", site.getLocatie());
            assertEquals(500, site.getCapaciteit());
            assertEquals(Status.ACTIEF, site.getOperationeleStatus());
            assertEquals(ProductieStatus.GEZOND, site.getProductieStatus());
        }

        @DisplayName("Maak site met verschillende operationele statussen")
        @ParameterizedTest(name = "Status: {0}")
        @MethodSource("operationeleStatusProvider")
        void createSiteWithOperationeleStatus(Status status) throws SiteException {
            Site site = Site.siteBuilder()
                    .buildNaam("Test Site")
                    .buildLocatie("Antwerpen")
                    .buildCapaciteit(100)
                    .buildOperationeleStatus(status)
                    .buildProductieStatus(ProductieStatus.GEZOND)
                    .buildSite();

            assertNotNull(site);
            assertEquals(status, site.getOperationeleStatus());
        }

        @DisplayName("Maak site met verschillende productie statussen")
        @ParameterizedTest(name = "Status: {0}")
        @MethodSource("productieStatusProvider")
        void createSiteWithProductieStatus(ProductieStatus status) throws SiteException {
            Site site = Site.siteBuilder()
                    .buildNaam("Test Site")
                    .buildLocatie("Gent")
                    .buildCapaciteit(200)
                    .buildOperationeleStatus(Status.ACTIEF)
                    .buildProductieStatus(status)
                    .buildSite();

            assertNotNull(site);
            assertEquals(status, site.getProductieStatus());
        }

    }

    @Nested
    @DisplayName("Validation Tests")
    class ValidationTests {

        @Nested
        @DisplayName("Ontbrekende Velden Validatie")
        class MissingFieldTests {

            private static Collection<Object[]> missingFieldTestData() {
                return Arrays.asList(new Object[][]{
                        {"testMissingNaam", "naam", SiteAttributes.NAAM},
                        {"testMissingLocatie", "locatie", SiteAttributes.LOCATIE},
                        {"testMissingCapaciteit", "capaciteit", SiteAttributes.CAPACITEIT},
                        {"testMissingOperationeleStatus", "operationeleStatus", SiteAttributes.OPERATIONELE_STATUS},
                        {"testMissingProductieStatus", "productieStatus", SiteAttributes.PRODUCTIE_STATUS}
                });
            }

            @ParameterizedTest(name = "{0}")
            @MethodSource("missingFieldTestData")
            @DisplayName("Valideer dat ontbrekend veld een exception gooit")
            void testMissingField(String testName, String fieldName, SiteAttributes expectedAttribute) {
                Site.SiteBuilder builder = switch (fieldName) {
                    case "naam" -> Site.siteBuilder()
                            .buildLocatie("TestLoc")
                            .buildCapaciteit(100)
                            .buildOperationeleStatus(Status.ACTIEF)
                            .buildProductieStatus(ProductieStatus.GEZOND);
                    case "locatie" -> Site.siteBuilder()
                            .buildNaam("Test")
                            .buildCapaciteit(100)
                            .buildOperationeleStatus(Status.ACTIEF)
                            .buildProductieStatus(ProductieStatus.GEZOND);
                    case "capaciteit" -> Site.siteBuilder()
                            .buildNaam("Test")
                            .buildLocatie("TestLoc")
                            .buildOperationeleStatus(Status.ACTIEF)
                            .buildProductieStatus(ProductieStatus.GEZOND);
                    case "operationeleStatus" -> Site.siteBuilder()
                            .buildNaam("Test")
                            .buildLocatie("TestLoc")
                            .buildCapaciteit(100)
                            .buildProductieStatus(ProductieStatus.GEZOND);
                    case "productieStatus" -> Site.siteBuilder()
                            .buildNaam("Test")
                            .buildLocatie("TestLoc")
                            .buildCapaciteit(100)
                            .buildOperationeleStatus(Status.ACTIEF);
                    default -> throw new IllegalArgumentException("Onbekend veld: " + fieldName);
                };

                SiteException exception = assertThrows(
                        SiteException.class,
                        builder::buildSite,
                        "SiteException moet gegooid worden voor ontbrekend veld: " + fieldName
                );

                Map<SiteAttributes, String> errors = exception.getRequired(); // ← getRequired() ipv getMissingAttributes()

                assertTrue(
                        errors.containsKey(expectedAttribute),
                        "Exception moet " + expectedAttribute + " bevatten voor veld: " + fieldName
                );

                assertFalse(
                        errors.get(expectedAttribute).isBlank(),
                        "Error message voor " + expectedAttribute + " mag niet leeg zijn"
                );
            }

            @Nested
            @DisplayName("Ongeldige Veld Tests")
            class InvalidFieldTests {

                private static Collection<Object[]> invalidCapaciteitTestData() {
                    return Arrays.asList(new Object[][]{
                            {"testCapaciteitZero", 0},
                            {"testCapaciteitNegative", -1},
                            {"testCapaciteitNegativeLarge", -1000}
                    });
                }

                @ParameterizedTest(name = "Capaciteit: {0}")
                @MethodSource("invalidCapaciteitTestData")
                @DisplayName("Valideer dat ongeldige capaciteit een exception gooit")
                void testInvalidCapaciteit(String testName, int capaciteit) {
                    Site.SiteBuilder builder = Site.siteBuilder()
                            .buildNaam("Test Site")
                            .buildLocatie("Test Loc")
                            .buildCapaciteit(capaciteit)
                            .buildOperationeleStatus(Status.ACTIEF)
                            .buildProductieStatus(ProductieStatus.GEZOND);

                    SiteException exception = assertThrows(
                            SiteException.class,
                            builder::buildSite
                    );

                    Map<SiteAttributes, String> errors = exception.getRequired();

                    assertTrue(
                            errors.containsKey(SiteAttributes.CAPACITEIT),
                            "Exception moet CAPACITEIT bevatten voor waarde: " + capaciteit
                    );

                    assertFalse(
                            errors.get(SiteAttributes.CAPACITEIT).isBlank(),
                            "Error message voor capaciteit mag niet leeg zijn"
                    );
                }

                @Test
                @DisplayName("Test met lege naam (alleen spaties)")
                void testEmptyNaam() {
                    Site.SiteBuilder builder = Site.siteBuilder()
                            .buildNaam("   ")
                            .buildLocatie("Brussel")
                            .buildCapaciteit(100)
                            .buildOperationeleStatus(Status.ACTIEF)
                            .buildProductieStatus(ProductieStatus.GEZOND);

                    SiteException exception = assertThrows(SiteException.class, builder::buildSite);
                    Map<SiteAttributes, String> errors = exception.getRequired();

                    assertTrue(
                            errors.containsKey(SiteAttributes.NAAM),
                            "Exception moet NAAM bevatten voor lege string"
                    );
                }

                @Test
                @DisplayName("Test met lege locatie (null)")
                void testNullLocatie() {
                    Site.SiteBuilder builder = Site.siteBuilder()
                            .buildNaam("Test Site")
                            .buildLocatie(null)
                            .buildCapaciteit(100)
                            .buildOperationeleStatus(Status.ACTIEF)
                            .buildProductieStatus(ProductieStatus.GEZOND);

                    SiteException exception = assertThrows(SiteException.class, builder::buildSite);
                    Map<SiteAttributes, String> errors = exception.getRequired();

                    assertTrue(
                            errors.containsKey(SiteAttributes.LOCATIE),
                            "Exception moet LOCATIE bevatten voor null waarde"
                    );
                }
            }

            @Nested
            @DisplayName("Edge Case Tests")
            class EdgeCaseTests {

                static Collection<ProductieStatus> productieStatusProvider() {
                    return Arrays.asList(ProductieStatus.values());
                }

                @Test
                @DisplayName("Maak site met minimale geldige capaciteit (1)")
                void createSiteWithMinimumCapacity() throws SiteException {
                    Site site = Site.siteBuilder()
                            .buildNaam("Klein Magazijn")
                            .buildLocatie("Luik")
                            .buildCapaciteit(1)
                            .buildOperationeleStatus(Status.INACTIEF)
                            .buildProductieStatus(ProductieStatus.OFFLINE)
                            .buildSite();

                    assertNotNull(site);
                    assertEquals(1, site.getCapaciteit());
                }

                @Test
                @DisplayName("Maak site met grote capaciteit (Integer.MAX_VALUE)")
                void createSiteWithMaximumCapacity() throws SiteException {
                    int maxCapaciteit = Integer.MAX_VALUE;
                    Site site = Site.siteBuilder()
                            .buildNaam("Groot Distributiecentrum")
                            .buildLocatie("Charleroi")
                            .buildCapaciteit(maxCapaciteit)
                            .buildOperationeleStatus(Status.ACTIEF)
                            .buildProductieStatus(ProductieStatus.GEZOND)
                            .buildSite();

                    assertNotNull(site);
                    assertEquals(maxCapaciteit, site.getCapaciteit());
                }

                @DisplayName("Maak site met alle productie statussen")
                @ParameterizedTest(name = "Productie status: {0}")
                @MethodSource("productieStatusProvider")
                void createSiteWithAllProductieStatuses(ProductieStatus status) throws SiteException {
                    Site site = Site.siteBuilder()
                            .buildNaam("Test")
                            .buildLocatie("Test")
                            .buildCapaciteit(100)
                            .buildOperationeleStatus(Status.ACTIEF)
                            .buildProductieStatus(status)
                            .buildSite();

                    assertEquals(status, site.getProductieStatus());
                }
            }

            @Nested
            @DisplayName("ToString Tests")
            class ToStringTests {

                @Test
                @DisplayName("Test toString geeft correcte output")
                void testToString() throws SiteException {
                    Site site = Site.siteBuilder()
                            .buildNaam("Hoofdkantoor")
                            .buildLocatie("Brussel")
                            .buildCapaciteit(500)
                            .buildOperationeleStatus(Status.ACTIEF)
                            .buildProductieStatus(ProductieStatus.GEZOND)
                            .buildSite();

                    String result = site.toString();

                    assertNotNull(result);
                    assertTrue(result.contains("Hoofdkantoor"));
                    assertTrue(result.contains("Brussel"));
                    assertTrue(result.contains("500"));
                    assertTrue(result.contains("ACTIEF"));
                    assertTrue(result.contains("GEZOND"));
                }

                @Test
                @DisplayName("Test toString bevat alle velden")
                void testToStringContainsAllFields() throws SiteException {
                    String naam = "TestSite";
                    String locatie = "Brugge";
                    int capaciteit = 250;

                    Site site = Site.siteBuilder()
                            .buildNaam(naam)
                            .buildLocatie(locatie)
                            .buildCapaciteit(capaciteit)
                            .buildOperationeleStatus(Status.INACTIEF)
                            .buildProductieStatus(ProductieStatus.PROBLEMEN)
                            .buildSite();

                    String result = site.toString();

                    assertTrue(result.contains("naam='" + naam + "'"));
                    assertTrue(result.contains("locatie='" + locatie + "'"));
                    assertTrue(result.contains("capaciteit=" + capaciteit));
                    assertTrue(result.contains("operationeleStatus=INACTIEF"));
                    assertTrue(result.contains("productieStatus=PROBLEMEN"));
                }
            }

            @Nested
            @DisplayName("Multiple Validation Errors Tests")
            class MultipleValidationErrorsTests {

                @Test
                @DisplayName("Test met meerdere ontbrekende velden")
                void testMultipleMissingFields() {
                    Site.SiteBuilder builder = Site.siteBuilder()
                            .buildNaam("Test Site");

                    SiteException exception = assertThrows(SiteException.class, builder::buildSite);

                    Map<SiteAttributes, String> errors = exception.getRequired();

                    assertTrue(errors.containsKey(SiteAttributes.LOCATIE));
                    assertTrue(errors.containsKey(SiteAttributes.CAPACITEIT));
                    assertTrue(errors.containsKey(SiteAttributes.OPERATIONELE_STATUS));
                    assertTrue(errors.containsKey(SiteAttributes.PRODUCTIE_STATUS));

                    assertEquals(4, errors.size(), "Moeten precies 4 errors zijn");

                    assertFalse(errors.containsKey(SiteAttributes.NAAM), "Naam zou geldig moeten zijn");
                }

                @Test
                @DisplayName("Test met alle velden ontbrekend")
                void testAllFieldsMissing() {
                    Site.SiteBuilder builder = Site.siteBuilder();

                    SiteException exception = assertThrows(SiteException.class, builder::buildSite);

                    Map<SiteAttributes, String> errors = exception.getRequired();

                    assertEquals(5, errors.size(), "Moeten precies 5 errors zijn");
                    assertTrue(errors.containsKey(SiteAttributes.NAAM));
                    assertTrue(errors.containsKey(SiteAttributes.LOCATIE));
                    assertTrue(errors.containsKey(SiteAttributes.CAPACITEIT));
                    assertTrue(errors.containsKey(SiteAttributes.OPERATIONELE_STATUS));
                    assertTrue(errors.containsKey(SiteAttributes.PRODUCTIE_STATUS));

                    for (Map.Entry<SiteAttributes, String> entry : errors.entrySet()) {
                        assertFalse(entry.getValue().isBlank(),
                                "Error message voor " + entry.getKey() + " mag niet leeg zijn");
                    }
                }
            }
        }
    }
}