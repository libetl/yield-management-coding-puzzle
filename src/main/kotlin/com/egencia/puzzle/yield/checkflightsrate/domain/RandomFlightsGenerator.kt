package com.egencia.puzzle.yield.checkflightsrate.domain

import io.github.benas.randombeans.api.EnhancedRandom
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.LocalDate
import java.time.ZoneId
import java.util.stream.Collectors

object RandomFlightsGenerator {
    fun run () {
        val rates = EnhancedRandom.randomStreamOf(1000, Rates::class.java).collect(Collectors.toList())
        val normalizedRates = rates.map { Rates(
                segment = Rates.Segment(
                        origin = it.segment.origin,
                        destination = if (it.segment.origin == it.segment.destination)
                            Rates.AirportCode.values().filter { code -> code != it.segment.origin }.shuffled()[0]
                        else it.segment.destination,
                        departureTime = LocalDate.now().atTime(it.segment.departureTime.toLocalTime()).atZone(ZoneId.of("CET")),
                        arrivalTime = LocalDate.now().atTime(it.segment.departureTime.toLocalTime()).atZone(ZoneId.of("CET"))
                                .plusMinutes(90),
                        flightNumber = Rates.Segment.FlightNumber(airlineCode = it.segment.flightNumber.airlineCode,
                                number = Math.abs(it.segment.flightNumber.number) % 10000)),
                availabilityByBookingClass = it.availabilityByBookingClass.map { it.key to Math.abs(it.value) % 10 }.toMap(),
                priceByBookingClass = Rates.BookingClass.values().mapIndexed {
                    index, clazz -> clazz to
                        Money.of(CurrencyUnit.EUR, BigDecimal.valueOf(50 + (index + Math.random())  * 20)
                                .setScale(2, RoundingMode.UP))}.toMap())}


        val classpathUrl = Thread.currentThread().contextClassLoader.getResource("sampleData.csv")

        LoadFlightsPricingData.saveFile(classpathUrl.file.replace("sampleData.csv", "aLotOfData.csv"), normalizedRates)
    }
}
