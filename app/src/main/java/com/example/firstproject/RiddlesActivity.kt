package com.example.firstproject


import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

data class Riddle(
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String = "" // можно оставить пустым или добавить пояснения
)

class RiddlesActivity : AppCompatActivity() {

    private val riddles = listOf(
        Riddle(
            text = "1-сұрақ:\nТүсі жоқ, бірақ бәрі оны көреді. Күнде пайда болады, бірақ ұстап болмайды. Бұл не?",
            options = listOf("Жел", "Көлеңке", "Су"),
            correctAnswerIndex = 1,
            explanation = "Дұрыс жауап: Көлеңке."
        ),
        Riddle(
            text = "2-сұрақ:\nБір жерде үш ағаш бар: алма, алмұрт және қарлыған. Бірақ тек бір ғана жеміс беретін ағаш бар. Қай ағаш?",
            options = listOf("Алма", "Алмұрт", "Қарлыған"),
            correctAnswerIndex = 2,
            explanation = "Дұрыс жауап: Қарлыған."
        ),
        Riddle(
            text = "3-сұрақ:\nЖеңіл де, салмақты да емес, бірақ суға түссе батады. Бұл не?",
            options = listOf("Ауа", "От", "Мұз"),
            correctAnswerIndex = 2,
            explanation = "Дұрыс жауап: Мұз."
        ),
        Riddle(
            text = "4-сұрақ:\nЕшқашан сөйлеспейді, бірақ бәрі оны тыңдайды. Бұл не?",
            options = listOf("Теледидар", "Кітап", "Радио"),
            correctAnswerIndex = 2,
            explanation = "Дұрыс жауап: Радио."
        ),
        Riddle(
            text = "5-сұрақ:\nТүнде жарық береді, күнде көрінбейді. Бұл не?",
            options = listOf("Ай", "Жұлдыз", "От"),
            correctAnswerIndex = 0,
            explanation = "Дұрыс жауап: Ай."
        )
    )

    private var currentIndex = 0
    private var score = 0

    private lateinit var prefs: android.content.SharedPreferences
    private lateinit var questionTextView: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var buttonNext: Button
    private lateinit var explanationTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_riddles)

        prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        questionTextView = findViewById(R.id.questionTextView)
        radioGroup = findViewById(R.id.radioGroup)
        buttonNext = findViewById(R.id.buttonNext)
        explanationTextView = findViewById(R.id.explanationTextView)

        showRiddle()

        buttonNext.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Жауап таңдаңыз", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val selectedIndex = radioGroup.indexOfChild(findViewById(selectedId))
            checkAnswer(selectedIndex)
        }
    }

    private fun showRiddle() {
        explanationTextView.visibility = View.GONE
        val riddle = riddles[currentIndex]
        questionTextView.text = riddle.text

        radioGroup.removeAllViews()
        for (option in riddle.options) {
            val rb = RadioButton(this)
            rb.text = option
            radioGroup.addView(rb)
        }
        radioGroup.clearCheck()
        buttonNext.text = "Жауапты тексеру"
    }

    private fun checkAnswer(selectedIndex: Int) {
        val riddle = riddles[currentIndex]

        if (selectedIndex == riddle.correctAnswerIndex) {
            score++
            Toast.makeText(this, "Дұрыс!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Қате!", Toast.LENGTH_SHORT).show()
        }

        explanationTextView.text = riddle.explanation
        explanationTextView.visibility = View.VISIBLE

        buttonNext.text = if (currentIndex == riddles.size - 1) "Нәтиже" else "Келесі сұрақ"

        buttonNext.setOnClickListener {
            currentIndex++
            if (currentIndex < riddles.size) {
                showRiddle()
                buttonNext.setOnClickListener {
                    val selectedId = radioGroup.checkedRadioButtonId
                    if (selectedId == -1) {
                        Toast.makeText(this, "Жауап таңдаңыз", Toast.LENGTH_SHORT).show()
                        return@setOnClickListener
                    }
                    val selectedIndex = radioGroup.indexOfChild(findViewById(selectedId))
                    checkAnswer(selectedIndex)
                }
            } else {
                showResult()
            }
        }
    }

    private fun showResult() {
        // Жинаған ұпайды жалпы жинаққа қосу
        val totalScore = prefs.getInt("total_score", 0) + score
        prefs.edit().putInt("total_score", totalScore).apply()

        questionTextView.text = "Тест аяқталды! Дұрыс жауаптар саны: $score из ${riddles.size}."
        radioGroup.visibility = View.GONE
        buttonNext.visibility = View.GONE
        explanationTextView.visibility = View.GONE
    }
}
