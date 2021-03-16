package ru.egoncharovsky.words.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import ru.egoncharovsky.words.database.AppDatabase
import ru.egoncharovsky.words.database.dao.StudyListDao
import ru.egoncharovsky.words.database.dao.WordDao
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
    fun provideWordDao(appDatabase: AppDatabase): WordDao {
        return appDatabase.wordDao()
    }

    @Provides
    fun provideStudyListDao(appDatabase: AppDatabase): StudyListDao {
        return appDatabase.studyListDao()
    }
}