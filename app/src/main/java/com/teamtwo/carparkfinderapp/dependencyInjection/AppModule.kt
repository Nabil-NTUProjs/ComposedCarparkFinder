package com.teamtwo.carparkfinderapp.dependencyInjection

import android.app.Application
import androidx.room.Room
import com.teamtwo.carparkfinderapp.data.local.AvailabilityDatabase
import com.teamtwo.carparkfinderapp.data.local.CarparkDatabase
import com.teamtwo.carparkfinderapp.data.remote.AvailabilityApi
import com.teamtwo.carparkfinderapp.data.remote.CarparkApi
import com.teamtwo.carparkfinderapp.data.repository.AvailabilityRepositoryImpl
import com.teamtwo.carparkfinderapp.data.repository.CarparkRepositoryImpl
import com.teamtwo.carparkfinderapp.domain.repository.AvailabilityRepository
import com.teamtwo.carparkfinderapp.domain.repository.CarparkRepository
import com.teamtwo.carparkfinderapp.domain.usecase.GetCarparks
import com.teamtwo.carparkfinderapp.domain.usecase.SetBookmark
import com.teamtwo.carparkfinderapp.util.Constants.BASE_URL
import com.teamtwo.carparkfinderapp.util.Constants.BASE_URL2
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /*
    FOR THE CARPARK DATA
     */

    // provide the api (retrofit instance) and repository as dependencies to inject to the viewModels
    @Singleton
    @Provides
    // provides retrofit instance
    fun provideCarparkApi(): CarparkApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(CarparkApi::class.java)  // create implementation of api interface
    }

    // repository depends on database, create a persistent database from the Data layer
    @Singleton
    @Provides
    fun provideCarparkDatabase(app: Application): CarparkDatabase {
        return Room.databaseBuilder(
            app,
            CarparkDatabase::class.java,
            "carpark.db"
        ).build()
    }

    // create a repository with the database instance
    // allows for injection of the repository to the viewModel
    @Singleton
    @Provides
    fun provideCarparkRepository(
        db: CarparkDatabase,
        api: CarparkApi
    ): CarparkRepository {
        return CarparkRepositoryImpl(api, db.dao)
    }

    @Singleton
    @Provides
    fun provideGetCarparksUseCase(
        repository: CarparkRepository
    ): GetCarparks {
        return GetCarparks(repository)
    }

    @Singleton
    @Provides
    fun provideSetBookmarkUseCase(
        repository: CarparkRepository
    ): SetBookmark {
        return SetBookmark(repository)
    }

    /*
    FOR THE AVAILABILITY DATA
     */

    // provide the api (retrofit instance) and repository as dependencies to inject to the viewModels
    @Singleton
    @Provides
    // provides retrofit instance
    fun provideAvailabilityApi(): AvailabilityApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL2)
            .build()
            .create(AvailabilityApi::class.java)  // create implementation of api interface
    }

    // repository depends on database, create a persistent database from the Data layer
    @Singleton
    @Provides
    fun provideAvailabilityDatabase(app: Application): AvailabilityDatabase {

        return Room.databaseBuilder(
            app,
            AvailabilityDatabase::class.java,
            "availability.db"
        ).build()
    }

    // create a repository with the database instance
    // allows for injection of the repository to the viewModel
    @Singleton
    @Provides
    fun provideAvailabilityRepository(
        db: AvailabilityDatabase,
        api: AvailabilityApi
    ): AvailabilityRepository {
        return AvailabilityRepositoryImpl(api, db.dao)
    }
}