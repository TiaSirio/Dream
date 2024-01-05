package it.dreamplatform.data.controller;

import com.esri.core.geometry.Point;
import it.dreamplatform.data.bean.DataBean;
import it.dreamplatform.data.bean.DataSetBean;
import it.dreamplatform.data.bean.DistrictBean;
import it.dreamplatform.data.bean.RankingBean;
import it.dreamplatform.data.entity.Data;
import it.dreamplatform.data.entity.DataSource;
import it.dreamplatform.data.service.DataService;
import it.dreamplatform.data.service.DataSourceService;
import it.dreamplatform.data.utils.GeoUtil;

import javax.inject.Inject;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * This class contains all the controller used by a Data entity or a DataBean of every genre.
 */
public class DataController {

    @Inject
    private GeoUtil geoUtil;

    @Inject
    private DataService dataService;

    @Inject
    private DataSourceService dataSourceService;

    @Inject
    public DataController (GeoUtil geoUtil, DataService dataService, DataSourceService dataSourceService) {
        this.geoUtil = geoUtil;
        this.dataService = dataService;
        this.dataSourceService = dataSourceService;
    }

    /**
     * This function is used to retrieve a DistrictBean.
     * @param districtId is the id of the selected District.
     * @return a Bean containing all the elements of a District.
     */
    public DistrictBean createSingleDistrict(String districtId) throws Exception {
        return geoUtil.createSingleDistrict(districtId);
    }

    /**
     * This function is used to retrieve a DataSetBean. Initially, by giving it a dataSource, it retrieves all the data
     * of the dataSet from the DB and then select only the interesting data (the ones inside the district of interest).
     * Then a dynamical medium point is found (according to all the points in the dataSet)
     * @param dataSourceId is the id of the dataSource from which the data will be retrieved.
     * @param districtOfInterest is the district of interest.
     * @return a DataSetBean containing all the information filtered by zone.
     */
    public DataSetBean createDataSet (Long dataSourceId, DistrictBean districtOfInterest) throws URISyntaxException {
        List<Data> dataList = dataService.getDataByDataSourceId(dataSourceId);
        //Create dataBean indicating at what district they belong to
        List<DataBean> dataBeans = geoUtil.valueSingleDistrict(dataList, districtOfInterest);
        //Filter keeping only the data of the searched district
        List<DataBean> filteredDataBeans = dataBeans.stream()
                                                    .filter(dataBean -> dataBean.getDistrict().equals(districtOfInterest.getName()))
                                                    .collect(Collectors.toList());
        //System.out.println(dataList.size());
        //System.out.println(dataBeans.size());
        //System.out.println(filteredDataBeans.size());
        //Calculate medium point of the district based on the achieved data
        Point mediumPoint = geoUtil.mediumPoint(filteredDataBeans);
        //Map the data in the 4 different possible area (using the ZoneEnum)
        filteredDataBeans = geoUtil.valueZone(filteredDataBeans, mediumPoint);
        //Calculate medium value for each zone of the district for the given dataSource
        return geoUtil.calculateZoneValue(filteredDataBeans);
    }

    /**
     * This function is used to retrieve a List of RankingBean. Initially, by giving it a districtId, retrieve the
     * information of the district and then all the dataSource available.
     * Then for each dataSource it retrieves the correct dataSet.
     * In the end a ranking is calculated according to all the dataSetBean involved.
     * @param districtId is the id of the searched district.
     * @return a List of RankingBean.
     */
    public List<RankingBean> createRanking (String districtId) throws Exception {
        List<DataSetBean> dataSetBeans = new ArrayList<>();
        List<DataSource> dataSources = dataSourceService.getDataSources();

        DistrictBean districtOfInterest = createSingleDistrict(districtId);
        for (DataSource dataSource: dataSources) {
            DataSetBean dataSetBean = createDataSet(dataSource.getDataSourceId(), districtOfInterest);
            dataSetBeans.add(dataSetBean);
        }
        return geoUtil.calculateRanking(dataSetBeans, districtOfInterest.getName());
    }

    /**
     * This function is used to retrieve a List of RankingBean. Initially, by giving it a districtId, retrieve the
     * information of the district. In the end a ranking is calculated according to all the dataSetBean involved.
     * @param districtId is the id of the searched district.
     * @return a List of RankingBean.
     */
    public List<RankingBean> createRankingForSelectedDataSets (String districtId, List<Long> dataSourcesIds) throws Exception {
        List<DataSetBean> dataSetBeans = new ArrayList<>();
        List<DataSource> dataSources = dataSourceService.getDataSources();
        for (int i = 0; i < dataSources.size(); i++){
            if (!dataSourcesIds.contains(dataSources.get(i).getDataSourceId())){
                dataSources.remove(i);
            }
        }
        if (dataSources.isEmpty()){
            RankingBean rankingBean = new RankingBean();
            rankingBean.setDistrict("No data");
            rankingBean.setValue(0);
            rankingBean.setZone(null);
            List<RankingBean> rankingList = new ArrayList<>();
            rankingList.add(rankingBean);
            return rankingList;
        }
        DistrictBean districtOfInterest = createSingleDistrict(districtId);
        for (DataSource dataSource: dataSources) {
            DataSetBean dataSetBean = createDataSet(dataSource.getDataSourceId(), districtOfInterest);
            dataSetBeans.add(dataSetBean);
        }
        return geoUtil.calculateRanking(dataSetBeans, districtOfInterest.getName());
    }

    /**
     * This function is used to retrieve the data of all the datasets contained in a given district according to its
     * districtId (creating also the polygon of the district).
     * @param districtId is the id of the searched district.
     * @return a List of List of DataBean (the first List represents the different datasets, the second List are all
     * the points in the i-th dataset).
     */
    public List<List<DataBean>> getDataOfDistrict (String districtId) throws Exception {
        List<DataBean> dataSetBeans = new ArrayList<>();
        List<DataSource> dataSources = dataSourceService.getDataSources();
        List<List<DataBean>> dataBeansOfDistrict = new ArrayList<>();

        DistrictBean districtOfInterest = createSingleDistrict(districtId);
        for (DataSource dataSource: dataSources) {
            dataSetBeans = retrieveDataOfDistrict(dataSource, districtOfInterest);
            dataBeansOfDistrict.add(dataSetBeans);
        }
        return dataBeansOfDistrict;
    }

    /**
     * This function is used to retrieve the data of all the datasets contained in a given district according to its
     * districtId.
     * @param dataSource is the i-th dataSource from which the data will be retrieved.
     * @param districtOfInterest is the Bean containing the polygon and other information of the district.
     * @return a List of DataBean containing all the data of the given dataSet.
     */
    public List<DataBean> retrieveDataOfDistrict (DataSource dataSource, DistrictBean districtOfInterest) {
        List<Data> dataList = dataService.getDataByDataSourceId(dataSource.getDataSourceId());
        List<DataBean> dataBeans = geoUtil.valueSingleDistrict(dataList, districtOfInterest);
        return dataBeans.stream()
                .filter(dataBean -> dataBean.getDistrict().equals(districtOfInterest.getName()))
                .collect(Collectors.toList());
    }

    /**
     * This function is used to retrieve all the data of the dataSet of a given dataSource.
     * @param dataSourceId is the id of the searched dataSource.
     * @return a List of Data of the given dataSource.
     */
    public List<Data> retrieveDataSetByDataSourceId (Long dataSourceId) {
        return dataService.getDataByDataSourceId(dataSourceId);
    }
}
