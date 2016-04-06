package com.cablush.cablushapp.utils;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by oscar on 04/04/16.
 */
public class CountryLocale {

    private Locale locale;

    private CountryLocale(Locale locale) {
        this.locale = locale;
    }

    private CountryLocale(String language, String country) {
        locale = new Locale(language, country);
    }

    /**
     * Get the default CountryLocale.
     */
    public static CountryLocale getDefault() {
        return new CountryLocale(Locale.getDefault());
    }

    /**
     * Get the CountryLocale of the Country code
     */
    public static CountryLocale getCountryLocale(String country) {
        return new CountryLocale("", country);
    }

    /**
     * Get all the CountryLocales
     */
    public static List<CountryLocale> getContriesLocales() {
        List<CountryLocale> countriesLocales = new ArrayList<>();
        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            countriesLocales.add(CountryLocale.getCountryLocale(countryCode));
        }
        return countriesLocales;
    }

    /**
     * Returns the country code.
     */
    public String getCountry() {
        return locale.getCountry();
    }

    /**
     * Returns the country name
     */
    public String getDisplayCountry() {
        return locale.getDisplayCountry();
    }

    @Override
    public String toString() {
        return locale.getDisplayCountry();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        CountryLocale that = (CountryLocale) o;

        return locale != null ? locale.getCountry().equals(that.locale.getCountry()) : that.locale == null;

    }

    @Override
    public int hashCode() {
        return locale != null ? locale.getCountry().hashCode() : 0;
    }
}
