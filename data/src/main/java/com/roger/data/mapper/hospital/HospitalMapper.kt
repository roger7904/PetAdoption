package com.roger.data.mapper.hospital

import com.roger.data.dto.hospital.HospitalDto
import com.roger.data.mapper.Mapper
import com.roger.domain.entity.hospital.HospitalEntity
import javax.inject.Inject

class HospitalMapper @Inject constructor() :
    Mapper<HospitalEntity, HospitalDto> {
    override fun toDto(entity: HospitalEntity?): HospitalDto {
        if (entity == null) {
            return HospitalDto()
        }
        return HospitalDto(
            縣市 = entity.city,
            字號 = entity.number,
            執照類別 = entity.kind,
            狀態 = entity.status,
            機構名稱 = entity.name,
            負責獸醫 = entity.doctor,
            機構電話 = entity.mobile,
            發照日期 = entity.date,
            機構地址 = entity.location
        )
    }

    override fun toEntity(dto: HospitalDto?): HospitalEntity {
        if (dto == null) {
            return HospitalEntity()
        }
        return HospitalEntity(
            city = dto.縣市,
            number = dto.字號,
            kind = dto.執照類別,
            status = dto.狀態,
            name = dto.機構名稱,
            doctor = dto.負責獸醫,
            mobile = dto.機構電話,
            date = dto.發照日期,
            location = dto.機構地址,
        )
    }
}