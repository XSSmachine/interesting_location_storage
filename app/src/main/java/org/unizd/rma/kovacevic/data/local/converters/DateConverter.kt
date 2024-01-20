package org.unizd.rma.kovacevic.data.local.converters

import androidx.room.TypeConverter
import java.util.*



class DateConverter {
    @TypeConverter
    fun toDate(date:Long?): Date?{
        return date?.let { Date(it) }
    }
    @TypeConverter
    fun fromDate(date:Date?): Long?{
        return date?.time
    }
}