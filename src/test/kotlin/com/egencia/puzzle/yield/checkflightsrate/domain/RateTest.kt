package com.egencia.puzzle.yield.checkflightsrate.domain

import com.egencia.puzzle.yield.checkflightsrate.domain.LoadFlightsPricingData.loadFile
import com.egencia.puzzle.yield.checkflightsrate.domain.Rates.AirlineCode.AF
import com.egencia.puzzle.yield.checkflightsrate.domain.Rates.AirlineCode.VS
import com.egencia.puzzle.yield.checkflightsrate.domain.Rates.AirportCode.NCE
import com.egencia.puzzle.yield.checkflightsrate.domain.Rates.AirportCode.ORY
import com.egencia.puzzle.yield.checkflightsrate.domain.Rates.BookingClass.*
import com.egencia.puzzle.yield.checkflightsrate.domain.Rates.Fare.*
import com.egencia.puzzle.yield.checkflightsrate.domain.Rates.Segment
import com.egencia.puzzle.yield.checkflightsrate.domain.Rates.Segment.FlightNumber
import com.egencia.puzzle.yield.checkflightsrate.domain.SearchResult.Proposal
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test
import java.math.BigDecimal.ZERO
import java.time.ZonedDateTime

class RateTest {

    private val randomData = loadFile("aLotOfData.csv")

    @Test
    fun noSegmentInAResultHasTheFinalDestinationAsOrigin(){

        //when
        val results = randomData.lookup(departure = ORY, destination = NCE,
                dateTime = ZonedDateTime.parse("2018-07-10T12:20:00.000+02:00"))

        //then
        assertTrue(results.none {
            result -> result.segments.any { segment -> segment.origin == NCE } })
    }

    @Test
    fun allResultsHaveAtLeastOneProposal(){

        //when
        val results = randomData.lookup(departure = ORY, destination = NCE,
                dateTime = ZonedDateTime.parse("2018-07-10T12:20:00.000+02:00"))

        //then
        assertTrue(results.none { result -> result.priceByFare.isEmpty() })
        assertTrue(results.none { result -> result.priceByFare.any {
            proposal -> proposal.value.totalPrice.amount == ZERO} })
    }

    @Test
    fun everyFirstFareShouldBeMoreExpensiveThanTheEconomyFare(){

        //when
        val results = randomData.lookup(departure = ORY, destination = NCE,
                dateTime = ZonedDateTime.parse("2018-07-10T12:20:00.000+02:00"))

        //then
        assertTrue(results.none {
            result -> result.priceByFare[FIRST] != null && result.priceByFare[ECONOMY] != null &&
                        result.priceByFare[FIRST]!!.totalPrice < result.priceByFare[ECONOMY]!!.totalPrice})
    }



    @Test
    fun readFromRandomData () {

        //when
        val results = randomData.lookup(departure = ORY, destination = NCE,
                dateTime = ZonedDateTime.parse("2018-07-10T12:20:00.000+02:00"))

        //then
        assertTrue(
                results.contains(
                        SearchResult(
                                segments= listOf(Segment(flightNumber= FlightNumber(airlineCode=VS, number=3819),
                                        origin=ORY, destination=NCE,
                                        departureTime= ZonedDateTime.parse("2018-07-10T17:04:49+02:00[CET]"),
                                        arrivalTime= ZonedDateTime.parse("2018-07-10T18:34:49+02:00[CET]"))),
                                priceByFare=mapOf(
                                        Pair(DISCOUNTED,Proposal(totalPrice=56 `€` 57, bookingClasses=listOf(K))),
                                        Pair(ECONOMY,Proposal(totalPrice=265 `€` 15, bookingClasses=listOf(Y))),
                                        Pair(PREMIUM,Proposal(totalPrice=343 `€` 72, bookingClasses=listOf(W))),
                                        Pair(DISCOUNTED_BUSINESS,Proposal(totalPrice=370 `€` 85, bookingClasses=listOf(D))),
                                        Pair(BUSINESS,Proposal(totalPrice= 450 `€` 35, bookingClasses=listOf(C)))))))
    }

    @Test
    fun readFromSampleData () {
        //given
        val rates = loadFile("sampleData.csv")

        //when
        val results = rates.lookup(departure = ORY, destination = NCE,
                dateTime = ZonedDateTime.parse("2018-07-10T12:20:00.000+02:00"))

        //then
        assertEquals(
                listOf(SearchResult(segments=listOf(Segment(
                        flightNumber=FlightNumber(airlineCode=AF, number=1111),
                        origin=ORY, destination=NCE,
                        departureTime=ZonedDateTime.parse("2018-07-10T12:20+02:00"),
                        arrivalTime= ZonedDateTime.parse("2018-07-10T13:50+02:00"))),
                        priceByFare=mapOf(
                                Pair(DISCOUNTED, Proposal(totalPrice=499 `€` 0, bookingClasses=listOf(K))),
                                Pair(ECONOMY, Proposal(totalPrice=59 `€` 99, bookingClasses=listOf(Y)))))),
                results)
    }

    @Test
    fun buildOneRate () {
        //given
        val dateTime = ZonedDateTime.now()
        val rates = listOf(Rates(
                segment = Segment(
                        departureTime = dateTime,
                        arrivalTime = dateTime.plusHours(1).plusMinutes(20),
                        flightNumber = FlightNumber(airlineCode = AF, number = 6213),
                        origin = ORY, destination = NCE),
                priceByBookingClass = mapOf(
                        Pair(Y, 119 `€` 99),
                        Pair(K, 249 `€` 99)),
                availabilityByBookingClass = mapOf(
                        Pair(Y, 0),
                        Pair(K, 5))))

        //when
        val results = rates.lookup(departure = ORY, destination = NCE, dateTime = dateTime)

        //then

        assertEquals(
                listOf(SearchResult(segments=listOf(Segment(
                        flightNumber=FlightNumber(airlineCode=AF, number=6213),
                        origin=ORY, destination=NCE,
                        departureTime=dateTime,
                        arrivalTime= dateTime.plusHours(1).plusMinutes(20))),
                        priceByFare=mapOf(
                                Pair(DISCOUNTED, Proposal(totalPrice=249 `€` 99, bookingClasses=listOf(K)))))),
                results)
    }
}