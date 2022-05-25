package com.roger.data.dto.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.roger.data.local_storage.DatabaseManager

@Entity(tableName = DatabaseManager.TABLE_USER)
data class UserModel(
    @ColumnInfo(name = COL_USER_ID)
    @PrimaryKey
    var userId: String,

    @ColumnInfo(name = COL_USER_NAME)
    var userName: String?,
) {
    companion object {
        const val COL_USER_ID = "user_id"
        const val COL_USER_NAME = "user_name"
    }
}