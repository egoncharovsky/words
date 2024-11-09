package ru.egoncharovsky.words.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import mu.KotlinLogging
import ru.egoncharovsky.words.domain.quiz.QuizManager
import ru.egoncharovsky.words.domain.quiz.card.Answer
import ru.egoncharovsky.words.domain.quiz.card.Card
import ru.egoncharovsky.words.domain.quiz.card.Meaning
import ru.egoncharovsky.words.domain.quiz.card.MultiChoice
import ru.egoncharovsky.words.domain.quiz.card.Question
import ru.egoncharovsky.words.domain.quiz.card.Remember
import ru.egoncharovsky.words.domain.quiz.card.RememberRight
import ru.egoncharovsky.words.repository.persistent.StudyListRepository
import javax.inject.Inject

@HiltViewModel
class QuizViewModel @Inject constructor(
    private val studyListRepository: StudyListRepository
) : ViewModel() {

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
        card.map { QuestionWithCallback(it as Answer) }

    fun getMeaning(): LiveData<MeaningWithShowedTrigger> =
        card.map { MeaningWithShowedTrigger(it as Meaning) }

    fun getMultiChoiceWithCallback(): LiveData<QuestionWithCallback<MultiChoice, String>> =
        card.map { QuestionWithCallback(it as MultiChoice) }

    fun getRememberWithCallback(): LiveData<QuestionWithCallback<Remember, Remember.Option>> =
        card.map { QuestionWithCallback(it as Remember) }

    fun getRememberRightWithCallback(): LiveData<QuestionWithCallback<RememberRight, RememberRight.Option>> =
        card.map { QuestionWithCallback(it as RememberRight) }

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