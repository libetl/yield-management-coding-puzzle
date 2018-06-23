# yield-management-coding-puzzle

## Intro

The goal of this puzzle is to get a working version of the flights search results page,
including the list of the flights and the fares

## Some domain context

Every flight is priced using booking classes,
and each booking class has a price.

(fixed for this puzzle, will not depend on the popularity or the time of the day or the point of sale)

Every booking class has a fixed number of seats.

The customers buying the plane tickets will start by filling the cheapest booking class of the chosen fare 
(Economy, Business or First are some possible fares).

When there is no more seat in a booking class, then the next reservations 
will happen on the next booking classes of the same fare
(more expensive of course).

Here we will not care about return trips (which are sometimes cheaper than one way trips)

We will not carry out the calculation of the airport taxes or the service fees.

## Your work

Your mission is to write the program to display the prices of a
flight search for each fare.

Exemple :
NCE (Nice Côte d'Azur Airport) -> LGA (La Guardia airport) on the 2018-07-10 :

Result consisting of two flights (or segments) :
* AF7701, NCE->CDG, departure at 10:10AM, arrival at 11:45AM, with these fares :
ECONOMY EUR52 PREMIUM EUR102
* DL405, CDG->LGA, departure at 10:20AM, arrival at 12:52AM, with these fares :
ECONOMY EUR2007 BUSINESS EUR3500

To simplify, we will only suggest trips having the same fare on all segments.
Therefore in this example, we will show one proposal of EUR2007+EUR52=EUR2059 in economy fare

## Where to start

Look at the RateTest.kt file to see some expected behaviors.
We ask for your extra attention on those unit tests

    fun noSegmentInAResultHasTheFinalDestinationAsOrigin()
    fun allResultsHaveAtLeastOneProposal()
    fun everyFirstFareShouldBeMoreExpensiveThanTheEconomyFare()

You can add some unit tests if you think not all of them are
implemented.
Then, go to `CheckFlightsRates.kt`
If you are not comfortable with Kotlin, rename the file into
CheckFlightsRates.java and write your program in java.

Please respect that following method signature 
(no matter if you started with java or kotlin) :

    lookup(table: List<Rates>, departure: AirportCode, 
           destination: AirportCode, dateTime: ZonedDateTime)
