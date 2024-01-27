package org.unizd.rma.kovacevic.di

import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import org.unizd.rma.kovacevic.data.local.LocationDao
import org.unizd.rma.kovacevic.data.local.LocationDatabase
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {
    @Provides
    @Singleton
    fun provideLocationDao(database: LocationDatabase): LocationDao =
        database.locationDao

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ):LocationDatabase = Room.databaseBuilder(
        context,
        LocationDatabase::class.java,
    "locations_db"
    ).build()
}