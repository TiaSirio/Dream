package it.dreamplatform.data.bean;

import it.dreamplatform.data.utils.ZoneEnum;

import java.io.Serializable;
import java.sql.Timestamp;

/**
 * This class represents a DataBean. It is mapped from the Data entity. It contains the value retrieved associated to a
 * precise longitude and latitude, the district we are using and the ZoneEnum that defines in which part of the district
 * this data is, considering that the zone are divided according to a point taken dynamically in the middle of the
 * district.
 */
public class DataBean implements Serializable {
    private Long dataId;
    private double value;
    private double longitude;
    private double latitude;
    private Timestamp timestamp;
    private Long dataSourceId;
    private String district;
    private ZoneEnum zone;

    public Long getDataId() {
        return dataId;
    }

    public void setDataId(Long dataId) {
        this.dataId = dataId;
    }

    public double getValue() {
        return value;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public Long getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(Long dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public ZoneEnum getZone() {
        return zone;
    }

    public void setZone(ZoneEnum zone) {
        this.zone = zone;
    }
}
