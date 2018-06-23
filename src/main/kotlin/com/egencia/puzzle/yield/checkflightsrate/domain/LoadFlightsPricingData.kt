package com.egencia.puzzle.yield.checkflightsrate.domain

import com.egencia.puzzle.yield.checkflightsrate.domain.Rates.*
import com.egencia.puzzle.yield.checkflightsrate.domain.Rates.Segment.FlightNumber
import org.joda.money.Money.parse
import java.io.*
import java.time.ZonedDateTime

object LoadFlightsPricingData {

    fun saveFile(fileName: String, rates: List<Rates>) {
        val writer = BufferedWriter(FileWriter(File(fileName)))

        writer.write("FlightNumber,Departure,Arrival,Origin,Destination,BookingClass,Price,Availability,BookingClass,Price,Availability,BookingClass,Price,Availability,BookingClass,Price,Availability,BookingClass,Price,Availability,BookingClass,Price,Availability,BookingClass,Price,Availability,BookingClass,Price,Availability,BookingClass,Price,Availability")
        writer.newLine()

        rates.forEach {
            writer.write("${it.segment.flightNumber.airlineCode}${it.segment.flightNumber.number},${it.segment.departureTime},${it.segment.arrivalTime},${it.segment.origin},${it.segment.destination}")
            it.priceByBookingClass.keys.forEach { clazz -> writer.write(",$clazz,${it.priceByBookingClass[clazz]!!},${it.availabilityByBookingClass[clazz]?: 0}")}
            writer.newLine()
        }

        writer.close()
    }

    fun loadFile(file : String): List<Rates> {
        val classpathUrl = Thread.currentThread().contextClassLoader.getResource(file)

        val reader = BufferedReader(FileReader(classpathUrl.file))

        reader.readLine() // headers

        var line: String ? = reader.readLine()
        var result : List<Rates> = listOf()

        while (line != null){
            val tokens = line.split(",")
            if (tokens.isNotEmpty()) {
                val serializedFares = tokens.drop(5)
                val groupedFares = serializedFares.withIndex().groupBy { it.index / 3 }.map {it.value}
                val newRate = Rates(
                        segment = Segment(
                                departureTime = ZonedDateTime.parse(tokens[1]),
                                arrivalTime = ZonedDateTime.parse(tokens[2]),
                                flightNumber = FlightNumber(airlineCode = AirlineCode.valueOf(tokens[0].substring(0, 2)),
                                        number = tokens[0].substring(2).toInt()),
                                origin = AirportCode.valueOf(tokens[3]),
                                destination = AirportCode.valueOf(tokens[4])),
                        priceByBookingClass = mapOf(
                                *groupedFares.map {Pair(BookingClass.valueOf(it[0].value),
                                        parse(it[1].value))}.toTypedArray()),
                        availabilityByBookingClass = mapOf(
                                *groupedFares.map {Pair(BookingClass.valueOf(it[0].value),
                                        it[2].value.toInt())}.toTypedArray()))
                result = result.plus(newRate)
            }
            line = reader.readLine()
        }

        reader.close()
        return result
    }
}
