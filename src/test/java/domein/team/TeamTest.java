package domein.team;

import com.example._026javag03.domein.Gebruiker;
import com.example._026javag03.domein.Site;
import com.example._026javag03.domein.Team;
import com.example._026javag03.exceptions.AdresException;
import com.example._026javag03.exceptions.SiteException;
import com.example._026javag03.exceptions.TeamException;
import com.example._026javag03.util.site.ProductieStatus;
import com.example._026javag03.util.gebruiker.Rol;
import com.example._026javag03.util.Status;
import com.example._026javag03.util.team.TeamAttributes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.time.LocalDate;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class TeamTest {

    private static Gebruiker verantwoordelijke;
    private static Gebruiker manager;
    private static Site site;

    @BeforeEach
    public void maakGebruikers() throws AdresException, SiteException {
        verantwoordelijke  = new Gebruiker.Builder("Verdonck"
                ,"Johannes"
                ,LocalDate.of(1990,1,1),
                "Kerkstraat",
                "10",
                null,
                1000,
                "Brussel",
                "Johannes.Verdonck@gmail.com",
                Rol.VERANTWOORDELIJKE,
                Status.ACTIEF).build();

        manager  = new Gebruiker.Builder("Verdonck"
                ,"Johannes"
                ,LocalDate.of(1990,1,1),
                "Kerkstraat",
                "10",
                null,
                1000,
                "Brussel",
                "Johannes.Verdonck@student.hogent.be",
                Rol.MANAGER,
                Status.ACTIEF).build();

        site = new Site.SiteBuilder()
                .buildNaam("Hoofdkantoor")
                .buildLocatie("Brussel")
                .buildCapaciteit(500)
                .buildOperationeleStatus(Status.ACTIEF)
                .buildProductieStatus(ProductieStatus.GEZOND).buildSite();
    }


    private static Stream<Arguments> ongeldigeTeams() {
        return Stream.of(
                Arguments.of(null,verantwoordelijke,TeamAttributes.SITE),
                Arguments.of(site,manager,TeamAttributes.VERANTWOORDELIJKE),
                Arguments.of(site,null,TeamAttributes.VERANTWOORDELIJKE)
        );
    }

    @Test
    void maakTeam_GeldigeParameters_MaaktTeam() throws TeamException {
         Team team = Team.teamBuilder()
                 .buildSite(site)
                 .buildVerantwoordelijke(verantwoordelijke)
                 .build();
         assertNotNull(team);
         assertEquals(site,team.getSite());
         assertEquals(verantwoordelijke,team.getVerantwoordelijke());
    }

    @ParameterizedTest
    @MethodSource("ongeldigeTeams")
    void maakTeam_OnGeldigeParameters_GooitException(Site site, Gebruiker verantwoordelijke, TeamAttributes expected) throws TeamException {
        TeamException ex = assertThrows(TeamException.class, () ->
                Team.teamBuilder()
                        .buildSite(site)
                        .buildVerantwoordelijke(verantwoordelijke)
                        .build());
        assertTrue(ex.getRequired().containsKey(expected));
    }
}
