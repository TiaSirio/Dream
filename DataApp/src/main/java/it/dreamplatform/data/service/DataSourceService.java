package it.dreamplatform.data.service;

import it.dreamplatform.data.entity.DataSource;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * This class creates and call the queries to retrieve DataSource entities from the DB.
 */
@Stateless
public class DataSourceService {
    @PersistenceContext(unitName = "data-persistence-provider")
    private EntityManager em;

    public DataSourceService() {}

    public DataSourceService(EntityManager em) {
        this.em = em;
    }

    /**
     * This function query the DB to retrieve the List of all available DataSources.
     * @return a List of all DataSources.
     */
    public List<DataSource> getDataSources(){
        TypedQuery<DataSource> query = em.createQuery("SELECT d FROM DataSource d", DataSource.class);
        return query.getResultList();
    }
}
