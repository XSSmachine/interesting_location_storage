package org.unizd.rma.kovacevic.data.repository

import kotlinx.coroutines.flow.Flow
import org.unizd.rma.kovacevic.data.local.LocationDao
import org.unizd.rma.kovacevic.data.local.model.Location
import org.unizd.rma.kovacevic.domain.repository.Repository
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val locationDao: LocationDao
):Repository {
    override fun getAllLocations(): Flow<List<Location>> {
        return locationDao.getAllLocations()
    }

    override fun getLocationById(id: Long): Flow<Location> {
        return locationDao.getLocationById(id)
    }

    override suspend fun insert(location: Location) {
        locationDao.insertLocation(location)
    }

    override suspend fun update(location: Location) {
        locationDao.updateLocation(location)
    }

    override suspend fun delete(id: Long) {
        locationDao.delete(id)
    }
}