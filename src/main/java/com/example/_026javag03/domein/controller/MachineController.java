package com.example._026javag03.domein.controller;

import com.example._026javag03.domein.Gebruiker;
import com.example._026javag03.domein.Machine;
import com.example._026javag03.domein.Site;
import com.example._026javag03.domein.beheerder.MachineBeheerder;
import com.example._026javag03.dto.MachineDTO;
import com.example._026javag03.exceptions.MachineException;
import com.example._026javag03.repository.gebruiker.GebruikerDao;
import com.example._026javag03.repository.gebruiker.GebruikerDaoJpa;
import com.example._026javag03.repository.machine.MachineDaoJpa;
import com.example._026javag03.repository.site.SiteDao;
import com.example._026javag03.repository.site.SiteDaoJpa;
import com.example._026javag03.util.machine.ProductieStatusMachine;
import com.example._026javag03.util.machine.StatusMachine;

import java.sql.Time;
import java.util.List;
import java.util.stream.Collectors;

public class MachineController {

    private final MachineBeheerder machineBeheerder;
    private final GebruikerDao gebruikerDao;
    private final SiteDao siteDao;

    public MachineController() {
        machineBeheerder = new MachineBeheerder(new MachineDaoJpa());
        siteDao = new SiteDaoJpa();
        gebruikerDao = new GebruikerDaoJpa();
    }

    public void voegMachineToe(
            Long siteId,
            String locatie,
            String productInfo,
            String status,
            String productieStatus,
            Object uptime,
            List<Long> werknemerIds
    ) throws MachineException {

        Site site = siteDao.get(siteId);

        List<Gebruiker> werknemers = werknemerIds.stream()
                .map(id -> gebruikerDao.get(id))
                .collect(Collectors.toList());

        Machine machine = Machine.machineBuilder()
                .buildSite(site)
                .buildLocatie(locatie)
                .buildProductInfo(productInfo)
                .buildStatus(StatusMachine.valueOf(status))
                .buildProductieStatus(ProductieStatusMachine.valueOf(productieStatus))
                .buildWerknemers(werknemers)
                .build();

        machineBeheerder.insertMachine(machine);
    }

    public List<MachineDTO> getMachines() {
        return machineBeheerder.getMachines()
                .stream()
                .map(machine -> new MachineDTO(
                        machine.getId(),
                        machine.getCode(),
                        machine.getSite().getId(),
                        machine.getLocatie(),
                        machine.getProductinfo(),
                        machine.getStatus(),
                        machine.getProductieStatus(),
                        machine.getUptime() != null ? machine.getUptime().toString() : null,
                        machine.getLaatsteOnderhoud(),
                        machine.getWerknemers()
                                .stream()
                                .map(g -> g.getId())
                                .toList()
                ))
                .collect(Collectors.toList());
    }

    public void updateMachine(MachineDTO dto) throws MachineException {

        Machine machine = new MachineDaoJpa().get(dto.id());

        Site site = siteDao.get(dto.siteId());

        List<Gebruiker> werknemers = dto.werknemerIds().stream()
                .map(id -> gebruikerDao.get(id))
                .toList();

        machine.setSite(site);
        machine.setLocatie(dto.locatie());
        machine.setProductinfo(dto.productinfo());
        machine.setStatus(dto.status());
        machine.setProductieStatus(dto.productieStatus());
        machine.setWerknemers(werknemers);

        if (dto.uptime() != null && !dto.uptime().isBlank()) {
            try {
                machine.setUptime(Time.valueOf(dto.uptime())); // verwacht HH:mm:ss
            } catch (IllegalArgumentException e) {
                throw new MachineException("Ongeldig uptime formaat (gebruik HH:mm:ss)");
            }
        } else {
            machine.setUptime(null);
        }

        machine.setLaatsteOnderhoud(dto.laatsteOnderhoud());

        machineBeheerder.updateMachine(machine);
    }

    public void verwijderMachine(Long machineId) {

        Machine machine = machineBeheerder.getMachines()
                .stream()
                .filter(m -> m.getId().equals(machineId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Machine niet gevonden"));

        machineBeheerder.deleteMachine(machine);
    }

    public void close() {
        machineBeheerder.closePersistency();
    }
}