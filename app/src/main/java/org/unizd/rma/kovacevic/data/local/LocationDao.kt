package org.unizd.rma.kovacevic.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import org.unizd.rma.kovacevic.data.local.model.Location

@Dao
interface LocationDao {

    @Query("SELECT * FROM locations ORDER BY createdDate")
    fun getAllLocations(): Flow<List<Location>>

    @Query("SELECT * FROM locations WHERE id = :id ORDER BY createdDate")
    fun getLocationById(id:Long):Flow<Location>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(location: Location)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateLocation(location: Location)

    @Query("DELETE FROM locations WHERE id=:id")
    suspend fun delete(id:Long)




}