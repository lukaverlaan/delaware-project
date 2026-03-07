package com.example._026javag03.domein.beheerder;

import com.example._026javag03.domein.Site;
import com.example._026javag03.repository.site.SiteDao;

import java.util.List;

public class SiteBeheerder {

    private final SiteDao siteRepo;

    public SiteBeheerder(SiteDao siteDao) {
        this.siteRepo = siteDao;
    }

    public List<Site> getSites() {
        return siteRepo.findAll();
    }

    public void insertSite(Site site) {

        boolean bestaat = siteRepo.findAll()
                .stream()
                .anyMatch(s -> s.getNaam().equalsIgnoreCase(site.getNaam()));

        if (bestaat) {
            throw new IllegalArgumentException("Er bestaat al een site met deze naam.");
        }

        try {

            siteRepo.startTransaction();

            siteRepo.insert(site);

            siteRepo.commitTransaction();

        } catch (Exception e) {

            siteRepo.rollbackTransaction();
            throw new IllegalArgumentException("Site kon niet opgeslagen worden.");
        }
    }

    public void updateSite(Site site) {

        boolean bestaat = siteRepo.findAll()
                .stream()
                .anyMatch(s ->
                        !s.getId().equals(site.getId()) &&
                                s.getNaam().equalsIgnoreCase(site.getNaam())
                );

        if (bestaat) {
            throw new IllegalArgumentException("Er bestaat al een site met deze naam.");
        }

        try {

            siteRepo.startTransaction();

            siteRepo.update(site);

            siteRepo.commitTransaction();

        } catch (Exception e) {

            siteRepo.rollbackTransaction();
            throw new IllegalArgumentException("Site kon niet geüpdatet worden.");
        }
    }

    public void deleteSite(Site site) {

        try {

            siteRepo.startTransaction();

            siteRepo.delete(site);

            siteRepo.commitTransaction();

        } catch (Exception e) {

            siteRepo.rollbackTransaction();
            throw new IllegalArgumentException("Site kon niet verwijderd worden.");
        }
    }

    public void closePersistency() {
        siteRepo.closePersistency();
    }
}