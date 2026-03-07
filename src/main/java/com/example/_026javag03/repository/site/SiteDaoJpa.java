package com.example._026javag03.repository.site;

import com.example._026javag03.domein.Site;
import com.example._026javag03.repository.GenericDaoJpa;

public class SiteDaoJpa extends GenericDaoJpa<Site> implements SiteDao {

    public SiteDaoJpa() {
        super(Site.class);
    }
}