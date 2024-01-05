package it.dreamplatform.data.utils;

/**
 * This enum is used to create a ranking inside a District. To achieve this result, during the computation of the data,
 * is dynamically created a point according to all the values of the dataset used at that moment. From the point
 * created, we can discover in what zone the point wee are looking for belong, only by looking at the just created
 * dynamical middle point.The different zones are: NORTH_WEST (0), NORTH_EAST (1), SOUTH_EAST (2) and SOUTH_WEST (3).
 */
public enum ZoneEnum {
    NORTH_WEST, NORTH_EAST, SOUTH_EAST, SOUTH_WEST
}
