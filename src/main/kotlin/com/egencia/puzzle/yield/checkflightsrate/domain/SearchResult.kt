package com.egencia.puzzle.yield.checkflightsrate.domain

import org.joda.money.CurrencyUnit
import org.joda.money.Money

data class SearchResult (val segments: List<Rates.Segment>,
                         val priceByFare: Map<Rates.Fare, Proposal>){
    data class Proposal (val totalPrice: Money, val bookingClasses: List<Rates.BookingClass>){
        companion object {
            val UNAVAILABLE = Proposal(totalPrice = Money.zero(CurrencyUnit.USD), bookingClasses = emptyList())
        }
    }
}
