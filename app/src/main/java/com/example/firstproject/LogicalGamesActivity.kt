package com.example.firstproject

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity

data class Question(
    val text: String,
    val options: List<String>,
    val correctAnswerIndex: Int,
    val explanation: String
)

class LogicalGamesActivity : AppCompatActivity() {

    private val questions = listOf(
        Question(
            text = "1-сұрақ: Қай стакан бірінші толады?\n\n" +
                    "Төменде бір-бірімен жалғасқан түтікшелері бар 4 стакан берілген. Егер жоғарыдан су құйылса, қай стакан бірінші толады?",
            options = listOf("1-стақан", "2-стақан", "3-стақан", "4-стақан"),
            correctAnswerIndex = 2,
            explanation = "Кейбір түтіктер жабық, сондықтан су 3-стақанға бірінші жетеді."
        ),
        Question(
            text = "2-сұрақ: Қай көлеңке сәйкес келеді?\n\n" +
                    "Бала батпырауық ұстап тұр. Төменде оның 4 көлеңкесі берілген. Қай көлеңке шын бейнесіне сәйкес?",
            options = listOf("Көлеңке A", "Көлеңке B", "Көлеңке C", "Көлеңке D"),
            correctAnswerIndex = 1,
            explanation = "Бұл көлеңкеде бала мен батпырауықтың пішіні мен орналасуы дұрыс бейнеленген."
        ),
        Question(
            text = "3-сұрақ:\nТөмендегі суреттердің біреуі басқаларынан ерекшеленеді. Қайсысы артық және неге?",
            options = listOf("Үшбұрыш", "Шаршы", "Дөңгелек", "Тіктөртбұрыш"),
            correctAnswerIndex = 2,
            explanation = "Дөңгелек – бұрыштары жоқ, өзгелері көпбұрыштар."
        )
    )

    private var currentIndex = 0
    private var score = 0

    private lateinit var prefs: SharedPreferences
    private lateinit var questionTextView: TextView
    private lateinit var radioGroup: RadioGroup
    private lateinit var buttonNext: Button
    private lateinit var explanationTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logical_games)

        prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

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
