package ru.egoncharovsky.words.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import mu.KotlinLogging
import ru.egoncharovsky.words.domain.quiz.QuizManager
import ru.egoncharovsky.words.domain.quiz.card.*
import ru.egoncharovsky.words.repository.StudyListRepository

class QuizViewModel : ViewModel() {

    private val logger = KotlinLogging.logger {}

    private lateinit var manager: QuizManager
    private lateinit var nextCard: () -> Card?

    private val card = MutableLiveData<Card>()
    private val answerIsCorrect = MutableLiveData<Boolean?>()
    private val nextIsVisible = MutableLiveData<Boolean>()
    private val finished = MutableLiveData<Boolean>()
    private val progress = MutableLiveData<Int>()

    fun getCard(): LiveData<Card> = card

    fun getAnswerModel(): LiveData<QuestionWithCallback<Answer, String>> =
        Transformations.map(card) { QuestionWithCallback(it as Answer) }

    fun getMeaningModel(): LiveData<MeaningWithShowedTrigger> =
        Transformations.map(card) { MeaningWithShowedTrigger(it as Meaning) }

    fun getMultiChoiceModel(): LiveData<QuestionWithCallback<MultiChoice, String>> =
        Transformations.map(card) { QuestionWithCallback(it as MultiChoice) }

    fun getRememberModel(): LiveData<QuestionWithCallback<Remember, Remember.Option>> =
        Transformations.map(card) { QuestionWithCallback(it as Remember) }

    fun getRememberRightModel(): LiveData<QuestionWithCallback<RememberRight, RememberRight.Option>> =
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

        val studyList = StudyListRepository.get(studyListId)
        logger.trace("Study list loaded: $studyList")

        val words = studyList.words.toSet()
        logger.trace("Total:${words.size} words:$words")

        manager = QuizManager(words)
        card.value = manager.start()
        progress.value = manager.progressPercentage()
    }
}