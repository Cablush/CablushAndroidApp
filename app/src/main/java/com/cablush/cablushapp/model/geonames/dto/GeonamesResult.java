package com.cablush.cablushapp.model.geonames.dto;

import java.util.List;

/**
 * Created by oscar on 05/04/16.
 */
public class GeonamesResult {

    private Integer totalResultsCount;

    private List<Geonames> geonames;

    private GeonamesStatus status;

    public boolean success() {
        return geonames != null && status == null;
    }

    public Integer getTotalResultsCount() {
        return totalResultsCount;
    }

    public void setTotalResultsCount(Integer totalResultsCount) {
        this.totalResultsCount = totalResultsCount;
    }

    public List<Geonames> getGeonames() {
        return geonames;
    }

    public void setGeonames(List<Geonames> geonames) {
        this.geonames = geonames;
    }

    public GeonamesStatus getStatus() {
        return status;
    }

    public void setStatus(GeonamesStatus status) {
        this.status = status;
    }
}
