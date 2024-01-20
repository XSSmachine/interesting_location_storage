package org.unizd.rma.kovacevic.domain.use_cases

import org.unizd.rma.kovacevic.data.local.model.Location
import org.unizd.rma.kovacevic.domain.repository.Repository
import javax.inject.Inject

class UpdateLocationUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(location: Location) {
        repository.update(location)
    }
}