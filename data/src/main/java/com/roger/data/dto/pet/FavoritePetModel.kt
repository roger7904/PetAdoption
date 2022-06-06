package com.roger.data.dto.pet

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.roger.data.local_storage.DatabaseManager

@Entity(tableName = DatabaseManager.TABLE_FAVORITE_PET)
data class FavoritePetModel(
    @PrimaryKey
    @ColumnInfo(name = COL_ID)
    var id: String,

    @ColumnInfo(name = COL_USER_ID)
    var userId: String,

    @ColumnInfo(name = COL_PET_ID)
    var petId: Int,
) {
    companion object {
        const val COL_ID = "id"
        const val COL_USER_ID = "user_id"
        const val COL_PET_ID = "pet_id"
    }
}