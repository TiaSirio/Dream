package it.dreamplatform.data.mapper;

import it.dreamplatform.data.bean.DataBean;
import it.dreamplatform.data.entity.Data;

/**
 * This class do different mappings between a Data entity to its corresponding bean and vice versa.
 */
public class DataMapper {

    /**
     * This function creates a DataBean and call another function to maps the values.
     * @param entity is the entity of the DB.
     * @return the corresponding Bean just created.
     */
    public DataBean mapEntityToBean (Data entity){
        return mapEntityToBean(entity, new DataBean());
    }

    /**
     * This function sets the values of a DataBean using the values retrieve from a Data entity.
     * @param entity is the entity of the DB.
     * @param bean is the object into which the function will set all the values.
     * @return the corresponding DataBean or null if the entity is null.
     */
    public DataBean mapEntityToBean(Data entity, DataBean bean){
        if (entity == null) {return null;}
        bean.setDataId(entity.getDataId());
        bean.setValue(entity.getValue());
        bean.setLongitude(entity.getLongitude());
        bean.setLatitude(entity.getLatitude());
        bean.setTimestamp(entity.getTimestamp());
        bean.setDataSourceId(entity.getDataSourceId());
        bean.setDistrict(entity.getDistrict());
        return bean;
    }

    /**
     * This function creates a Data entity and call another function to map the values.
     * @param bean is the DataBean from which the value will be retrieved.
     * @return the corresponding Data entity just created.
     */
    public Data mapBeanToEntity (DataBean bean){
        return mapBeanToEntity(new Data(), bean);
    }

    /**
     * This function sets the values of a Data entity using the values retrieve from a DataBean.
     * @param entity is the entity of the DB.
     * @param bean is the object from which the values will be retrieved.
     * @return the corresponding Data or null if the Bean is null.
     */
    public Data mapBeanToEntity(Data entity, DataBean bean){
        if (bean == null) {return null;}
        entity.setDataId(bean.getDataId());
        entity.setValue(bean.getValue());
        entity.setLongitude(bean.getLongitude());
        entity.setLatitude(bean.getLatitude());
        entity.setTimestamp(bean.getTimestamp());
        entity.setDataSourceId(bean.getDataSourceId());
        entity.setDistrict(bean.getDistrict());
        return entity;
    }
}
