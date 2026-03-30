package domein.team;

import com.example._026javag03.domein.Team;
import com.example._026javag03.domein.beheerder.TeamBeheerder;
import com.example._026javag03.repository.GenericDao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TeamBeheerderTest {

    @Mock
    private GenericDao<Team> teamRepo;

    @InjectMocks
    private TeamBeheerder teamBeheerder;

    /* ============================================================
       HULPMETHODES
       ============================================================ */

    private static Team maakTeam(Long id) {
        Team team = new Team();
        team.setId(id);
        return team;
    }

    /* ============================================================
       getTeamList()
       ============================================================ */

    private static Stream<List<Team>> teamLijsten() {
        return Stream.of(
                List.of(
                        maakTeam(1L),
                        maakTeam(2L)
                ),
                List.of()
        );
    }

    @ParameterizedTest
    @MethodSource("teamLijsten")
    @DisplayName("getTeamList retourneert correcte lijst")
    void getTeamList_ReturnsCorrectList(List<Team> lijst) {

        when(teamRepo.findAll()).thenReturn(lijst);

        List<Team> result = teamBeheerder.getTeamList();

        assertEquals(lijst, result);
        verify(teamRepo, times(1)).findAll();
    }

    /* ============================================================
       insertTeam()
       ============================================================ */

    private static Stream<Team> geldigeTeams() {
        return Stream.of(
                maakTeam(null),
                maakTeam(null)
        );
    }

    @ParameterizedTest
    @MethodSource("geldigeTeams")
    @DisplayName("insertTeam start en commit transactie")
    void insertTeam_StartsAndCommitsTransaction(Team team) {

        when(teamRepo.findAll()).thenReturn(List.of());

        teamBeheerder.insertTeam(team);

        assertNotNull(team.getCode());

        verify(teamRepo).startTransaction();
        verify(teamRepo).insert(team);
        verify(teamRepo).commitTransaction();
    }

    @Test
    @DisplayName("insertTeam rollback bij exception")
    void insertTeam_RollbackBijFout() {

        Team team = maakTeam(null);

        when(teamRepo.findAll()).thenReturn(List.of());

        doThrow(new RuntimeException())
                .when(teamRepo)
                .insert(team);

        assertThrows(IllegalArgumentException.class,
                () -> teamBeheerder.insertTeam(team));

        verify(teamRepo).rollbackTransaction();
    }

    /* ============================================================
       updateTeam()
       ============================================================ */

    @Test
    @DisplayName("updateTeam start en commit transactie")
    void updateTeam_StartsAndCommitsTransaction() {

        Team team = maakTeam(1L);

        teamBeheerder.updateTeam(team);

        verify(teamRepo).startTransaction();
        verify(teamRepo).update(team);
        verify(teamRepo).commitTransaction();
    }

    @Test
    @DisplayName("updateTeam rollback bij exception")
    void updateTeam_RollbackBijFout() {

        Team team = maakTeam(1L);

        doThrow(new RuntimeException())
                .when(teamRepo)
                .update(team);

        assertThrows(IllegalArgumentException.class,
                () -> teamBeheerder.updateTeam(team));

        verify(teamRepo).rollbackTransaction();
    }

    /* ============================================================
       deleteTeam()
       ============================================================ */

    @Test
    @DisplayName("deleteTeam start en commit transactie")
    void deleteTeam_StartsAndCommitsTransaction() {

        Team team = maakTeam(1L);

        teamBeheerder.deleteTeam(team);

        verify(teamRepo).startTransaction();
        verify(teamRepo).delete(team);
        verify(teamRepo).commitTransaction();
    }

    /* ============================================================
       genereerTeamCode()
       ============================================================ */

    @Test
    @DisplayName("genereerTeamCode maakt correcte code")
    void genereerTeamCode_ReturnsCorrectCode() {

        when(teamRepo.findAll()).thenReturn(
                List.of(
                        maakTeam(1L),
                        maakTeam(5L),
                        maakTeam(3L)
                )
        );

        String code = teamBeheerder.genereerTeamCode();

        assertEquals("DLW-TEAM-0006", code);
    }

    @Test
    @DisplayName("genereerTeamCode werkt wanneer er nog geen teams zijn")
    void genereerTeamCode_EmptyList() {

        when(teamRepo.findAll()).thenReturn(List.of());

        String code = teamBeheerder.genereerTeamCode();

        assertEquals("DLW-TEAM-0001", code);
    }

    /* ============================================================
       closePersistency()
       ============================================================ */

    @Test
    @DisplayName("closePersistency delegeert naar repo")
    void closePersistency_DelegatesToRepo() {

        teamBeheerder.closePersistency();

        verify(teamRepo).closePersistency();
    }

    /* ============================================================
       Constructor
       ============================================================ */

    @Test
    @DisplayName("Constructor initialiseert correct")
    void constructor_InitialiseertCorrect() {

        TeamBeheerder beheerder = new TeamBeheerder(teamRepo);

        assertNotNull(beheerder);

        verifyNoInteractions(teamRepo);
    }
}