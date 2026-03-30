package domein.machine;

import com.example._026javag03.domein.Gebruiker;
import com.example._026javag03.domein.Machine;
import com.example._026javag03.domein.Site;
import com.example._026javag03.domein.beheerder.MachineBeheerder;
import com.example._026javag03.exceptions.MachineException;
import com.example._026javag03.repository.GenericDao;
import com.example._026javag03.util.machine.ProductieStatusMachine;
import com.example._026javag03.util.machine.StatusMachine;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("MachineBeheerder Tests")
class MachineBeheerderTest {

    @Mock
    private GenericDao<Machine> machineRepo;

    @InjectMocks
    private MachineBeheerder machineBeheerder;

    /* ============================================================
       HULPMETHODES
       ============================================================ */

    private static Site mockSite() {
        return mock(Site.class);
    }

    private static Gebruiker mockGebruiker() {
        return mock(Gebruiker.class);
    }

    private static Machine maakMachine(String locatie,
                                       String productInfo,
                                       StatusMachine status,
                                       ProductieStatusMachine productieStatus) throws MachineException {
        return Machine.machineBuilder()
                .buildSite(mockSite())
                .buildLocatie(locatie)
                .buildProductInfo(productInfo)
                .buildStatus(status)
                .buildProductieStatus(productieStatus)
                .buildWerknemers(List.of(mockGebruiker())) // ✅ FIX
                .build();
    }

    /* ============================================================
       getMachines()
       ============================================================ */

    private static Stream<List<Machine>> machineLijsten() throws MachineException {
        return Stream.of(
                List.of(
                        maakMachine("Productiehal A", "Widgets", StatusMachine.DRAAIT, ProductieStatusMachine.GEZOND),
                        maakMachine("Productiehal B", "Gadgets", StatusMachine.DRAAIT, ProductieStatusMachine.GEZOND)
                ),
                List.of()
        );
    }

    @ParameterizedTest
    @MethodSource("machineLijsten")
    @DisplayName("getMachines retourneert correcte lijst")
    void getMachines_ReturnsCorrectList(List<Machine> lijst) {

        when(machineRepo.findAll()).thenReturn(lijst);

        List<Machine> result = machineBeheerder.getMachines();

        assertEquals(lijst, result);
        verify(machineRepo, times(1)).findAll();
    }

    /* ============================================================
       insertMachine()
       ============================================================ */

    private static Stream<Machine> geldigeMachines() throws MachineException {
        return Stream.of(
                maakMachine("Productiehal A", "Widgets", StatusMachine.DRAAIT, ProductieStatusMachine.GEZOND),
                maakMachine("Productiehal B", "Gadgets", StatusMachine.GESTOPT_AUTO, ProductieStatusMachine.OFFLINE)
        );
    }

    @ParameterizedTest
    @MethodSource("geldigeMachines")
    @DisplayName("insertMachine start en commit transactie")
    void insertMachine_StartsAndCommitsTransaction(Machine machine) {

        machineBeheerder.insertMachine(machine);

        verify(machineRepo).startTransaction();
        verify(machineRepo).insert(machine);
        verify(machineRepo).commitTransaction();
    }

    /* ============================================================
       updateMachine()
       ============================================================ */

    @ParameterizedTest
    @MethodSource("geldigeMachines")
    @DisplayName("updateMachine start en commit transactie")
    void updateMachine_StartsAndCommitsTransaction(Machine machine) {

        machineBeheerder.updateMachine(machine);

        verify(machineRepo).startTransaction();
        verify(machineRepo).update(machine);
        verify(machineRepo).commitTransaction();
    }

    @Test
    @DisplayName("updateMachine rollback bij exception")
    void updateMachine_RollbackBijFout() throws MachineException {

        Machine machine = maakMachine("Test Machine", "Test Info",
                StatusMachine.DRAAIT, ProductieStatusMachine.GEZOND);
        machine.setId(1L);

        doThrow(new RuntimeException("Update fout"))
                .when(machineRepo)
                .update(machine);

        assertThrows(IllegalArgumentException.class, () ->
                machineBeheerder.updateMachine(machine)
        );

        verify(machineRepo).startTransaction();
        verify(machineRepo).update(machine);
        verify(machineRepo, never()).commitTransaction();
        verify(machineRepo).rollbackTransaction();
    }

    /* ============================================================
       deleteMachine()
       ============================================================ */

    @Test
    @DisplayName("deleteMachine roept delete aan")
    void deleteMachine_CallsDelete() throws MachineException {

        Machine machine = maakMachine("Delete Machine", "Test Info", StatusMachine.DRAAIT, ProductieStatusMachine.GEZOND);
        machine.setId(1L);

        machineBeheerder.deleteMachine(machine);

        verify(machineRepo).delete(machine);
    }

    @Test
    @DisplayName("deleteMachine geen transactie (volgens originele code)")
    void deleteMachine_NoTransaction() throws MachineException {

        Machine machine = maakMachine("Delete Machine", "Test Info", StatusMachine.DRAAIT, ProductieStatusMachine.GEZOND);

        machineBeheerder.deleteMachine(machine);

        verify(machineRepo).startTransaction();
        verify(machineRepo).delete(machine);
        verify(machineRepo).commitTransaction();
    }

    /* ============================================================
       closePersistency()
       ============================================================ */

    @Test
    @DisplayName("closePersistency delegeert naar repo")
    void closePersistency_DelegatesToRepo() {

        machineBeheerder.closePersistency();

        verify(machineRepo).closePersistency();
    }

    /* ============================================================
       Constructor test
       ============================================================ */

    @Test
    @DisplayName("Constructor initialiseert correct")
    void constructor_InitialiseertCorrect() {

        GenericDao<Machine> repo = mock(GenericDao.class);
        MachineBeheerder beheerder = new MachineBeheerder(repo);

        assertNotNull(beheerder);
        verifyNoInteractions(repo);
    }
}