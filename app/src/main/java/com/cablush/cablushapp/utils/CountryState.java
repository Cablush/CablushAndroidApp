package com.cablush.cablushapp.utils;

import com.cablush.cablushapp.model.geonames.dto.Geonames;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by oscar on 07/04/16.
 */
public class CountryState implements Comparable<CountryState>{

    private Geonames geonames;

    private CountryState(Geonames geonames) {
        this.geonames = geonames;
    }

    private CountryState(String state) {
        this.geonames = new Geonames();
        this.geonames.setAdminCode1(state);
    }

    public static List<CountryState> fromSubDivision(List<Geonames> firstSubdivisions) {
        List<CountryState> countryStates = new ArrayList<>();
        for (Geonames geonames : firstSubdivisions) {
            countryStates.add(new CountryState(geonames));
        }
        Collections.sort(countryStates);
        return countryStates;
    }

    public static CountryState getContryState(String state) {
        return new CountryState(state);
    }

    public String getCode() {
        if (ValidateUtils.isNotEmpty(geonames.getAlternateNames())) {
            for (Geonames.AlternateName alternateName : geonames.getAlternateNames()) {
                if (alternateName.getLang().equalsIgnoreCase("abbr")) {
                    return alternateName.getName();
                }
            }
        }
        return geonames.getAdminCode1();
    }

    public String getName() {
        return ValidateUtils.isNotBlank(geonames.getAdminName1()) ? geonames.getAdminName1() : geonames.getName();
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CountryState that = (CountryState) o;

        return geonames != null ? this.getCode().equals(that.getCode()) : that.geonames == null;
    }

    @Override
    public int hashCode() {
        return geonames != null ? getCode().hashCode() : 0;
    }

    @Override
    public int compareTo(CountryState another) {
        return this.getName().compareTo(another.getName());
    }
}
