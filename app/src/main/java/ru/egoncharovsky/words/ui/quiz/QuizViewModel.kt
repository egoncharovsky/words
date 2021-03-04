package ru.egoncharovsky.words.ui.quiz

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import ru.egoncharovsky.words.domain.Word
import ru.egoncharovsky.words.domain.quiz.QuizManager
import ru.egoncharovsky.words.domain.quiz.card.*

class QuizViewModel : ViewModel() {

    private val dictionary = listOf(
        Word("apple", "яблоко"),
        Word("any", "любой"),
        Word("you", "ты"),
        Word("I", "я"),
        Word("many", "много"),
        Word("love", "любить"),
        Word("TV", "телевизор"),
        Word("shift", "сдвиг"),
        Word("weather", "погода"),
        Word("translation", "перевод"),
        Word("nice", "приятный"),
        Word("very", "очень"),
        Word("Greece", "Греция"),
        Word("cool", "круто"),
        Word("cold", "холодно"),
        Word("hot", "горячо"),
        Word("hungry", "голодный"),
        Word("breakfast", "завтра"),
    )

    private val quizManager: QuizManager = QuizManager(dictionary.toSet())
    private var nextCard: () -> Card? = { quizManager.start() }

    private val card = MutableLiveData<Card>().apply {
        value = nextCard()
    }
    private val nextIsVisible = MutableLiveData<Boolean>()
    private val finished = MutableLiveData<Boolean>()

    fun getCard(): LiveData<Card> = card

    fun getAnswerModel(): LiveData<QuestionWithCallback<Answer, String>> = Transformations.map(card) { QuestionWithCallback(it as Answer) }
    fun getMeaningModel(): LiveData<MeaningWithShowedTrigger> = Transformations.map(card) { MeaningWithShowedTrigger(it as Meaning) }
    fun getMultiChoiceModel(): LiveData<QuestionWithCallback<MultiChoice, String>> = Transformations.map(card) { QuestionWithCallback(it as MultiChoice) }
    fun getRememberModel(): LiveData<QuestionWithCallback<Remember, Remember.Option>> = Transformations.map(card) { QuestionWithCallback(it as Remember) }

    fun getNextVisibility(): LiveData<Boolean> = nextIsVisible
    fun getFinished(): LiveData<Boolean> = finished

    inner class QuestionWithCallback<Q : Question<A>, A>(val question: Q) {
        fun sendAnswer(value: A) {
            nextCard = {
                if (quizManager.hasNext()) quizManager.next(question, value) else null
            }
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
            nextCard = {
                if (quizManager.hasNext()) quizManager.next(meaning) else null
            }
            nextIsVisible.value = true
        }
    }

    fun clickNext() {
        val next = nextCard()
        if (next != null) {
            nextIsVisible.value = false
            card.value = next
        } else {
            finished.value = true
        }
    }
}