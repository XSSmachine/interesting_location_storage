package org.unizd.rma.kovacevic.domain.use_cases

import kotlinx.coroutines.flow.Flow
import org.unizd.rma.kovacevic.data.local.model.Location
import org.unizd.rma.kovacevic.domain.repository.Repository
import javax.inject.Inject

class GetAllLocationsUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(): Flow<List<Location>> = repository.getAllLocations()
}