package com.roger.data.mapper.shelter

import com.roger.data.dto.shelter.ShelterDto
import com.roger.data.mapper.Mapper
import com.roger.domain.entity.shelter.ShelterEntity
import javax.inject.Inject

class ShelterMapper @Inject constructor() :
    Mapper<ShelterEntity, ShelterDto> {
    override fun toDto(entity: ShelterEntity?): ShelterDto {
        if (entity == null) {
            return ShelterDto()
        }
        return ShelterDto(
            ID = entity.id,
            ShelterName = entity.shelterName,
            CityName = entity.cityName,
            Address = entity.address,
            Phone = entity.phone,
            OpenTime = entity.openTime,
            Url = entity.url,
            Lon = entity.lon,
            Lat = entity.lat
        )
    }

    override fun toEntity(dto: ShelterDto?): ShelterEntity {
        if (dto == null) {
            return ShelterEntity()
        }
        return ShelterEntity(
            id = dto.ID,
            shelterName = dto.ShelterName,
            cityName = dto.CityName,
            address = dto.Address,
            phone = dto.Phone,
            openTime = dto.OpenTime,
            url = dto.Url,
            lon = dto.Lon,
            lat = dto.Lat,
        )
    }
}