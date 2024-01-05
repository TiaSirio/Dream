package it.dreamplatform.data.integration;

import it.dreamplatform.data.EntityManagerProvider;
import it.dreamplatform.data.bean.DataBean;
import it.dreamplatform.data.bean.RankingBean;
import it.dreamplatform.data.controller.DataController;
import it.dreamplatform.data.entity.Data;
import it.dreamplatform.data.mapper.DataMapper;
import it.dreamplatform.data.service.DataService;
import it.dreamplatform.data.service.DataSourceService;
import it.dreamplatform.data.utils.GeoUtil;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class DataIntegrationTest {

    private DataService dataService;
    private DataSourceService dataSourceService;

    private DataController dataController;

    @Rule
    public EntityManagerProvider provider = EntityManagerProvider.withUnit("data-test");

    @Before
    public void setUp() {
        dataService = new DataService(this.provider.em());
        dataSourceService = new DataSourceService(this.provider.em());
        DataMapper dataMapper = new DataMapper();
        GeoUtil geoUtil = new GeoUtil(dataMapper);
        dataController = new DataController(geoUtil, dataService, dataSourceService);
    }

    @Test
    public void checkDataForNotExistingDistrictTest() throws Exception {
        assertThrows(Exception.class, () -> dataController.createRanking("19_123"));
    }

    @Test
    public void getRankingsForDifferentDistrictsTest() throws Exception {
        List<RankingBean> rankings = dataController.createRanking("19_1");
        assertEquals("Adilabad", rankings.get(0).getDistrict());
        assertEquals("Adilabad", rankings.get(1).getDistrict());
        assertEquals("Adilabad", rankings.get(2).getDistrict());
        assertEquals("Adilabad", rankings.get(3).getDistrict());
        assertEquals(4, rankings.size());
        rankings = new ArrayList<>();
        rankings = dataController.createRanking("22_2");
        assertEquals("Bhadradri Kothagudem", rankings.get(0).getDistrict());
        assertEquals("Bhadradri Kothagudem", rankings.get(1).getDistrict());
        assertEquals("Bhadradri Kothagudem", rankings.get(2).getDistrict());
        assertEquals("Bhadradri Kothagudem", rankings.get(3).getDistrict());
        assertEquals(4, rankings.size());
        rankings = new ArrayList<>();
        rankings = dataController.createRanking("21_1");
        assertEquals("Hyderabad", rankings.get(0).getDistrict());
        assertEquals("Hyderabad", rankings.get(1).getDistrict());
        assertEquals("Hyderabad", rankings.get(2).getDistrict());
        assertEquals("Hyderabad", rankings.get(3).getDistrict());
        assertEquals(4, rankings.size());
    }

    @Test
    public void getDataForAllDataSetsAccordingToSingleDistrictTest() throws Exception {
        List<List<DataBean>> dataOfDistrict = dataController.getDataOfDistrict("19_1");
        assertEquals(dataOfDistrict.size(), dataSourceService.getDataSources().size());
    }

    @Test
    public void getDataOfGivenDataSetTest() {
        List<Data> dataSet = dataController.retrieveDataSetByDataSourceId(1L);
        assertEquals(dataSet.size(), dataService.getDataByDataSourceId(1L).size());
    }

    @Test
    public void getRankingsForDifferentDistrictsForSelectedDataSourcesTest() throws Exception {
        List<Long> dataSourcesIds = new ArrayList<>();
        dataSourcesIds.add(1L);
        dataSourcesIds.add(2L);
        List<RankingBean> rankings = dataController.createRankingForSelectedDataSets("19_1", dataSourcesIds);
        assertEquals("Adilabad", rankings.get(0).getDistrict());
        assertEquals("Adilabad", rankings.get(1).getDistrict());
        assertEquals("Adilabad", rankings.get(2).getDistrict());
        assertEquals("Adilabad", rankings.get(3).getDistrict());
        assertEquals(4, rankings.size());
        rankings = new ArrayList<>();
        rankings = dataController.createRankingForSelectedDataSets("22_2", dataSourcesIds);
        assertEquals("Bhadradri Kothagudem", rankings.get(0).getDistrict());
        assertEquals("Bhadradri Kothagudem", rankings.get(1).getDistrict());
        assertEquals("Bhadradri Kothagudem", rankings.get(2).getDistrict());
        assertEquals("Bhadradri Kothagudem", rankings.get(3).getDistrict());
        assertEquals(4, rankings.size());
        rankings = new ArrayList<>();
        rankings = dataController.createRankingForSelectedDataSets("21_1", dataSourcesIds);
        assertEquals("Hyderabad", rankings.get(0).getDistrict());
        assertEquals("Hyderabad", rankings.get(1).getDistrict());
        assertEquals("Hyderabad", rankings.get(2).getDistrict());
        assertEquals("Hyderabad", rankings.get(3).getDistrict());
        assertEquals(4, rankings.size());
        rankings = dataController.createRankingForSelectedDataSets("21_1", new ArrayList<>());
        assertEquals("No data", rankings.get(0).getDistrict());
        assertEquals(0, rankings.get(0).getValue());
        assertNull(rankings.get(0).getZone());
        assertEquals(1, rankings.size());
    }
}
