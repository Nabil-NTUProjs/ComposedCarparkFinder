package com.teamtwo.carparkfinderapp.domain.usecase

import com.teamtwo.carparkfinderapp.domain.repository.CarparkRepository

class SetBookmark (private val repository: CarparkRepository) {

    // with this, the use case class may be called as a function
    // the list viewModel will call this use case to set or unset the bookmark flag in dao

    suspend operator fun invoke(flag: Int, id: Int) {
        repository.updateBookmark(flag, id)
    }
}