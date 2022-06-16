package com.roger.data.mapper.pet

import com.roger.domain.entity.pet.PetEntity
import javax.inject.Inject

class PetWeatherMapper @Inject constructor() {
    fun toWeatherEntity(entity: PetEntity?, min: String?, max: String?, wx: String?): PetEntity {
        if (entity == null) {
            return PetEntity()
        }
        return PetEntity(
            id = entity.id,
            subId = entity.subId,
            areaId = entity.areaId,
            shelterId = entity.shelterId,
            petPlace = entity.petPlace,
            kind = entity.kind,
            sex = entity.sex,
            bodyType = entity.bodyType,
            colour = entity.colour,
            age = entity.age,
            sterilization = entity.sterilization,
            vaccine = entity.vaccine,
            foundPlace = entity.foundPlace,
            title = entity.title,
            status = entity.status,
            remark = entity.remark,
            caption = entity.caption,
            adoptOpenDate = entity.adoptOpenDate,
            adoptClosedDate = entity.adoptClosedDate,
            infoUpdateTime = entity.infoUpdateTime,
            infoCreateTime = entity.infoCreateTime,
            shelterName = entity.shelterName,
            albumFile = entity.albumFile,
            albumUpdateTime = entity.albumUpdateTime,
            cDate = entity.cDate,
            shelterAddress = entity.shelterAddress,
            shelterTel = entity.shelterTel,
            variety = entity.variety,
            weatherMin = min,
            weatherMax = max,
            weatherWx = wx
        )
    }
}