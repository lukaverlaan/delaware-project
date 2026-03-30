package domein.site;

import com.example._026javag03.domein.Site;
import com.example._026javag03.domein.beheerder.SiteBeheerder;
import com.example._026javag03.exceptions.SiteException;
import com.example._026javag03.repository.site.SiteDao;
import com.example._026javag03.util.Status;
import com.example._026javag03.util.site.ProductieStatus;
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
class SiteBeheerderTest {

    @Mock
    private SiteDao siteRepo;

    @InjectMocks
    private SiteBeheerder siteBeheerder;

    /* ============================================================
       HULPMETHODES
       ============================================================ */

    private static Site maakSite(String naam,
                                 String locatie,
                                 int capaciteit,
                                 Status operationeleStatus,
                                 ProductieStatus productieStatus) throws SiteException {

        return Site.siteBuilder()
                .buildNaam(naam)
                .buildLocatie(locatie)
                .buildCapaciteit(capaciteit)
                .buildOperationeleStatus(operationeleStatus)
                .buildProductieStatus(productieStatus)
                .buildSite();
    }

    /* ============================================================
       getSites()
       ============================================================ */

    private static Stream<List<Site>> siteLijsten() throws SiteException {
        return Stream.of(
                List.of(
                        maakSite("gent", "gent", 5, Status.ACTIEF, ProductieStatus.GEZOND),
                        maakSite("antwerpen", "antwerpen", 6, Status.ACTIEF, ProductieStatus.GEZOND)
                ),
                List.of()
        );
    }

    @ParameterizedTest
    @MethodSource("siteLijsten")
    @DisplayName("getSites retourneert correcte lijst")
    void getSites_ReturnsCorrectList(List<Site> lijst) {

        when(siteRepo.findAll()).thenReturn(lijst);

        List<Site> result = siteBeheerder.getSites();

        assertEquals(lijst, result);
        verify(siteRepo, times(1)).findAll();
    }

    /* ============================================================
       insertSite()
       ============================================================ */

    private static Stream<Site> geldigeSites() throws SiteException {
        return Stream.of(
                maakSite("gent", "gent", 5, Status.ACTIEF, ProductieStatus.GEZOND),
                maakSite("antwerpen", "antwerpen", 6, Status.ACTIEF, ProductieStatus.GEZOND)
        );
    }

    @ParameterizedTest
    @MethodSource("geldigeSites")
    @DisplayName("insertSite start en commit transactie")
    void insertSite_StartsAndCommitsTransaction(Site site) {

        when(siteRepo.findAll()).thenReturn(List.of());

        siteBeheerder.insertSite(site);

        verify(siteRepo).startTransaction();
        verify(siteRepo).insert(site);
        verify(siteRepo).commitTransaction();
    }

    @Test
    @DisplayName("insertSite gooit exception bij bestaande naam")
    void insertSite_ThrowsExceptionOnDuplicateName() throws SiteException {

        Site site = maakSite("gent", "gent", 5, Status.ACTIEF, ProductieStatus.GEZOND);
        Site bestaandeSite = maakSite("gent", "gent", 5, Status.ACTIEF, ProductieStatus.GEZOND);

        when(siteRepo.findAll()).thenReturn(List.of(bestaandeSite));

        assertThrows(IllegalArgumentException.class,
                () -> siteBeheerder.insertSite(site));

        verify(siteRepo).findAll();

        verify(siteRepo, never()).update(any(Site.class));
    }

    @Test
    @DisplayName("insertSite rollback bij exception")
    void insertSite_RollbackBijFout() throws SiteException {

        Site site = maakSite("NieuweSite", "Gent", 5, Status.ACTIEF, ProductieStatus.GEZOND);

        when(siteRepo.findAll()).thenReturn(List.of());

        doThrow(new RuntimeException())
                .when(siteRepo)
                .insert(site);

        assertThrows(IllegalArgumentException.class,
                () -> siteBeheerder.insertSite(site));

        verify(siteRepo).rollbackTransaction();
    }

    /* ============================================================
       updateSite()
       ============================================================ */

    @ParameterizedTest
    @MethodSource("geldigeSites")
    @DisplayName("updateSite start en commit transactie")
    void updateSite_StartsAndCommitsTransaction(Site site) {

       when(siteRepo.findAll()).thenReturn(List.of());

        siteBeheerder.updateSite(site);

        verify(siteRepo).startTransaction();
        verify(siteRepo).update(site);
        verify(siteRepo).commitTransaction();
    }

    @Test
    @DisplayName("updateSite gooit exception bij bestaande naam")
    void updateSite_ThrowsExceptionOnDuplicateName() throws SiteException {

        Site site = maakSite("NieuweSite", "Gent", 5, Status.ACTIEF, ProductieStatus.GEZOND);
        site.setId(1L);

        Site andereSite = maakSite("NieuweSite", "Gent", 5, Status.ACTIEF, ProductieStatus.GEZOND);
        andereSite.setId(2L);

        when(siteRepo.findAll()).thenReturn(List.of(andereSite));

        assertThrows(IllegalArgumentException.class,
                () -> siteBeheerder.updateSite(site));

        verify(siteRepo).findAll();

        verify(siteRepo, never()).update(any(Site.class));
    }

    @Test
    @DisplayName("updateSite rollback bij exception")
    void updateSite_RollbackBijFout() throws SiteException {

        Site site = maakSite("NieuweSite", "Gent", 5, Status.ACTIEF, ProductieStatus.GEZOND);
        site.setId(1L);

        when(siteRepo.findAll()).thenReturn(List.of());

        doThrow(new RuntimeException())
                .when(siteRepo)
                .update(site);

        assertThrows(IllegalArgumentException.class,
                () -> siteBeheerder.updateSite(site));

        verify(siteRepo).rollbackTransaction();
    }

    /* ============================================================
       deleteSite()
       ============================================================ */

    @Test
    @DisplayName("deleteSite start en commit transactie")
    void deleteSite_StartsAndCommitsTransaction() throws SiteException {

        Site site = maakSite("DeleteSite", "Gent", 5, Status.ACTIEF, ProductieStatus.GEZOND);
        site.setId(1L);

        siteBeheerder.deleteSite(site);

        verify(siteRepo).startTransaction();
        verify(siteRepo).delete(site);
        verify(siteRepo).commitTransaction();
    }

    @Test
    @DisplayName("deleteSite rollback bij exception")
    void deleteSite_RollbackBijFout() throws SiteException {

        Site site = maakSite("DeleteSite", "Gent", 5, Status.ACTIEF, ProductieStatus.GEZOND);
        site.setId(1L);

        doThrow(new RuntimeException())
                .when(siteRepo)
                .delete(site);

        assertThrows(IllegalArgumentException.class,
                () -> siteBeheerder.deleteSite(site));

        verify(siteRepo).rollbackTransaction();
    }

    /* ============================================================
       closePersistency()
       ============================================================ */

    @Test
    @DisplayName("closePersistency delegeert naar repo")
    void closePersistency_DelegatesToRepo() {

        siteBeheerder.closePersistency();

        verify(siteRepo).closePersistency();
    }

    /* ============================================================
       Constructor test
       ============================================================ */

    @Test
    @DisplayName("Constructor initialiseert correct")
    void constructor_InitialiseertCorrect() {

        SiteBeheerder beheerder = new SiteBeheerder(siteRepo);

        assertNotNull(beheerder);
        verifyNoInteractions(siteRepo);
    }
}