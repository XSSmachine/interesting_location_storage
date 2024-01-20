package org.unizd.rma.kovacevic.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import org.unizd.rma.kovacevic.data.local.converters.DateConverter
import org.unizd.rma.kovacevic.data.local.model.Location

@TypeConverters(value = [DateConverter::class])
@Database(
    entities = [Location::class],
    version = 1,
    exportSchema = false
)
abstract class LocationDatabase:RoomDatabase() {

    abstract val locationDao:LocationDao

}