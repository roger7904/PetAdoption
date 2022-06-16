package com.roger.data.mapper.weather

import com.roger.data.dto.weather.WeatherDto
import com.roger.data.mapper.Mapper
import com.roger.domain.entity.weather.WeatherEntity
import javax.inject.Inject

class WeatherMapper @Inject constructor() :
    Mapper<WeatherEntity, WeatherDto> {
    override fun toDto(entity: WeatherEntity?): WeatherDto {
        if (entity == null) {
            return WeatherDto()
        }
        return WeatherDto(
            records = WeatherDto.Records(
                datasetDescription = entity.records?.datasetDescription,
                location = entity.records?.location?.map { location ->
                    WeatherDto.Records.Location(
                        locationName = location.locationName,
                        weatherElement = location.weatherElement?.map { weatherElement ->
                            WeatherDto.Records.Location.WeatherElement(
                                elementName = weatherElement.elementName,
                                time = weatherElement.time?.map {
                                    WeatherDto.Records.Location.WeatherElement.Time(
                                        endTime = it.endTime,
                                        parameter = WeatherDto.Records.Location.WeatherElement.Time.Parameter(
                                            parameterName = it.parameter?.parameterName,
                                            parameterUnit = it.parameter?.parameterUnit,
                                            parameterValue = it.parameter?.parameterValue,
                                        ),
                                        startTime = it.startTime,
                                    )
                                }
                            )
                        }
                    )
                },
            ),
            result = WeatherDto.Result(
                fields = entity.result?.fields?.map {
                    WeatherDto.Result.Field(
                        id = it.id,
                        type = it.type,
                    )
                },
                resource_id = entity.result?.resource_id,
            ),
            success = entity.success,
        )
    }

    override fun toEntity(dto: WeatherDto?): WeatherEntity {
        if (dto == null) {
            return WeatherEntity()
        }
        return WeatherEntity(
            records = WeatherEntity.Records(
                datasetDescription = dto.records?.datasetDescription,
                location = dto.records?.location?.map { location ->
                    WeatherEntity.Records.Location(
                        locationName = location.locationName,
                        weatherElement = location.weatherElement?.map { weatherElement ->
                            WeatherEntity.Records.Location.WeatherElement(
                                elementName = weatherElement.elementName,
                                time = weatherElement.time?.map {
                                    WeatherEntity.Records.Location.WeatherElement.Time(
                                        endTime = it.endTime,
                                        parameter = WeatherEntity.Records.Location.WeatherElement.Time.Parameter(
                                            parameterName = it.parameter?.parameterName,
                                            parameterUnit = it.parameter?.parameterUnit,
                                            parameterValue = it.parameter?.parameterValue,
                                        ),
                                        startTime = it.startTime,
                                    )
                                }
                            )
                        }
                    )
                },
            ),
            result = WeatherEntity.Result(
                fields = dto.result?.fields?.map {
                    WeatherEntity.Result.Field(
                        id = it.id,
                        type = it.type,
                    )
                },
                resource_id = dto.result?.resource_id,
            ),
            success = dto.success,
        )
    }
}