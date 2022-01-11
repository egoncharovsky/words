package ru.egoncharovsky.words.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.egoncharovsky.words.domain.importing.CsvParser
import ru.egoncharovsky.words.domain.importing.DictionaryImporter
import ru.egoncharovsky.words.repository.persistent.WordRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideDictionaryImporter(wordRepository: WordRepository, csvParser: CsvParser): DictionaryImporter {
        return DictionaryImporter(wordRepository, csvParser)
    }

    @Singleton
    @Provides
    fun provideCsvParser(): CsvParser {
        return CsvParser()
    }

}