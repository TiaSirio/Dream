package it.dreamplatform.data.utils;

import com.esri.core.geometry.*;
import com.google.gson.JsonObject;
import it.dreamplatform.data.bean.DataSetBean;
import it.dreamplatform.data.bean.RankingBean;
import it.dreamplatform.data.bean.DataBean;
import it.dreamplatform.data.bean.DistrictBean;
import it.dreamplatform.data.entity.Data;
import it.dreamplatform.data.mapper.DataMapper;

import javax.inject.Inject;
import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.sql.Timestamp;
import java.util.*;

/**
 * This class is an util class in which are present functions used to manage the GeoJson format and to also other
 * functions to do spatial calculations.
 */
public class GeoUtil {

    @Inject
    private DataMapper dataMapper;

    @Inject
    public GeoUtil(DataMapper dataMapper){
        this.dataMapper = dataMapper;
    }

    /**
     * In this function we read the corresponding GeoJson file of a given district, we create the district polygon and
     * we give to it its corresponding district name.
     * @param districtId is the id of the selected district.
     * @return a DistrictBean with all the information of the district.
     * @throws Exception when the districtId is not valid.
     */
    public DistrictBean createSingleDistrict(String districtId) throws Exception {
        String district = getCorrectDistrict(districtId);
        if (district.equals("")){
            throw new Exception("Invalid district inserted");
        }
        File file = getFileFromResource(district);
        List<String> lines = new ArrayList<>();
        try {
            lines = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder();
        for(String line: lines){
            stringBuilder.append(line);
        }
        DistrictBean districtBean = new DistrictBean();

        Polygon polygon = (Polygon) OperatorImportFromGeoJson.local().execute(GeoJsonImportFlags.geoJsonImportDefaults, Geometry.Type.Polygon, stringBuilder.toString(), null).getGeometry();
        districtBean.setPolygon(polygon);

        JsonObject element = com.google.gson.JsonParser.parseString(stringBuilder.toString()).getAsJsonObject();
        JsonObject properties = element.get("properties").getAsJsonObject();
        districtBean.setName(properties.get("Dist_Name").getAsString());

        return districtBean;
    }

    /*public List<DistrictBean> createDistrict() {
        //Initially read all files.
        //Created to not cause errors.
        List<String> geoJsons = new ArrayList<>();
        List<DistrictBean> districts = new ArrayList<>();
        for (String geoJson : geoJsons) {
            DistrictBean districtBean = new DistrictBean();

            Polygon polygon = (Polygon) OperatorImportFromGeoJson.local().execute(GeoJsonImportFlags.geoJsonImportDefaults, Geometry.Type.Polygon, geoJson, null).getGeometry();
            districtBean.setPolygon(polygon);

            JsonObject element = com.google.gson.JsonParser.parseString(geoJson).getAsJsonObject();
            JsonObject properties = element.get("properties").getAsJsonObject();
            districtBean.setName(properties.get("Dist_Name").getAsString());

            districts.add(districtBean);
        }
        return districts;
    }*/

    /**
     * This function is needed to retrieve the name of the GeoJson file given the districtId.
     * @param districtId is the id of the searched district.
     * @return the name of the file of the GeoJson district.
     */
    private String getCorrectDistrict(String districtId) {
        switch (districtId) {
            case "19_1":
                return "Adilabad.json";
            case "22_2":
                return "BhadradriKothagudem.json";
            case "21_1":
                return "Hyderabad.json";
            case "16_1":
                return "Jagtial.json";
            case "20_2":
                return "Jangoan.json";
            case "21_3":
                return "JayashankarBhupalpally.json";
            case "21_4":
                return "JogulambaGadwal.json";
            case "14_2":
                return "Kamareddy.json";
            case "18_2":
                return "Karimnagar.json";
            case "20_1":
                return "Khammam.json";
            case "22_1":
                return "KumurambheemAsifabad.json";
            case "19_4":
                return "Mahabubabad.json";
            case "21_5":
                return "Mahabubnagar.json";
            case "14_1":
                return "Mancherial.json";
            case "19_3":
                return "Medak.json";
            case "17_1":
                return "MedchalMalkajgiri.json";
            case "15_2":
                return "Mulugu.json";
            case "21_6":
                return "Nagarkurnool.json";
            case "14_3":
                return "Nalgonda.json";
            case "23_1":
                return "Narayanpet.json";
            case "14_5":
                return "Nirmal.json";
            case "19_2":
                return "Nizamabad.json";
            case "18_1":
                return "Peddapalli.json";
            case "20_4":
                return "RajannaSircilla.json";
            case "20_3":
                return "Rangareddy.json";
            case "15_1":
                return "Sangareddy.json";
            case "17_2":
                return "Siddipet.json";
            case "17_3":
                return "Suryapet.json";
            case "23_2":
                return "Vikarabad.json";
            case "15_3":
                return "Wanaparthy.json";
            case "14_4":
                return "WarangalRural.json";
            case "21_2":
                return "WarangalUrban.json";
            case "23_3":
                return "YadadriBhuvanagiri.json";
            default:
                return "";
        }
    }

    /**
     * This function opens the file of the GeoJson of a district.
     * @param fileName is the name of the file.
     * @return the GeoJson file.
     * @throws URISyntaxException when the name of the file is not present.
     */
    private File getFileFromResource(String fileName) throws URISyntaxException {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        if (resource == null) {
            throw new IllegalArgumentException("file not found! " + fileName);
        } else {
            return new File(resource.toURI());
        }
    }

    /*public List<DataBean> valueDistricts(List<Data> dataList, List<DistrictBean> districts) {
        List<DataBean> toReturn = new ArrayList<>();
        SpatialReference spatialReference = SpatialReference.create(1984);
        for (Data data: dataList) {
            Point dataPoint = new Point(data.getLongitude(), data.getLatitude());
            for (DistrictBean district : districts) {
                boolean contains = OperatorContains.local().execute(district.getPolygon(), dataPoint, spatialReference,null);
                if(contains){
                    data.setDistrict(district.getName());
                    DataBean dataBean = dataMapper.mapEntityToBean(data);
                    toReturn.add(dataBean);
                    break;
                }
            }
        }
        return toReturn;
    }*/

    /**
     * This function check for each point of the dataList, if a point belongs to the given district.
     * @param dataList is the List of Data from which the points will be retrieved.
     * @param district is the Bean of the district.
     * @return a list of DataBean (that contains more information related to the position according to the district
     * (if it's in the district the name of the district is set in the DataBean)).
     */
    public List<DataBean> valueSingleDistrict(List<Data> dataList, DistrictBean district) {
        List<DataBean> toReturn = new ArrayList<>();
        SpatialReference spatialReference = SpatialReference.create(1984);
        for (Data data: dataList) {
            Point dataPoint = new Point(data.getLongitude(), data.getLatitude());
            boolean contains = OperatorContains.local().execute(district.getPolygon(), dataPoint, spatialReference,null);
            if(contains){
                data.setDistrict(district.getName());
                DataBean dataBean = dataMapper.mapEntityToBean(data);
                toReturn.add(dataBean);
            }
        }
        return toReturn;
    }

    /**
     * This function dynamically creates a medium according to all the values of a given DataSet. This medium point will
     * be used to divide the district into four zones, in order to apply a ranking algorithm for every district.
     * @param dataBeanList is the List of DataBean.
     * @return the medium point.
     */
    public Point mediumPoint (List<DataBean> dataBeanList) {
        double mediumLatitude = 0;
        double mediumLongitude = 0;
        int counter = dataBeanList.size();
        for (DataBean data: dataBeanList) {
            mediumLatitude += data.getLatitude();
            mediumLongitude += data.getLongitude();
        }
        mediumLatitude = mediumLatitude / counter;
        mediumLongitude = mediumLongitude / counter;

        return new Point(mediumLongitude, mediumLatitude);
    }

    /**
     * This function maps very value in its corresponding zone (relying on the position of the medium point).
     * @param dataBeanList is the List of DataBean.
     * @param mediumPoint is the medium point.
     * @return a mapped List of DataBean.
     */
    public List<DataBean> valueZone (List<DataBean> dataBeanList, Point mediumPoint) {
        for (DataBean dataBean:dataBeanList) {
            if(dataBean.getLongitude() > mediumPoint.getX()){
                if(dataBean.getLatitude() > mediumPoint.getY()){
                    dataBean.setZone(ZoneEnum.NORTH_EAST);
                } else {
                    dataBean.setZone(ZoneEnum.SOUTH_EAST);
                }
            } else {
                if(dataBean.getLatitude() > mediumPoint.getY()){
                    dataBean.setZone(ZoneEnum.NORTH_WEST);
                } else {
                    dataBean.setZone(ZoneEnum.SOUTH_WEST);
                }
            }
        }
        return dataBeanList;
    }

    /**
     * This function calculate the value that each zone has. Then this value will be used to calculate the
     * ranking. Each value is calculate by doing an arithmetic mean.
     * @param dataBeanList is the mapped List of DataBean.
     * @return a DataSetBean containing the value to be used to calculate the ranking.
     */
    public DataSetBean calculateZoneValue(List<DataBean> dataBeanList) {
        DataSetBean dataSetBean = new DataSetBean();

        double valueNE = 0;
        double valueNW = 0;
        double valueSE = 0;
        double valueSW = 0;

        int counterNE = 0;
        int counterNW = 0;
        int counterSE = 0;
        int counterSW = 0;


        for (DataBean dataBean:dataBeanList) {
            if(dataBean.getZone() == ZoneEnum.NORTH_EAST){
                valueNE += dataBean.getValue();
                counterNE ++;
            }
            else if(dataBean.getZone() == ZoneEnum.NORTH_WEST){
                valueNW += dataBean.getValue();
                counterNW ++;
            }
            else if(dataBean.getZone() == ZoneEnum.SOUTH_EAST){
                valueSE += dataBean.getValue();
                counterSE ++;
            }
            else if(dataBean.getZone() == ZoneEnum.SOUTH_WEST){
                valueSW += dataBean.getValue();
                counterSW ++;
            }
        }
        valueNE = valueNE / counterNE;
        valueNW = valueNW / counterNW;
        valueSE = valueSE / counterSE;
        valueSW = valueSW / counterSW;

        dataSetBean.setDataSourceId(dataBeanList.get(0).getDataSourceId());
        dataSetBean.setDistrict(dataBeanList.get(0).getDistrict());
        dataSetBean.setValueNE(valueNE);
        dataSetBean.setValueNW(valueNW);
        dataSetBean.setValueSE(valueSE);
        dataSetBean.setValueSW(valueSW);

        return dataSetBean;
    }

    /**
     * This function calculate the ranking of a given district. As scoring function for our ranking is being adopted the
     * sum of the elements of all the DataSetBean.
     * @param dataSetBeans is a List of DataSetBean that contains the values used to calculate the ranking.
     * @param district is the name of the district.
     * @return a RankingBean that contains the result of the ranking function (considering that higher
     * values are better).
     * @throws Exception when there is a value that doesn't belong to the selected district.
     */
    public List<RankingBean> calculateRanking(List<DataSetBean> dataSetBeans, String district) throws Exception {
        double rankNE = 0;
        double rankNW = 0;
        double rankSE = 0;
        double rankSW = 0;

        RankingBean zoneNE = new RankingBean();
        RankingBean zoneNW = new RankingBean();
        RankingBean zoneSE = new RankingBean();
        RankingBean zoneSW = new RankingBean();

        zoneNE.setDistrict(district);
        zoneNW.setDistrict(district);
        zoneSE.setDistrict(district);
        zoneSW.setDistrict(district);

        zoneNE.setZone(ZoneEnum.NORTH_EAST);
        zoneNW.setZone(ZoneEnum.NORTH_WEST);
        zoneSE.setZone(ZoneEnum.SOUTH_EAST);
        zoneSW.setZone(ZoneEnum.SOUTH_WEST);

        //Here the ranking function is done
        for (DataSetBean dataSet : dataSetBeans) {
            if(!dataSet.getDistrict().equals(district)){
                throw new Exception("DataSet must belong to district " + district + "!\n");
            }
            rankNE += dataSet.getValueNE();
            rankNW += dataSet.getValueNW();
            rankSE += dataSet.getValueSE();
            rankSW += dataSet.getValueSW();
        }
        zoneNE.setValue(rankNE);
        zoneNW.setValue(rankNW);
        zoneSE.setValue(rankSE);
        zoneSW.setValue(rankSW);

        List<RankingBean> rankingList = new ArrayList<>();
        rankingList.add(zoneNE);
        rankingList.add(zoneNW);
        rankingList.add(zoneSE);
        rankingList.add(zoneSW);

        Comparator<RankingBean> compareByValue = Comparator.comparingDouble(RankingBean::getValue).reversed();
        rankingList.sort(compareByValue);
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        for (int i = 0; i<rankingList.size(); i++) {
            rankingList.get(i).setTimestamp(timestamp);
            rankingList.get(i).setPosition(i+1);
        }
        return rankingList;
    }
}
