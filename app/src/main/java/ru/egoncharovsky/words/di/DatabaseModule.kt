package ru.egoncharovsky.words.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.egoncharovsky.words.database.AppDatabase
import ru.egoncharovsky.words.database.dao.DictionaryEntryDao
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase {
        return AppDatabase.getInstance(context)
    }

    @Provides
    fun provideDictionaryEntryDao(appDatabase: AppDatabase): DictionaryEntryDao {
        return appDatabase.dictionaryEntryDao()
    }
}