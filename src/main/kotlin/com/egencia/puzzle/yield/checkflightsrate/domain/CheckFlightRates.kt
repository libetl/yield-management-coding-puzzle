package com.egencia.puzzle.yield.checkflightsrate.domain

import com.egencia.puzzle.yield.checkflightsrate.domain.Rates.AirportCode
import java.time.ZonedDateTime

fun List<Rates>.lookup(departure: AirportCode, destination: AirportCode, dateTime: ZonedDateTime) : List<SearchResult> {
    return emptyList()
}
