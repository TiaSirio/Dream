package it.dreamplatform.data.service;

import it.dreamplatform.data.entity.Data;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

/**
 * This class creates and call the queries to retrieve Data entities from the DB.
 */
@Stateless
public class DataService {
    @PersistenceContext(unitName = "data-persistence-provider")
    private EntityManager em;

    public DataService() {}

    public DataService(EntityManager em) {
        this.em = em;
    }

    /**
     * This function query the DB to retrieve a List of Data of a given DataSource.
     * @param dataSourceId is the id of the DataSource.
     * @return a List of Data entities.
     */
    public List<Data> getDataByDataSourceId(Long dataSourceId){
        TypedQuery<Data> query = em.createQuery("SELECT d FROM Data d WHERE d.dataSourceId = :dataSourceId", Data.class);
        query.setParameter("dataSourceId", dataSourceId);
        return query.getResultList();
    }
}
