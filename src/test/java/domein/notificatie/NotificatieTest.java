package domein.notificatie;

import com.example._026javag03.domein.Gebruiker;
import com.example._026javag03.domein.Notificatie;
import com.example._026javag03.exceptions.NotificatieException;
import com.example._026javag03.util.notificatie.NotificatieAttributes;
import com.example._026javag03.util.notificatie.NotificatieStatus;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;

@DisplayName("Notificatie Domeinklasse Tests")
class NotificatieTest {

    private static LocalDateTime testDatum;
    private static Gebruiker mockGebruiker;

    @BeforeAll
    static void setUpAll() {
        testDatum = LocalDateTime.now();
        mockGebruiker = mock(Gebruiker.class);
    }

    private static Notificatie.NotificatieBuilder createValidBuilder() {
        return Notificatie.notificatieBuilder()
                .buildCode("DLW-NOTI-001")
                .buildDatum(testDatum)
                .buildType("INFO")
                .buildInhoud("Test notificatie inhoud")
                .buildStatus(NotificatieStatus.ONGELEZEN)
                .buildGebruiker(mockGebruiker);
    }

    @Nested
    @DisplayName("Successful Creation Tests")
    class SuccessfulCreationTests {

        static Collection<NotificatieStatus> statusProvider() {
            return Arrays.asList(NotificatieStatus.values());
        }

        @Test
        @DisplayName("Maak geldige notificatie met alle verplichte velden")
        void createValidNotificatie() throws NotificatieException {
            Notificatie notificatie = createValidBuilder().buildNotificatie();

            assertNotNull(notificatie);
            assertEquals("DLW-NOTI-001", notificatie.getCode());
            assertEquals(testDatum, notificatie.getDatum());
            assertEquals("INFO", notificatie.getType());
            assertEquals("Test notificatie inhoud", notificatie.getInhoud());
            assertEquals(NotificatieStatus.ONGELEZEN, notificatie.getStatus());
            assertEquals(mockGebruiker, notificatie.getGebruiker());
        }

        @ParameterizedTest(name = "Status: {0}")
        @MethodSource("statusProvider")
        @DisplayName("Maak notificatie met verschillende statussen")
        void createNotificatieWithStatus(NotificatieStatus status) throws NotificatieException {
            Notificatie notificatie = Notificatie.notificatieBuilder()
                    .buildCode("DLW-NOTI-001")
                    .buildDatum(testDatum)
                    .buildType("INFO")
                    .buildInhoud("Test inhoud")
                    .buildStatus(status)
                    .buildGebruiker(mockGebruiker)
                    .buildNotificatie();

            assertNotNull(notificatie);
            assertEquals(status, notificatie.getStatus());
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
                        {"testMissingDatum", NotificatieAttributes.DATUM},
                        {"testMissingType", NotificatieAttributes.TYPE},
                        {"testMissingInhoud", NotificatieAttributes.INHOUD},
                        {"testMissingGebruiker", NotificatieAttributes.GEBRUIKER}
                });
            }

            @ParameterizedTest(name = "{0}")
            @MethodSource("missingFieldTestData")
            void testMissingField(String testName, NotificatieAttributes expectedAttribute) {

                Notificatie.NotificatieBuilder builder;

                switch (expectedAttribute) {
                    case DATUM -> builder = Notificatie.notificatieBuilder()
                            .buildCode("DLW-NOTI-001")
                            .buildType("INFO")
                            .buildInhoud("Test inhoud")
                            .buildStatus(NotificatieStatus.ONGELEZEN)
                            .buildGebruiker(mockGebruiker);
                    case TYPE -> builder = Notificatie.notificatieBuilder()
                            .buildCode("DLW-NOTI-001")
                            .buildDatum(testDatum)
                            .buildInhoud("Test inhoud")
                            .buildStatus(NotificatieStatus.ONGELEZEN)
                            .buildGebruiker(mockGebruiker);
                    case INHOUD -> builder = Notificatie.notificatieBuilder()
                            .buildCode("DLW-NOTI-001")
                            .buildDatum(testDatum)
                            .buildType("INFO")
                            .buildStatus(NotificatieStatus.ONGELEZEN)
                            .buildGebruiker(mockGebruiker);
                    case GEBRUIKER -> builder = Notificatie.notificatieBuilder()
                            .buildCode("DLW-NOTI-001")
                            .buildDatum(testDatum)
                            .buildType("INFO")
                            .buildInhoud("Test inhoud")
                            .buildStatus(NotificatieStatus.ONGELEZEN);
                    default -> throw new IllegalArgumentException("Onverwachte attribute");
                }

                NotificatieException exception = assertThrows(NotificatieException.class, builder::buildNotificatie);

                assertTrue(exception.getMissingAttributes().contains(expectedAttribute));
            }
        }

        @Nested
        @DisplayName("Ongeldige Veld Tests")
        class InvalidFieldTests {

            @Test
            void testEmptyType() {
                Notificatie.NotificatieBuilder builder = Notificatie.notificatieBuilder()
                        .buildCode("DLW-NOTI-001")
                        .buildDatum(testDatum)
                        .buildType("   ")
                        .buildInhoud("Test inhoud")
                        .buildStatus(NotificatieStatus.ONGELEZEN)
                        .buildGebruiker(mockGebruiker);

                NotificatieException exception = assertThrows(NotificatieException.class, builder::buildNotificatie);
                assertTrue(exception.getMissingAttributes().contains(NotificatieAttributes.TYPE));
            }

            @Test
            void testEmptyInhoud() {
                Notificatie.NotificatieBuilder builder = Notificatie.notificatieBuilder()
                        .buildCode("DLW-NOTI-001")
                        .buildDatum(testDatum)
                        .buildType("INFO")
                        .buildInhoud("   ")
                        .buildStatus(NotificatieStatus.ONGELEZEN)
                        .buildGebruiker(mockGebruiker);

                NotificatieException exception = assertThrows(NotificatieException.class, builder::buildNotificatie);
                assertTrue(exception.getMissingAttributes().contains(NotificatieAttributes.INHOUD));
            }

            @Test
            void testNullType() {
                Notificatie.NotificatieBuilder builder = Notificatie.notificatieBuilder()
                        .buildCode("DLW-NOTI-001")
                        .buildDatum(testDatum)
                        .buildType(null)
                        .buildInhoud("Test inhoud")
                        .buildStatus(NotificatieStatus.ONGELEZEN)
                        .buildGebruiker(mockGebruiker);

                NotificatieException exception = assertThrows(NotificatieException.class, builder::buildNotificatie);
                assertTrue(exception.getMissingAttributes().contains(NotificatieAttributes.TYPE));
            }

            @Test
            void testNullInhoud() {
                Notificatie.NotificatieBuilder builder = Notificatie.notificatieBuilder()
                        .buildCode("DLW-NOTI-001")
                        .buildDatum(testDatum)
                        .buildType("INFO")
                        .buildInhoud(null)
                        .buildStatus(NotificatieStatus.ONGELEZEN)
                        .buildGebruiker(mockGebruiker);

                NotificatieException exception = assertThrows(NotificatieException.class, builder::buildNotificatie);
                assertTrue(exception.getMissingAttributes().contains(NotificatieAttributes.INHOUD));
            }

            @Test
            void testNullDatum() {
                Notificatie.NotificatieBuilder builder = Notificatie.notificatieBuilder()
                        .buildCode("DLW-NOTI-001")
                        .buildDatum(null)
                        .buildType("INFO")
                        .buildInhoud("Test inhoud")
                        .buildStatus(NotificatieStatus.ONGELEZEN)
                        .buildGebruiker(mockGebruiker);

                NotificatieException exception = assertThrows(NotificatieException.class, builder::buildNotificatie);
                assertTrue(exception.getMissingAttributes().contains(NotificatieAttributes.DATUM));
            }
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        static Collection<NotificatieStatus> statusProvider() {
            return Arrays.asList(NotificatieStatus.values());
        }

        @ParameterizedTest(name = "Status: {0}")
        @MethodSource("statusProvider")
        @DisplayName("Maak notificatie met alle statussen")
        void createNotificatieWithAllStatuses(NotificatieStatus status) throws NotificatieException {
            Notificatie notificatie = Notificatie.notificatieBuilder()
                    .buildCode("DLW-NOTI-001")
                    .buildDatum(testDatum)
                    .buildType("INFO")
                    .buildInhoud("Test")
                    .buildStatus(status)
                    .buildGebruiker(mockGebruiker)
                    .buildNotificatie();

            assertEquals(status, notificatie.getStatus());
        }

        @Test
        @DisplayName("Code is optioneel veld")
        void createNotificatieWithoutCode() throws NotificatieException {
            Notificatie notificatie = Notificatie.notificatieBuilder()
                    .buildDatum(testDatum)
                    .buildType("INFO")
                    .buildInhoud("Test zonder code")
                    .buildStatus(NotificatieStatus.ONGELEZEN)
                    .buildGebruiker(mockGebruiker)
                    .buildNotificatie();

            assertNotNull(notificatie);
            assertNull(notificatie.getCode());
        }
    }
}