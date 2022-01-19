package ru.egoncharovsky.words.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.egoncharovsky.words.repository.persistent.StudyListRepository
import ru.egoncharovsky.words.repository.persistent.WordRepository
import ru.egoncharovsky.words.repository.persistent.room.StudyListRepositoryRoom
import ru.egoncharovsky.words.repository.persistent.room.WordRepositoryRoom

@InstallIn(SingletonComponent::class)
@Module
abstract class RepositoryModule {

    @Binds
    abstract fun bindWordRepository(wordRepositoryRoom: WordRepositoryRoom): WordRepository

    @Binds
    abstract fun bindStudyListRepository(studyListRepositoryRoom: StudyListRepositoryRoom): StudyListRepository

}