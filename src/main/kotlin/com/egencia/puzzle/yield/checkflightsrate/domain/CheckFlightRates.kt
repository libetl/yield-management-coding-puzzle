package com.egencia.puzzle.yield.checkflightsrate.domain

import com.egencia.puzzle.yield.checkflightsrate.domain.Rates.AirportCode
import java.time.ZonedDateTime

//set this value to 'true' if you prefer to code in java
/**
 * then :
 * @see com.egencia.puzzle.yield.checkflightsrate.domain.CheckFlightRates
 */
val I_PREFER_JAVA = false

fun List<Rates>.lookup(departure: AirportCode, destination: AirportCode, dateTime: ZonedDateTime) : List<SearchResult> {

    //remove this if statement (and the constant above) if you prefer to code in kotlin
    if (I_PREFER_JAVA){
        return CheckFlightRates.lookup(this, departure, destination, dateTime)
    }

    //In the kotlin version, the input is 'this' (a list of Rates)

    //Find all the flights matching the departure airport and departing after dateTime
    // 1°) First, find the direct flights from departure to destination

    // 2°) Then if you are not in a hurry, find all the non-direct flights
    // (flights taking off from the right airport, but not landing in the final destination)

    // 3°) Finally, With the union of the two results, find the cheapest prices for each fare
    // (economy, premium, business, first)

    return emptyList()
}
