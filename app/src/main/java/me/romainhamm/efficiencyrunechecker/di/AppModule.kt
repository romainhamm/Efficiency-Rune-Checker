package me.romainhamm.efficiencyrunechecker.di

import com.squareup.moshi.Moshi
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.romainhamm.efficiencyrunechecker.parsing.adapter.RuneListOutputAdapter
import me.romainhamm.efficiencyrunechecker.parsing.adapter.RuneOutputAdapter
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
object AppModule {

    @Provides
    @Singleton
    fun providesMoshi(
        runeAdapterFactory: RuneOutputAdapter.Factory,
        runeListOutputAdapter: RuneListOutputAdapter.Factory,
    ): Moshi = Moshi.Builder()
        .add(runeListOutputAdapter)
        .add(runeAdapterFactory)
        .build()
}
