package ru.egoncharovsky.words.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import ru.egoncharovsky.words.domain.importing.CsvParser
import ru.egoncharovsky.words.domain.importing.DictionaryImporter
import ru.egoncharovsky.words.domain.importing.WordPopularityImporter
import ru.egoncharovsky.words.domain.service.WordService
import ru.egoncharovsky.words.repository.persistent.WordRepository
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DomainModule {

    @Singleton
    @Provides
    fun provideDictionaryImporter(
        wordRepository: WordRepository,
        csvParser: CsvParser
    ): DictionaryImporter {
        return DictionaryImporter(wordRepository, csvParser)
    }

    @Singleton
    @Provides
    fun provideCsvParser(): CsvParser {
        return CsvParser()
    }

    @Singleton
    @Provides
    fun provideWordService(wordRepository: WordRepository): WordService {
        return WordService(wordRepository)
    }

    @Singleton
    @Provides
    fun provideWordPopularityImporter(wordRepository: WordRepository): WordPopularityImporter {
        return WordPopularityImporter(wordRepository)
    }

}