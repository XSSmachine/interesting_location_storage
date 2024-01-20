package org.unizd.rma.kovacevic.domain.use_cases

import org.unizd.rma.kovacevic.domain.repository.Repository
import javax.inject.Inject

class GetLocationByIdUseCase @Inject constructor(
    private val repository: Repository
) {
    operator fun invoke(id:Long) = repository.getLocationById(id)
}