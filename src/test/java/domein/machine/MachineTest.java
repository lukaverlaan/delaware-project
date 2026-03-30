package domein.machine;

import com.example._026javag03.domein.Gebruiker;
import com.example._026javag03.domein.Machine;
import com.example._026javag03.domein.Site;
import com.example._026javag03.exceptions.MachineException;
import com.example._026javag03.util.machine.MachineAttributes;
import com.example._026javag03.util.machine.ProductieStatusMachine;
import com.example._026javag03.util.machine.StatusMachine;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@DisplayName("Machine Domeinklasse Tests")
class MachineTest {

    private static Site mockSite;
    private static Gebruiker mockGebruiker;

    @BeforeAll
    static void setUpAll() {
        mockSite = mock(Site.class);
        when(mockSite.getNaam()).thenReturn("Test Site");

        mockGebruiker = mock(Gebruiker.class);
        when(mockGebruiker.getNaam()).thenReturn("Test Medewerker");
    }

    private static Machine.MachineBuilder createValidBuilder() {
        return Machine.machineBuilder()
                .buildSite(mockSite)
                .buildLocatie("Productiehal A")
                .buildProductInfo("Productie van widgets")
                .buildStatus(StatusMachine.DRAAIT)
                .buildProductieStatus(ProductieStatusMachine.GEZOND)
                .buildWerknemers(List.of(mockGebruiker));
    }

    @Nested
    @DisplayName("Successful Creation Tests")
    class SuccessfulCreationTests {

        static Collection<StatusMachine> statusProvider() {
            return Arrays.asList(StatusMachine.values());
        }

        static Collection<ProductieStatusMachine> productieStatusProvider() {
            return Arrays.asList(ProductieStatusMachine.values());
        }

        @Test
        @DisplayName("Maak geldige machine met alle verplichte velden")
        void createValidMachine() throws MachineException {
            Machine machine = createValidBuilder().build();

            assertNotNull(machine);
            assertEquals(mockSite, machine.getSite());
            assertEquals("Productiehal A", machine.getLocatie());
            assertEquals("Productie van widgets", machine.getProductinfo());
            assertEquals(StatusMachine.DRAAIT, machine.getStatus());
            assertEquals(ProductieStatusMachine.GEZOND, machine.getProductieStatus());
            assertTrue(machine.getWerknemers().contains(mockGebruiker));
        }

        @ParameterizedTest(name = "Status: {0}")
        @MethodSource("statusProvider")
        @DisplayName("Maak machine met verschillende statussen")
        void createMachineWithStatus(StatusMachine status) throws MachineException {
            Machine machine = Machine.machineBuilder()
                    .buildSite(mockSite)
                    .buildLocatie("Test Locatie")
                    .buildProductInfo("Test Info")
                    .buildStatus(status)
                    .buildProductieStatus(ProductieStatusMachine.GEZOND)
                    .buildWerknemers(List.of(mockGebruiker))
                    .build();

            assertNotNull(machine);
            assertEquals(status, machine.getStatus());
        }

        @ParameterizedTest(name = "Productie Status: {0}")
        @MethodSource("productieStatusProvider")
        @DisplayName("Maak machine met verschillende productie statussen")
        void createMachineWithProductieStatus(ProductieStatusMachine status) throws MachineException {
            Machine machine = Machine.machineBuilder()
                    .buildSite(mockSite)
                    .buildLocatie("Test Locatie")
                    .buildProductInfo("Test Info")
                    .buildStatus(StatusMachine.DRAAIT)
                    .buildProductieStatus(status)
                    .buildWerknemers(List.of(mockGebruiker))
                    .build();

            assertNotNull(machine);
            assertEquals(status, machine.getProductieStatus());
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
                        {"testMissingSite", MachineAttributes.SITE},
                        {"testMissingLocatie", MachineAttributes.LOCATIE},
                        {"testMissingStatus", MachineAttributes.STATUS},
                        {"testMissingProductieStatus", MachineAttributes.PRODUCTIE_STATUS},
                        {"testMissingProductInfo", MachineAttributes.PRODUCTIE_INFO}
                });
            }

            @ParameterizedTest(name = "{0}")
            @MethodSource("missingFieldTestData")
            void testMissingField(String testName, MachineAttributes expectedAttribute) {

                Machine.MachineBuilder builder;

                switch (expectedAttribute) {
                    case SITE -> builder = Machine.machineBuilder()
                            .buildLocatie("Test Locatie")
                            .buildProductInfo("Test Info")
                            .buildStatus(StatusMachine.DRAAIT)
                            .buildProductieStatus(ProductieStatusMachine.GEZOND)
                            .buildWerknemers(List.of(mockGebruiker));
                    case LOCATIE -> builder = Machine.machineBuilder()
                            .buildSite(mockSite)
                            .buildProductInfo("Test Info")
                            .buildStatus(StatusMachine.DRAAIT)
                            .buildProductieStatus(ProductieStatusMachine.GEZOND)
                            .buildWerknemers(List.of(mockGebruiker));
                    case STATUS -> builder = Machine.machineBuilder()
                            .buildSite(mockSite)
                            .buildLocatie("Test Locatie")
                            .buildProductInfo("Test Info")
                            .buildProductieStatus(ProductieStatusMachine.GEZOND)
                            .buildWerknemers(List.of(mockGebruiker));
                    case PRODUCTIE_STATUS -> builder = Machine.machineBuilder()
                            .buildSite(mockSite)
                            .buildLocatie("Test Locatie")
                            .buildProductInfo("Test Info")
                            .buildStatus(StatusMachine.DRAAIT)
                            .buildWerknemers(List.of(mockGebruiker));
                    case PRODUCTIE_INFO -> builder = Machine.machineBuilder()
                            .buildSite(mockSite)
                            .buildLocatie("Test Locatie")
                            .buildStatus(StatusMachine.DRAAIT)
                            .buildProductieStatus(ProductieStatusMachine.GEZOND)
                            .buildWerknemers(List.of(mockGebruiker));
                    default -> throw new IllegalArgumentException("Onverwachte attribute");
                }

                MachineException exception = assertThrows(MachineException.class, builder::build);

                assertTrue(exception.getMissingAttributes().contains(expectedAttribute));
            }
        }

        @Nested
        @DisplayName("Ongeldige Veld Tests")
        class InvalidFieldTests {

            @Test
            void testEmptyLocatie() {
                Machine.MachineBuilder builder = Machine.machineBuilder()
                        .buildSite(mockSite)
                        .buildLocatie("   ")
                        .buildProductInfo("Test Info")
                        .buildStatus(StatusMachine.DRAAIT)
                        .buildProductieStatus(ProductieStatusMachine.GEZOND)
                        .buildWerknemers(List.of(mockGebruiker));

                MachineException exception = assertThrows(MachineException.class, builder::build);
                assertTrue(exception.getMissingAttributes().contains(MachineAttributes.LOCATIE));
            }

            @Test
            void testEmptyProductInfo() {
                Machine.MachineBuilder builder = Machine.machineBuilder()
                        .buildSite(mockSite)
                        .buildLocatie("Test Locatie")
                        .buildProductInfo("   ")
                        .buildStatus(StatusMachine.DRAAIT)
                        .buildProductieStatus(ProductieStatusMachine.GEZOND)
                        .buildWerknemers(List.of(mockGebruiker));

                MachineException exception = assertThrows(MachineException.class, builder::build);
                assertTrue(exception.getMissingAttributes().contains(MachineAttributes.PRODUCTIE_INFO));
            }

            @Test
            void testNullLocatie() {
                Machine.MachineBuilder builder = Machine.machineBuilder()
                        .buildSite(mockSite)
                        .buildLocatie(null)
                        .buildProductInfo("Test Info")
                        .buildStatus(StatusMachine.DRAAIT)
                        .buildProductieStatus(ProductieStatusMachine.GEZOND)
                        .buildWerknemers(List.of(mockGebruiker));

                MachineException exception = assertThrows(MachineException.class, builder::build);
                assertTrue(exception.getMissingAttributes().contains(MachineAttributes.LOCATIE));
            }

            @Test
            void testNullProductInfo() {
                Machine.MachineBuilder builder = Machine.machineBuilder()
                        .buildSite(mockSite)
                        .buildLocatie("Test Locatie")
                        .buildProductInfo(null)
                        .buildStatus(StatusMachine.DRAAIT)
                        .buildProductieStatus(ProductieStatusMachine.GEZOND)
                        .buildWerknemers(List.of(mockGebruiker));

                MachineException exception = assertThrows(MachineException.class, builder::build);
                assertTrue(exception.getMissingAttributes().contains(MachineAttributes.PRODUCTIE_INFO));
            }
        }
    }

    @Nested
    @DisplayName("Edge Case Tests")
    class EdgeCaseTests {

        static Collection<ProductieStatusMachine> productieStatusProvider() {
            return Arrays.asList(ProductieStatusMachine.values());
        }

        @Test
        @DisplayName("Maak machine zonder werknemers")
        void createMachineWithoutWerknemers() throws MachineException {
            Machine machine = Machine.machineBuilder()
                    .buildSite(mockSite)
                    .buildLocatie("Test Locatie")
                    .buildProductInfo("Test Info")
                    .buildStatus(StatusMachine.GESTOPT_AUTO)
                    .buildProductieStatus(ProductieStatusMachine.OFFLINE)
                    .build();

            assertNotNull(machine);
            assertTrue(machine.getWerknemers().isEmpty());
        }

        @ParameterizedTest(name = "Productie status: {0}")
        @MethodSource("productieStatusProvider")
        void createMachineWithAllProductieStatuses(ProductieStatusMachine status) throws MachineException {
            Machine machine = Machine.machineBuilder()
                    .buildSite(mockSite)
                    .buildLocatie("Test")
                    .buildProductInfo("Test")
                    .buildStatus(StatusMachine.DRAAIT)
                    .buildProductieStatus(status)
                    .buildWerknemers(List.of(mockGebruiker))
                    .build();

            assertEquals(status, machine.getProductieStatus());
        }
    }
}