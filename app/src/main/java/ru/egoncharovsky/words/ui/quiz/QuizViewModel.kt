package ru.egoncharovsky.words.ui.quiz

import androidx.lifecycle.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import mu.KotlinLogging
import ru.egoncharovsky.words.domain.quiz.QuizManager
import ru.egoncharovsky.words.domain.quiz.card.*
import ru.egoncharovsky.words.repository.persistent.StudyListRepository
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val studyListRepository: StudyListRepository
): ViewModel() {

    private val logger = KotlinLogging.logger {}

    private lateinit var manager: QuizManager
    private lateinit var nextCard: () -> Card?

    private val card = MutableLiveData<Card>()
    private val answerIsCorrect = MutableLiveData<Boolean?>()
    private val nextIsVisible = MutableLiveData<Boolean>()
    private val finished = MutableLiveData<Boolean>()
    private val progress = MutableLiveData<Int>()

    fun getCard(): LiveData<Card> = card

    fun getAnswerWitchCallback(): LiveData<QuestionWithCallback<Answer, String>> =
        Transformations.map(card) { QuestionWithCallback(it as Answer) }

    fun getMeaning(): LiveData<MeaningWithShowedTrigger> =
        Transformations.map(card) { MeaningWithShowedTrigger(it as Meaning) }

    fun getMultiChoiceWithCallback(): LiveData<QuestionWithCallback<MultiChoice, String>> =
        Transformations.map(card) { QuestionWithCallback(it as MultiChoice) }

    fun getRememberWithCallback(): LiveData<QuestionWithCallback<Remember, Remember.Option>> =
        Transformations.map(card) { QuestionWithCallback(it as Remember) }

    fun getRememberRightWithCallback(): LiveData<QuestionWithCallback<RememberRight, RememberRight.Option>> =
        Transformations.map(card) { QuestionWithCallback(it as RememberRight) }

    fun getAnswerCorrectness(): LiveData<Boolean?> = answerIsCorrect
    fun getNextVisibility(): LiveData<Boolean> = nextIsVisible
    fun getFinished(): LiveData<Boolean> = finished
    fun getProgress(): LiveData<Int> = progress

    inner class QuestionWithCallback<Q : Question<A>, A>(val question: Q) {
        fun sendAnswer(value: A) {
            nextCard = { manager.next(question, value) }
            answerIsCorrect.value = question.checkAnswer(value)
            nextIsVisible.value = true
        }
    }

    inner class MeaningWithShowedTrigger(
        meaning: Meaning
    ) {
        val meaning = meaning
            get() {
                meaningShowed()
                return field
            }

        private fun meaningShowed() {
            nextCard = { manager.next(meaning) }
            nextIsVisible.value = true
        }
    }

    fun clickNext() {
        answerIsCorrect.value = null
        val next = nextCard()
        progress.value = manager.progressPercentage()
        if (next != null) {
            nextIsVisible.value = false
            next.let { card.value = it }
        } else {
            finished.value = true
        }
    }

    fun startQuiz(studyListId: Long) {
        logger.debug("Starting quiz for study list $studyListId")

        viewModelScope.launch {
            studyListRepository.get(studyListId).collect { studyList ->
                logger.trace("Study list loaded: $studyList")

                val words = studyList.words.toSet()
                logger.trace("Total:${words.size} words:$words")

                manager = QuizManager(words)
                card.postValue(manager.start())
                progress.postValue(manager.progressPercentage())
            }
        }
    }
}