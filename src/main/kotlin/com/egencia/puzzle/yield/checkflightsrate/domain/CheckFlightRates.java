package com.egencia.puzzle.yield.checkflightsrate.domain;

import org.jetbrains.annotations.NotNull;

import java.time.ZonedDateTime;
import java.util.List;

import static java.util.Collections.emptyList;

public class CheckFlightRates {

    @NotNull
    public static List<SearchResult> lookup(@NotNull List<Rates> table,
                                            @NotNull Rates.AirportCode departure,
                                            @NotNull Rates.AirportCode destination,
                                            @NotNull ZonedDateTime dateTime) {

        //table will be the input of the method

        //Find all the flights matching the departure airport and departing after dateTime
        // 1°) First, find the direct flights from departure to destination

        // 2°) Then if you are not in a hurry, find all the non-direct flights
        // (flights taking off from the right airport, but not landing in the final destination)

        // 3°) Finally, With the union of the two results, find the cheapest prices for each fare
        // (economy, premium, business, first)

        return emptyList();
    }
}
