package com.example._026javag03.domein;

import com.example._026javag03.exceptions.TeamException;
import com.example._026javag03.util.Rol;
import com.example._026javag03.util.TeamAttributes;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter
@Getter
@Entity
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private int id;

    private String code;
    @ManyToOne
    private Site site;
    @ManyToOne
    private Gebruiker verantwoordelijke;
    @OneToMany
    private List<Gebruiker> medewerkers;

    private Team (TeamBuilder builder){
        this.verantwoordelijke = builder.verantwoordelijke;
        this.site = builder.site;
    }

    public static TeamBuilder teamBuilder(){
        return new TeamBuilder();
    }


    private static class TeamBuilder {
        private Site site;
        private Gebruiker verantwoordelijke;

        public TeamBuilder buildSite(Site site){
            this.site = site;
            return this;
        }

        public TeamBuilder buildVerantwoordelijke(Gebruiker verantwoordelijke){
            this.verantwoordelijke = verantwoordelijke;
            return this;
        }

        private Set<TeamAttributes> required = new HashSet<>();
        public TeamBuilder build() throws TeamException {
            if(site == null){
                required.add(TeamAttributes.SITE);
            }

            if(verantwoordelijke == null || verantwoordelijke.getRol() != Rol.VERANTWOORDELIJKE){
                required.add(TeamAttributes.VERANTWOORDELIJKE);
            }

            if(!required.isEmpty()){
                throw new TeamException(required);
            }

            return this;
        }

    }

    @Override
    public String toString() {
        return "%s %s, %s, %s"
                .formatted(code,site,verantwoordelijke,medewerkers);
    }
}
