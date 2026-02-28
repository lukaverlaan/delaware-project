package domein;

import com.example._026javag03.domein.Gebruiker;
import com.example._026javag03.domein.GebruikerBeheerder;
import com.example._026javag03.exceptions.AdresException;
import com.example._026javag03.repository.GenericDao;
import com.example._026javag03.util.Rol;
import com.example._026javag03.util.Status;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GebruikerBeheerderTest {

    @Mock
    private GenericDao<Gebruiker> gebruikerRepo;

    @InjectMocks
    private GebruikerBeheerder gebruikerBeheerder;

    private Gebruiker testGebruiker1;
    private Gebruiker testGebruiker2;
    private List<Gebruiker> testGebruikerList;

//    private AdresBuilder buildGeldigAdres() {
//        AdresBuilder adresBuilder = new AdresBuilder();
//        adresBuilder.createAdres();
//        adresBuilder.buildStraat("Kerkstraat");
//        adresBuilder.buildHuisnr(10);
//        adresBuilder.buildPostcode(1000);
//        adresBuilder.buildStad("Brussel");
//        return adresBuilder;
//    }

    @BeforeEach
    void setUp() throws AdresException {

        testGebruiker1 = new Gebruiker.Builder(
                "Jansen",
                "Jan",
                LocalDate.of(1990, 1, 15),
                "Straat1",
                1,
                1000,
                "Brussel",
                "jan.jansen@example.com",
                Rol.WERKNEMER,
                Status.ACTIEF
        ).gsm("0470123456").build();

        testGebruiker2 = new Gebruiker.Builder(
                "Peters",
                "Pieter",
                LocalDate.of(1985, 6, 20),
                "Straat2",
                2,
                2000,
                "Antwerpen",
                "pieter.peters@example.com",
                Rol.ADMINISTRATOR,
                Status.ACTIEF
        ).build();

        testGebruikerList = Arrays.asList(testGebruiker1, testGebruiker2);
    }

    @Nested
    @DisplayName("Tests voor getGebruikerList()")
    class GetGebruikerListTests {

        @Test
        @DisplayName("Moet lijst retourneren wanneer gebruikers bestaan")
        void getGebruikerList_MetGebruikers_ReturnsList() {
            when(gebruikerRepo.findAll()).thenReturn(testGebruikerList);

            List<Gebruiker> result = gebruikerBeheerder.getGebruikerList();

            assertNotNull(result);
            assertEquals(2, result.size());
            assertEquals("Jansen", result.get(0).getNaam());
            assertEquals("Peters", result.get(1).getNaam());
            verify(gebruikerRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Moet lege lijst retourneren wanneer geen gebruikers bestaan")
        void getGebruikerList_GeenGebruikers_ReturnsEmptyList() {
            when(gebruikerRepo.findAll()).thenReturn(Collections.emptyList());

            List<Gebruiker> result = gebruikerBeheerder.getGebruikerList();

            assertNotNull(result);
            assertTrue(result.isEmpty());
            verify(gebruikerRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Moet cached lijst retourneren bij tweede aanroep")
        void getGebruikerList_TweedeAanroep_ReturnsCachedList() {
            when(gebruikerRepo.findAll()).thenReturn(testGebruikerList);

            List<Gebruiker> firstResult = gebruikerBeheerder.getGebruikerList();
            List<Gebruiker> secondResult = gebruikerBeheerder.getGebruikerList();

            assertSame(firstResult, secondResult);
            verify(gebruikerRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Moet null retourneren wanneer repository null retourneert")
        void getGebruikerList_RepositoryReturnsNull_ReturnsNull() {
            when(gebruikerRepo.findAll()).thenReturn(null);

            List<Gebruiker> result = gebruikerBeheerder.getGebruikerList();

            assertNull(result);
            verify(gebruikerRepo, times(1)).findAll();
        }
    }

    @Nested
    @DisplayName("Tests voor insertGebruiker()")
    class InsertGebruikerTests {

        @Test
        @DisplayName("Moet insert methode aanroepen met juiste gebruiker")
        void insertGebruiker_ValidGebruiker_CallsInsert() throws AdresException {


            Gebruiker nieuweGebruiker = new Gebruiker.Builder(
                    "Nieuw",
                    "Gebruiker",
                    LocalDate.of(1995, 3, 10),
                    "stationstaraat",
                    25,
                    2000,
                    "Antwerpen",
                    "nieuw@example.com",
                    Rol.WERKNEMER,
                    Status.ACTIEF
            ).gsm("0470111222").build();

            doNothing().when(gebruikerRepo).insert(nieuweGebruiker);

            gebruikerBeheerder.insertGebruiker(nieuweGebruiker);

            verify(gebruikerRepo, times(1)).insert(nieuweGebruiker);
        }

        @Test
        @DisplayName("Moet insert aanroepen met null gebruiker")
        void insertGebruiker_NullGebruiker_CallsInsertWithNull() {
            doNothing().when(gebruikerRepo).insert(null);

            gebruikerBeheerder.insertGebruiker(null);

            verify(gebruikerRepo, times(1)).insert(null);
        }

        @Test
        @DisplayName("Moet gebruiker met alle velden correct doorgeven")
        void insertGebruiker_GebruikerMetVelden_PassesAllFields() throws AdresException {


            Gebruiker gebruikerMetVelden = new Gebruiker.Builder(
                    "Volledige",
                    "Naam",
                    LocalDate.of(1988, 7, 22),
                    "stationstaraat",
                    25,
                    2000,
                    "Antwerpen",
                    "volledig@example.com",
                    Rol.WERKNEMER,
                    Status.ACTIEF
            ).gsm("0470333444").build();

            doNothing().when(gebruikerRepo).insert(gebruikerMetVelden);

            gebruikerBeheerder.insertGebruiker(gebruikerMetVelden);

            verify(gebruikerRepo).insert(argThat(gebruiker ->
                    gebruiker.getNaam().equals("Volledige") &&
                            gebruiker.getVoornaam().equals("Naam") &&
                            gebruiker.getEmail().equals("volledig@example.com")
            ));
        }
    }

    /*@Nested
    @DisplayName("Tests voor updateGebruiker()")
    class UpdateGebruikerTests {
        @Test
        @DisplayName("Moet update methode aanroepen met juiste gebruiker")
        void updateGebruiker_ValidGebruiker_CallsUpdate() throws AdresException {
            AdresBuilder adresBuilder = buildGeldigAdres();


            Gebruiker teUpdatenGebruiker = new Gebruiker.Builder(
                    "Bijgewerkt",
                    "Naam",
                    LocalDate.of(1990, 1, 15),
                    adresBuilder,
                    "stationstaraat",
                    25,
                    2000,
                    "Antwerpen",
                    "update@example.com",
                    Rol.ADMINISTRATOR,
                    Status.ACTIEF
            ).build();

            doNothing().when(gebruikerRepo).update(teUpdatenGebruiker);

            gebruikerBeheerder.updateGebruiker(teUpdatenGebruiker);

            verify(gebruikerRepo, times(1)).update(teUpdatenGebruiker);
        }


            @Test
        @DisplayName("Moet update aanroepen met null gebruiker")
        void updateGebruiker_NullGebruiker_CallsUpdateWithNull() {
            doNothing().when(gebruikerRepo).update(null);

            gebruikerBeheerder.updateGebruiker(null);

            verify(gebruikerRepo, times(1)).update(null);
        }

        @Test
        @DisplayName("Moet update aanroepen voor bestaande gebruiker")
        void updateGebruiker_BestaandeGebruiker_CallsUpdate() {
            doNothing().when(gebruikerRepo).update(testGebruiker1);

            gebruikerBeheerder.updateGebruiker(testGebruiker1);

            verify(gebruikerRepo, times(1)).update(testGebruiker1);
        }
    }*/

    @Nested
    @DisplayName("Integration tests voor constructor en dependencies")
    class ConstructorTests {

        @Test
        @DisplayName("Moet correct initialiseren met geldige repository")
        void constructor_ValidRepository_InitializesCorrectly() {
            GebruikerBeheerder beheerder = new GebruikerBeheerder(gebruikerRepo);

            assertNotNull(beheerder);
            verifyNoInteractions(gebruikerRepo);
        }
    }

    @Nested
    @DisplayName("Tests voor interactie met repository")
    class RepositoryInteractionTests {

        @Test
        @DisplayName("Moet repository methoden niet aanroepen bij constructie")
        void constructor_NoRepositoryCalls() {
            new GebruikerBeheerder(gebruikerRepo);

            verifyNoInteractions(gebruikerRepo);
        }

        @Test
        @DisplayName("Moet findAll exact één keer aanroepen bij getGebruikerList")
        void getGebruikerList_CallsFindAllOnce() {
            when(gebruikerRepo.findAll()).thenReturn(testGebruikerList);

            gebruikerBeheerder.getGebruikerList();

            verify(gebruikerRepo, times(1)).findAll();
        }

        @Test
        @DisplayName("Moet insert exact één keer aanroepen bij insertGebruiker")
        void insertGebruiker_CallsInsertOnce() {
            doNothing().when(gebruikerRepo).insert(any(Gebruiker.class));

            gebruikerBeheerder.insertGebruiker(testGebruiker1);

            verify(gebruikerRepo, times(1)).insert(testGebruiker1);
        }
    }

    @Nested
    @DisplayName("Tests met verschillende rollen en statussen")
    class RolEnStatusTests {

        @Test
        @DisplayName("Moet correct werken met WERKNEMER rol")
        void insertGebruiker_Werknemer_CorrectlyInserted() throws AdresException {

            Gebruiker werknemer = new Gebruiker.Builder(
                    "Test",
                    "Werknemer",
                    LocalDate.of(1990, 5, 15),
                    "stationstaraat",
                    25,
                    2000,
                    "Antwerpen",
                    "werknemer@example.com",
                    Rol.WERKNEMER,
                    Status.ACTIEF
            ).gsm("0470555666").build();

            doNothing().when(gebruikerRepo).insert(werknemer);

            gebruikerBeheerder.insertGebruiker(werknemer);

            verify(gebruikerRepo).insert(argThat(g ->
                    g.getRol() == Rol.WERKNEMER && g.getGsm() != null
            ));
        }

        @Test
        @DisplayName("Moet correct werken met verschillende statussen")
        void getGebruikerList_VerschillendeStatussen_ReturnsAll() throws AdresException {


            Gebruiker inactiefGebruiker = new Gebruiker.Builder(
                    "Inactief",
                    "Gebruiker",
                    LocalDate.of(1992, 3, 5),
                    "stationstraat",
                    25,
                    2000,
                    "Antwerpen",
                    "inactief@example.com",
                    Rol.WERKNEMER,
                    Status.INACTIEF
            ).gsm("0470777888").build();

            List<Gebruiker> gemixteLijst = Arrays.asList(testGebruiker1, inactiefGebruiker);
            when(gebruikerRepo.findAll()).thenReturn(gemixteLijst);

            List<Gebruiker> result = gebruikerBeheerder.getGebruikerList();

            assertEquals(2, result.size());
            assertTrue(result.stream().anyMatch(g -> g.getStatus() == Status.ACTIEF));
            assertTrue(result.stream().anyMatch(g -> g.getStatus() == Status.INACTIEF));
        }
    }
}