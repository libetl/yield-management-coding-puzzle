package com.egencia.puzzle.yield.checkflightsrate.domain

import org.joda.money.Money

infix fun Int.`€`(decimals: Int): Money =
        Money.parse("EUR$this.$decimals")
infix fun Int.`$`(decimals: Int): Money =
        Money.parse("USD$this.$decimals")
