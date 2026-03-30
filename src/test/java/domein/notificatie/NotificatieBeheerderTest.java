package domein.notificatie;

import com.example._026javag03.domein.Gebruiker;
import com.example._026javag03.domein.Notificatie;
import com.example._026javag03.domein.beheerder.NotificatieBeheerder;
import com.example._026javag03.exceptions.NotificatieException;
import com.example._026javag03.repository.GenericDao;
import com.example._026javag03.util.notificatie.NotificatieStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("NotificatieBeheerder Tests")
class NotificatieBeheerderTest {

    @Mock
    private GenericDao<Notificatie> notificatieRepo;

    @InjectMocks
    private NotificatieBeheerder notificatieBeheerder;

    /* ============================================================
       HULPMETHODES
       ============================================================ */

    private static Gebruiker mockGebruiker() {
        return mock(Gebruiker.class);
    }

    private static Notificatie maakNotificatie(String code,
                                               String type,
                                               String inhoud,
                                               NotificatieStatus status) throws NotificatieException {
        return Notificatie.notificatieBuilder()
                .buildCode(code)
                .buildDatum(LocalDateTime.now())
                .buildType(type)
                .buildInhoud(inhoud)
                .buildStatus(status)
                .buildGebruiker(mockGebruiker())
                .buildNotificatie();
    }

    /* ============================================================
       getNotificaties()
       ============================================================ */

    private static Stream<List<Notificatie>> notificatieLijsten() throws NotificatieException {
        return Stream.of(
                List.of(
                        maakNotificatie("DLW-NOTI-001", "INFO", "Test notificatie 1", NotificatieStatus.ONGELEZEN),
                        maakNotificatie("DLW-NOTI-002", "WARNING", "Test notificatie 2", NotificatieStatus.GELEZEN)
                ),
                List.of()
        );
    }

    @ParameterizedTest
    @MethodSource("notificatieLijsten")
    @DisplayName("getNotificaties retourneert correcte lijst")
    void getNotificaties_ReturnsCorrectList(List<Notificatie> lijst) {

        when(notificatieRepo.findAll()).thenReturn(lijst);

        List<Notificatie> result = notificatieBeheerder.getNotificaties();

        assertEquals(lijst, result);
        verify(notificatieRepo, times(1)).findAll();
    }

    /* ============================================================
       insertNotificatie()
       ============================================================ */

    private static Stream<Notificatie> geldigeNotificaties() throws NotificatieException {
        return Stream.of(
                maakNotificatie(null, "INFO", "Test notificatie", NotificatieStatus.ONGELEZEN),
                maakNotificatie(null, "ERROR", "Fout notificatie", NotificatieStatus.GELEZEN)
        );
    }

    @ParameterizedTest
    @MethodSource("geldigeNotificaties")
    @DisplayName("insertNotificatie start en commit transactie")
    void insertNotificatie_StartsAndCommitsTransaction(Notificatie notificatie) {

        when(notificatieRepo.findAll()).thenReturn(List.of());

        notificatieBeheerder.insertNotificatie(notificatie);

        verify(notificatieRepo).findAll();
        verify(notificatieRepo).startTransaction();
        verify(notificatieRepo).insert(notificatie);
        verify(notificatieRepo).commitTransaction();
    }

    @Test
    @DisplayName("insertNotificatie genereert correcte code")
    void insertNotificatie_GeneratesCorrectCode() throws NotificatieException {

        Notificatie bestaandeNotificatie = maakNotificatie("DLW-NOTI-001", "INFO", "Test", NotificatieStatus.ONGELEZEN);
        bestaandeNotificatie.setId(5L);
        when(notificatieRepo.findAll()).thenReturn(List.of(bestaandeNotificatie));

        Notificatie nieuweNotificatie = maakNotificatie(null, "INFO", "Nieuwe notificatie", NotificatieStatus.ONGELEZEN);

        notificatieBeheerder.insertNotificatie(nieuweNotificatie);

        assertEquals("DLW-NOTI-0006", nieuweNotificatie.getCode());
    }

    @Test
    @DisplayName("insertNotificatie rollback bij exception")
    void insertNotificatie_RollbackBijFout() throws NotificatieException {

        Notificatie notificatie = maakNotificatie(null, "INFO", "Test notificatie", NotificatieStatus.ONGELEZEN);
        when(notificatieRepo.findAll()).thenReturn(List.of());

        doThrow(new RuntimeException("Insert fout"))
                .when(notificatieRepo)
                .insert(notificatie);

        assertThrows(IllegalArgumentException.class, () ->
                notificatieBeheerder.insertNotificatie(notificatie)
        );

        verify(notificatieRepo).findAll();
        verify(notificatieRepo).startTransaction();
        verify(notificatieRepo).insert(notificatie);
        verify(notificatieRepo, never()).commitTransaction();
        verify(notificatieRepo).rollbackTransaction();
    }

    /* ============================================================
       updateNotificatie()
       ============================================================ */

    @ParameterizedTest
    @MethodSource("geldigeNotificaties")
    @DisplayName("updateNotificatie start en commit transactie")
    void updateNotificatie_StartsAndCommitsTransaction(Notificatie notificatie) {

        notificatieBeheerder.updateNotificatie(notificatie);

        verify(notificatieRepo).startTransaction();
        verify(notificatieRepo).update(notificatie);
        verify(notificatieRepo).commitTransaction();
    }

    @Test
    @DisplayName("updateNotificatie rollback bij exception")
    void updateNotificatie_RollbackBijFout() throws NotificatieException {

        Notificatie notificatie = maakNotificatie("DLW-NOTI-001", "INFO", "Test notificatie", NotificatieStatus.ONGELEZEN);

        doThrow(new RuntimeException("Update fout"))
                .when(notificatieRepo)
                .update(notificatie);

        assertThrows(IllegalArgumentException.class, () ->
                notificatieBeheerder.updateNotificatie(notificatie)
        );

        verify(notificatieRepo).startTransaction();
        verify(notificatieRepo).update(notificatie);
        verify(notificatieRepo, never()).commitTransaction();
        verify(notificatieRepo).rollbackTransaction();
    }

    /* ============================================================
       deleteNotificatie()
       ============================================================ */

    @Test
    @DisplayName("deleteNotificatie roept delete aan")
    void deleteNotificatie_CallsDelete() throws NotificatieException {

        Notificatie notificatie = maakNotificatie("DLW-NOTI-001", "INFO", "Test notificatie", NotificatieStatus.ONGELEZEN);

        notificatieBeheerder.deleteNotificatie(notificatie);

        verify(notificatieRepo).startTransaction();
        verify(notificatieRepo).delete(notificatie);
        verify(notificatieRepo).commitTransaction();
    }

    /* ============================================================
       closePersistency()
       ============================================================ */

    @Test
    @DisplayName("closePersistency delegeert naar repo")
    void closePersistency_DelegatesToRepo() {

        notificatieBeheerder.closePersistency();

        verify(notificatieRepo).closePersistency();
    }

    /* ============================================================
       genereerNotificatieCode()
       ============================================================ */

    @Test
    @DisplayName("genereerNotificatieCode genereert correcte code bij lege lijst")
    void genereerNotificatieCode_EmptyList() {

        when(notificatieRepo.findAll()).thenReturn(List.of());

        String result = notificatieBeheerder.genereerNotificatieCode();

        assertEquals("DLW-NOTI-0001", result);
    }

    @Test
    @DisplayName("genereerNotificatieCode genereert volgende code")
    void genereerNotificatieCode_NextNumber() {

        Notificatie notificatie1 = mock(Notificatie.class);
        when(notificatie1.getId()).thenReturn(3L);

        Notificatie notificatie2 = mock(Notificatie.class);
        when(notificatie2.getId()).thenReturn(7L);

        when(notificatieRepo.findAll()).thenReturn(List.of(notificatie1, notificatie2));

        String result = notificatieBeheerder.genereerNotificatieCode();

        assertEquals("DLW-NOTI-0008", result);
    }

    /* ============================================================
       Constructor test
       ============================================================ */

    @Test
    @DisplayName("Constructor initialiseert correct")
    void constructor_InitialiseertCorrect() {

        GenericDao<Notificatie> repo = mock(GenericDao.class);
        NotificatieBeheerder beheerder = new NotificatieBeheerder(repo);

        assertNotNull(beheerder);
        verifyNoInteractions(repo);
    }
}