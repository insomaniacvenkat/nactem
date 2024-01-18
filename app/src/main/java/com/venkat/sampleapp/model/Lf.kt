package com.venkat.sampleapp.model

/*
Data classes representing lexical frequency (Lf), variations (Var), and meanings data (MeaningsDataItem and MeaningsData)
for this application from nactem API.
*/
data class Lf(
    val freq: Int,
    val lf: String,
    val since: Int,
    val vars: List<Var>
)

data class MeaningsDataItem(
    val lfs: List<Lf>,
    val sf: String
)

data class Var(
    val freq: Int,
    val lf: String,
    val since: Int
)

typealias MeaningsData = ArrayList<MeaningsDataItem>
