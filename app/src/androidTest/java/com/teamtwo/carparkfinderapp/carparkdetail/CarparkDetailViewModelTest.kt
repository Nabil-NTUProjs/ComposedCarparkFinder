package com.teamtwo.carparkfinderapp.carparkdetail

import com.teamtwo.carparkfinderapp.data.repository.FakeAvailabilityRepository
import com.teamtwo.carparkfinderapp.data.repository.FakeCarparkRepository
import com.teamtwo.carparkfinderapp.presentation.carparkdetail.CarparkDetailViewModel
import org.junit.Before
import org.junit.Test

class CarparkDetailViewModelTest {

    private lateinit var viewModel: CarparkDetailViewModel

    @Before
    fun setup() {
        viewModel = CarparkDetailViewModel(FakeCarparkRepository(), FakeAvailabilityRepository())
    }

    @Test
    fun getCarparksWithHTTPError() {

    }
}