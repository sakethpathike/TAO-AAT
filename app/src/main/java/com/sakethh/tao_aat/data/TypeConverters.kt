package com.sakethh.tao_aat.data

import androidx.room3.ColumnTypeConverter
import com.sakethh.tao_aat.domain.Attribution

class TypeConverters {
    @ColumnTypeConverter
    fun stringToType(string: String) = Attribution.Type.valueOf(string)

    @ColumnTypeConverter
    fun typeToString(type: Attribution.Type) = type.name

    @ColumnTypeConverter
    fun stringToStatus(string: String) = Attribution.Status.valueOf(string)

    @ColumnTypeConverter
    fun statusToString(status: Attribution.Status) = status.name
}