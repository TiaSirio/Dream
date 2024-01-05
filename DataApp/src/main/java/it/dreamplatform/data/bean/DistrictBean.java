package it.dreamplatform.data.bean;

import com.esri.core.geometry.Polygon;

import java.io.Serializable;

/**
 * This class represents a District. It is not mapped from an entity. It contains the name of the district and the
 * polygon associated to the district (the polygon defines the structure of the district and is obtained through the
 * GeoJson file related to the name of the district).
 */
public class DistrictBean implements Serializable {
    private String name;
    private Polygon polygon;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Polygon getPolygon() {
        return polygon;
    }

    public void setPolygon(Polygon polygon) {
        this.polygon = polygon;
    }
}
