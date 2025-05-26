package com.example.firstproject
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.firstproject.R
import android.widget.Button
import android.widget.ImageView
import android.content.Context





data class ImageQuestion(
    val text: String,
    val imageOptions: List<Int>, // Drawable resources
    val correctAnswerIndex: Int,
    val explanation: String
)

class LogicalGamesActivity : AppCompatActivity() {

    private val questions = listOf(
        ImageQuestion(
            text = "Қай көлеңке сәйкес келеді?",
            imageOptions = listOf(
                R.drawable.shadow_a,
                R.drawable.shadow_b,
                R.drawable.shadow_c,),
            correctAnswerIndex = 1,
            explanation = "B көлеңкесі пішініне сай."
        ),
    )

    private var currentIndex = 0
    private var score = 0
    private lateinit var prefs: SharedPreferences

    private lateinit var questionTextView: TextView
    private lateinit var optionsContainer: LinearLayout
    private lateinit var buttonNext: Button
    private lateinit var explanationTextView: TextView

    private var selectedAnswer = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_logical_games)

        prefs = getSharedPreferences("app_prefs", Context.MODE_PRIVATE)

        questionTextView = findViewById(R.id.questionTextView)
        optionsContainer = findViewById(R.id.optionsContainer)
        buttonNext = findViewById(R.id.buttonNext)
        explanationTextView = findViewById(R.id.explanationTextView)

        showQuestion()

        buttonNext.setOnClickListener {
            if (selectedAnswer == -1) {
                Toast.makeText(this, "Жауапты таңдаңыз", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            checkAnswer()
        }
    }

    private fun showQuestion() {
        selectedAnswer = -1
        explanationTextView.visibility = View.GONE
        val q = questions[currentIndex]
        questionTextView.text = q.text
        optionsContainer.removeAllViews()

        for ((index, imageRes) in q.imageOptions.withIndex()) {
            val imageView = ImageView(this).apply {
                setImageResource(imageRes)
                layoutParams = LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.MATCH_PARENT, 300
                ).apply {
                    setMargins(0, 8, 0, 8)
                }
                scaleType = ImageView.ScaleType.CENTER_CROP
                setBackgroundResource(android.R.drawable.dialog_holo_light_frame)
                setPadding(8, 8, 8, 8)
                setOnClickListener {
                    selectedAnswer = index
                    highlightSelected(index)
                }
            }
            optionsContainer.addView(imageView)
        }

        buttonNext.text = "Жауапты тексеру"
    }

    private fun highlightSelected(selectedIndex: Int) {
        for (i in 0 until optionsContainer.childCount) {
            val child = optionsContainer.getChildAt(i)
            child.alpha = if (i == selectedIndex) 1.0f else 0.5f
        }
    }

    private fun checkAnswer() {
        val q = questions[currentIndex]

        if (selectedAnswer == q.correctAnswerIndex) {
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

        optionsContainer.visibility = View.GONE
        buttonNext.visibility = View.GONE
        explanationTextView.visibility = View.GONE
    }
}
