package org.unizd.rma.kovacevic.domain.repository

import kotlinx.coroutines.flow.Flow
import org.unizd.rma.kovacevic.data.local.model.Location

interface Repository {

    fun getAllLocations(): Flow<List<Location>>
    fun getLocationById(id:Long):Flow<Location>
    suspend fun insert(location: Location)
    suspend fun update(location: Location)
    suspend fun delete(id: Long)

}