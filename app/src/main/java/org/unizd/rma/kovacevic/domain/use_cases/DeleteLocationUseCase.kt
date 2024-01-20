package org.unizd.rma.kovacevic.domain.use_cases

import org.unizd.rma.kovacevic.domain.repository.Repository
import javax.inject.Inject

class DeleteLocationUseCase @Inject constructor(
    private val repository: Repository
) {
    suspend operator fun invoke(id:Long) = repository.delete(id)
}