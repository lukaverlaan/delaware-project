package domein;

import com.example._026javag03.domein.Gebruiker;
import com.example._026javag03.domein.beheerder.GebruikerBeheerder;
import com.example._026javag03.exceptions.AdresException;
import com.example._026javag03.repository.gebruiker.GebruikerDao;
import com.example._026javag03.util.gebruiker.Rol;
import com.example._026javag03.util.Status;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class GebruikerBeheerderTest {

    @Mock
    private GebruikerDao gebruikerRepo;

    @InjectMocks
    private GebruikerBeheerder gebruikerBeheerder;

    /* ============================================================
       HULPMETHODES
       ============================================================ */

    private static Gebruiker maakGebruiker(String naam, String email, Rol rol)
            throws AdresException {

        Gebruiker.Builder builder = new Gebruiker.Builder(
                naam,
                "Voornaam",
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

        if (rol == Rol.WERKNEMER) {
            builder.gsm("0470123456");
        }

        return builder.build();
    }

    /* ============================================================
       getGebruikerList()
       ============================================================ */

    private static Stream<List<Gebruiker>> gebruikerLijsten() throws AdresException {
        return Stream.of(
                List.of(
                        maakGebruiker("Jan", "jan@test.com", Rol.ADMINISTRATOR),
                        maakGebruiker("Piet", "piet@test.com", Rol.WERKNEMER)
                ),
                List.of()
        );
    }

    @ParameterizedTest
    @MethodSource("gebruikerLijsten")
    @DisplayName("getGebruikerList retourneert correcte lijst")
    void getGebruikerList_ReturnsCorrectList(List<Gebruiker> lijst) {

        when(gebruikerRepo.findAll()).thenReturn(lijst);

        List<Gebruiker> result = gebruikerBeheerder.getGebruikerList();

        assertEquals(lijst, result);
        verify(gebruikerRepo, times(1)).findAll();
    }

    /* ============================================================
       insertGebruiker()
       ============================================================ */

    private static Stream<Gebruiker> geldigeGebruikers() throws AdresException {
        return Stream.of(
                maakGebruiker("Jan", "jan@test.com", Rol.ADMINISTRATOR),
                maakGebruiker("Piet", "piet@test.com", Rol.WERKNEMER)
        );
    }

    @ParameterizedTest
    @MethodSource("geldigeGebruikers")
    @DisplayName("insertGebruiker start en commit transactie")
    void insertGebruiker_StartsAndCommitsTransaction(Gebruiker gebruiker) {

        gebruikerBeheerder.insertGebruiker(gebruiker);

        verify(gebruikerRepo).startTransaction();
        verify(gebruikerRepo).insert(gebruiker);
        verify(gebruikerRepo).commitTransaction();
    }

    /* ============================================================
       updateGebruiker()
       ============================================================ */

    @ParameterizedTest
    @MethodSource("geldigeGebruikers")
    @DisplayName("updateGebruiker start en commit transactie")
    void updateGebruiker_StartsAndCommitsTransaction(Gebruiker gebruiker) {

        gebruikerBeheerder.updateGebruiker(gebruiker);

        verify(gebruikerRepo).startTransaction();
        verify(gebruikerRepo).update(gebruiker);
        verify(gebruikerRepo).commitTransaction();
    }

    @Test
    @DisplayName("updateGebruiker rollback bij exception")
    void updateGebruiker_RollbackBijFout() throws AdresException {

        Gebruiker gebruiker = maakGebruiker("Jan", "jan@test.com", Rol.ADMINISTRATOR);

        doThrow(new RuntimeException())
                .when(gebruikerRepo)
                .update(gebruiker);

        assertThrows(IllegalArgumentException.class,
                () -> gebruikerBeheerder.updateGebruiker(gebruiker));

        verify(gebruikerRepo).rollbackTransaction();
    }

    /* ============================================================
       verwijderGebruiker()
       ============================================================ */

    @Test
    @DisplayName("verwijderGebruiker zet status op INACTIEF")
    void verwijderGebruiker_ZetStatusInactief() throws AdresException {

        Gebruiker gebruiker = maakGebruiker("Jan", "jan@test.com", Rol.ADMINISTRATOR);

        gebruikerBeheerder.verwijderGebruiker(gebruiker);

        assertEquals(Status.INACTIEF, gebruiker.getStatus());
        verify(gebruikerRepo).update(gebruiker);
    }

    /* ============================================================
       Constructor test
       ============================================================ */

    @Test
    @DisplayName("Constructor initialiseert correct")
    void constructor_InitialiseertCorrect() {

        GebruikerBeheerder beheerder = new GebruikerBeheerder(gebruikerRepo);

        assertNotNull(beheerder);
        verifyNoInteractions(gebruikerRepo);
    }
}