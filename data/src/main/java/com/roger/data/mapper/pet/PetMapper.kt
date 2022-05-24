package com.roger.data.mapper.pet

import com.roger.data.dto.pet.PetDto
import com.roger.data.mapper.Mapper
import com.roger.domain.entity.pet.PetEntity
import javax.inject.Inject

class PetMapper @Inject constructor() :
    Mapper<PetEntity, PetDto> {
    override fun toDto(entity: PetEntity?): PetDto {
        if (entity == null) {
            return PetDto()
        }
        return PetDto(
            animal_id = entity.id,
            animal_subid = entity.subId,
            animal_area_pkid = entity.areaId,
            animal_shelter_pkid = entity.shelterId,
            animal_place = entity.petPlace,
            animal_kind = entity.kind,
            animal_sex = entity.sex,
            animal_bodytype = entity.bodyType,
            animal_colour = entity.colour,
            animal_age = entity.age,
            animal_sterilization = entity.sterilization,
            animal_bacterin = entity.vaccine,
            animal_foundplace = entity.foundPlace,
            animal_title = entity.title,
            animal_status = entity.status,
            animal_remark = entity.remark,
            animal_caption = entity.caption,
            animal_opendate = entity.adoptOpenDate,
            animal_closeddate = entity.adoptClosedDate,
            animal_update = entity.infoUpdateTime,
            animal_createtime = entity.infoCreateTime,
            shelter_name = entity.shelterName,
            album_file = entity.albumFile,
            album_update = entity.albumUpdateTime,
            cDate = entity.cDate,
            shelter_address = entity.shelterAddress,
            shelter_tel = entity.shelterTel,
            animal_Variety = entity.variety,
        )
    }

    override fun toEntity(dto: PetDto?): PetEntity {
        if (dto == null) {
            return PetEntity()
        }
        return PetEntity(
            id = dto.animal_id,
            subId = dto.animal_subid,
            areaId = dto.animal_area_pkid,
            shelterId = dto.animal_shelter_pkid,
            petPlace = dto.animal_place,
            kind = dto.animal_kind,
            sex = dto.animal_sex,
            bodyType = dto.animal_bodytype,
            colour = dto.animal_colour,
            age = dto.animal_age,
            sterilization = dto.animal_sterilization,
            vaccine = dto.animal_bacterin,
            foundPlace = dto.animal_foundplace,
            title = dto.animal_title,
            status = dto.animal_status,
            remark = dto.animal_remark,
            caption = dto.animal_caption,
            adoptOpenDate = dto.animal_opendate,
            adoptClosedDate = dto.animal_closeddate,
            infoUpdateTime = dto.animal_update,
            infoCreateTime = dto.animal_createtime,
            shelterName = dto.shelter_name,
            albumFile = dto.album_file,
            albumUpdateTime = dto.album_update,
            cDate = dto.cDate,
            shelterAddress = dto.shelter_address,
            shelterTel = dto.shelter_tel,
            variety = dto.animal_Variety,
        )
    }
}