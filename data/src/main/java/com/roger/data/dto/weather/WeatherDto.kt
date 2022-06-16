package com.roger.data.dto.weather

data class WeatherDto(
    val records: Records? = null,
    val result: Result? = null,
    val success: String? = null,
) {
    data class Records(
        val datasetDescription: String? = null,
        val location: List<Location>? = null,
    ) {
        data class Location(
            val locationName: String? = null,
            val weatherElement: List<WeatherElement>? = null,
        ) {
            data class WeatherElement(
                val elementName: String? = null,
                val time: List<Time>? = null,
            ) {
                data class Time(
                    val endTime: String? = null,
                    val parameter: Parameter? = null,
                    val startTime: String? = null,
                ) {
                    data class Parameter(
                        val parameterName: String? = null,
                        val parameterUnit: String? = null,
                        val parameterValue: String? = null,
                    )
                }
            }
        }
    }

    data class Result(
        val fields: List<Field>? = null,
        val resource_id: String? = null,
    ) {
        data class Field(
            val id: String? = null,
            val type: String? = null,
        )
    }
}