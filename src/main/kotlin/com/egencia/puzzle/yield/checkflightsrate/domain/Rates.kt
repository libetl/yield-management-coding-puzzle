package com.egencia.puzzle.yield.checkflightsrate.domain

import org.joda.money.Money
import java.time.ZonedDateTime
import com.egencia.puzzle.yield.checkflightsrate.domain.Rates.Fare.*

data class Rates(
        val segment: Segment,
        val priceByBookingClass: Map<BookingClass, Money>,
        val availabilityByBookingClass: Map<BookingClass, Int>){

    data class Segment(val flightNumber: FlightNumber,
                       val origin: AirportCode,
                       val destination: AirportCode,
                       val departureTime: ZonedDateTime,
                       val arrivalTime: ZonedDateTime) {
        data class FlightNumber(val airlineCode: AirlineCode,
                                val number: Int)
    }

    enum class AirlineCode {`7R`, A3, JU, W2, `3S`, CE, EK, FM, GY, IT, J2, QC, S7, T7, TX, UD, UN, UT, UU, VU,
        YD, YM, ZI, KU, AT, B2, CA, CY, IB, IG, K2, RO, BI, FJ, GA, KQ, QR, AR, MR, MS, VV, KC, MD, OZ, PG, AF,
        AI, CO, EY, HM, KL, NZ, PR, QF, SQ, US, VN, AZ, BR, CI, CX, CZ, ET, HR, JL, KA, KE, LY, MH, MU, RJ, SA,
        SK, SU, SV, TG, TK, UA, UL, VS, WY, U2}

    enum class AirportCode {ORY, CDG, RNS, NTE, NCE, MRS, MPL, BES, SXB, TLS, LYS, BDX}

    enum class BookingClass (val fare: Fare) {
        K(DISCOUNTED), L(DISCOUNTED), Q(DISCOUNTED), V(DISCOUNTED), U(DISCOUNTED), T(DISCOUNTED),
        X(DISCOUNTED), N(DISCOUNTED), O(DISCOUNTED), S(DISCOUNTED),
        Y(ECONOMY), B(ECONOMY), M(ECONOMY), H(ECONOMY),
        W(PREMIUM), E(PREMIUM),
        D(DISCOUNTED_BUSINESS), I(DISCOUNTED_BUSINESS), Z(DISCOUNTED_BUSINESS),
        J(BUSINESS), C(BUSINESS),
        A(FIRST), F(FIRST)}

    enum class Fare {DISCOUNTED, ECONOMY, PREMIUM, DISCOUNTED_BUSINESS, BUSINESS, FIRST}

}

