package com.egencia.puzzle.yield.checkflightsrate.domain

import com.egencia.puzzle.yield.checkflightsrate.domain.Rates.AirportCode
import com.egencia.puzzle.yield.checkflightsrate.domain.Rates.BookingClass
import com.egencia.puzzle.yield.checkflightsrate.domain.SearchResult.Proposal
import com.egencia.puzzle.yield.checkflightsrate.domain.SearchResult.Proposal.Companion.UNAVAILABLE
import org.joda.money.Money
import java.time.ZonedDateTime

fun List<Rates>.lookup(departure: AirportCode, destination: AirportCode, dateTime: ZonedDateTime) : List<SearchResult> =
        this.findAllPossibleTrips(departure, destination, dateTime)
                .returnWithTheLowestPrices()

private fun List<Rates>.findAllPossibleTrips(departure: AirportCode, destination: AirportCode, dateTime: ZonedDateTime,
                                             maxArrivalTime : ZonedDateTime = dateTime.plusDays(1),
                                             prefix: List<Rates> = listOf()): List<List<Rates>> {
    //In the kotlin version, the input is 'this' (a list of Rates)

    //Find all the flights matching the departure airport and departing after dateTime
    // 1°) First, find the direct flights from departure to destination
    val immediateArrival =
            this.filter {it.segment.origin === departure && it.segment.destination === destination &&
                    dateTime <= it.segment.departureTime && it.segment.arrivalTime < maxArrivalTime}
                    .map {prefix + it}

    // 2°) Then if you are not in a hurry, find all the non-direct flights
    // (flights taking off from the right airport, but not landing in the final destination)
    val indirectArrival : List<List<Rates>> = this.filter { it.segment.origin === departure &&
            dateTime <= it.segment.departureTime && it.segment.arrivalTime < maxArrivalTime &&
            !prefix.map {prefixSegment -> prefixSegment.segment.origin}.contains(it.segment.destination) &&
            it.segment.destination != destination}
            .flatMap { findAllPossibleTrips(it.segment.destination, destination,
                    it.segment.arrivalTime.plusMinutes(30), maxArrivalTime,
                    prefix + it)}

    return immediateArrival + indirectArrival
}

private fun List<List<Rates>>.returnWithTheLowestPrices () : List<SearchResult> =
// 3°) Finally, With the union of the two results, find the cheapest prices for each fare
// (economy, premium, business, first)
        this.map {SearchResult(
        segments = it.map { rates -> rates.segment },
        priceByFare = Rates.Fare.values()
                .map { fare -> fare to it.lowestPriceFor(fare) }
                .filter { pair -> pair.second != UNAVAILABLE }
                .toMap())}

private fun List<Rates>.lowestPriceFor(fare: Rates.Fare): Proposal {
    val bookingClasses = this.map { rate ->
        BookingClass.values().filter { it.fare == fare }.find { rate.availabilityByBookingClass[it] ?: 0  > 0 }}
    if (bookingClasses.any { it == null }) return Proposal.UNAVAILABLE

    val totalPrice = bookingClasses.foldIndexed(Money.zero(this[0].priceByBookingClass.values.toList()[0].currencyUnit),
            {index, acc, value -> acc.plus(this[index].priceByBookingClass[value])})

    return Proposal(totalPrice = totalPrice, bookingClasses = bookingClasses.map { it!! })
}
