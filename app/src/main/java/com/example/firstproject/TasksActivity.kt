package com.example.firstproject

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

data class TaskQuestion(
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

class TasksActivity : AppCompatActivity() {

    private val questions = listOf(
        TaskQuestion(
            text = "1. Геометрия: Қай фигураның ауданы үлкен?\n" +
                    "Тік төртбұрыш — 6см × 4см, үшбұрыш — табаны 6см, биіктігі 4см.",
            options = listOf("A) Тік төртбұрыш", "B) Үшбұрыш", "C) Аудандары тең"),
            correctAnswerIndex = 0,
            explanation = "Тік төртбұрыш ауданы: 24см², үшбұрыш ауданы: 12см²."
        ),
        TaskQuestion(
            text = "2. Биология: Әр жануардың мекен ортасын сәйкестендіріңіз.\n" +
                    "Қасқыр, Түлкі, Арыстан, Елік",
            options = listOf(
                "A) Қасқыр — Орман",
                "B) Түлкі — Далалы аймақ",
                "C) Арыстан — Саванна",
                "D) Елік — Орман"
            ),
            correctAnswerIndex = 0,
            explanation = "Қасқыр - Орман, Түлкі - Далалы аймақ, Арыстан - Саванна, Елік - Орман."
        ),
        TaskQuestion(
            text = "3. Қазақ тілі: Артық сөзді табыңыз.\n" +
                    "Дәптер, Кітап, Қалам, Алма",
            options = listOf("A) Дәптер", "B) Кітап", "C) Қалам", "D) Алма"),
            correctAnswerIndex = 3,
            explanation = "Алма — жеміс, қалғандары мектеп құралдары."
        )
    )

    private var currentIndex = 0
    private var score = 0

    private lateinit var questionTextView: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var buttonNext: Button
    private lateinit var explanationTextView: TextView

    private lateinit var prefs: android.content.SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tasks)

        prefs = getSharedPreferences("app_prefs", MODE_PRIVATE)

        questionTextView = findViewById(R.id.questionTextView)
        radioGroup = findViewById(R.id.radioGroup)
        buttonNext = findViewById(R.id.buttonNext)
        explanationTextView = findViewById(R.id.explanationTextView)

        showQuestion()

        buttonNext.setOnClickListener {
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId == -1) {
                Toast.makeText(this, "Жауабыңызды таңдаңыз", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val selectedIndex = radioGroup.indexOfChild(findViewById(selectedId))
            checkAnswer(selectedIndex)
        }
    }

    private fun showQuestion() {
        explanationTextView.visibility = View.GONE
        val q = questions[currentIndex]
        questionTextView.text = q.text

        radioGroup.removeAllViews()
        for (option in q.options) {
            val radioButton = RadioButton(this)
            radioButton.text = option
            radioGroup.addView(radioButton)
        }
        radioGroup.clearCheck()
        buttonNext.text = "Жауапты тексеру"
    }

    private fun checkAnswer(selectedIndex: Int) {
        val q = questions[currentIndex]

        if (selectedIndex == q.correctAnswerIndex) {
            score++
            Toast.makeText(this, "Дұрыс!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Қате!", Toast.LENGTH_SHORT).show()
        }

        explanationTextView.text = "Түсіндірме: ${q.explanation}"
        explanationTextView.visibility = View.VISIBLE

        buttonNext.text = if (currentIndex == questions.size - 1) "Қорытынды" else "Келесі сұрақ"

        buttonNext.setOnClickListener {
            currentIndex++
            if (currentIndex < questions.size) {
                showQuestion()
                buttonNext.setOnClickListener {
                    val selectedId = radioGroup.checkedRadioButtonId
                    if (selectedId == -1) {
                        Toast.makeText(this, "Жауабыңызды таңдаңыз", Toast.LENGTH_SHORT).show()
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
        val previousScore = prefs.getInt("total_score", 0)
        val newTotalScore = previousScore + score
        prefs.edit().putInt("total_score", newTotalScore).apply()

        questionTextView.text = "Тест аяқталды! Сіз $score/${questions.size} дұрыс жауап бердіңіз.\n" +
                "Жалпы балл: $newTotalScore"

        radioGroup.visibility = View.GONE
        buttonNext.visibility = View.GONE
        explanationTextView.visibility = View.GONE
    }

}
