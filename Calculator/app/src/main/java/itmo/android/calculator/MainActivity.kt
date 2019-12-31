package itmo.android.calculator

import kotlinx.android.synthetic.main.activity_main.*
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {
    private var line = ""
    private var position = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val buttons = listOf(
            one, two, three, four, five, six, seven, eight, nine, zero,
            add, sub, multiply, divide, dot, leftBracket, rightBracket, clear
        )
        buttons.forEach {
            val a = it
            it.setOnClickListener {
                expression.append(a.text)
            }
        }
        answer.setOnClickListener {
            finalAnswer.text = start(expression.text.toString()).toString()
        }
        clear.setOnClickListener {
            expression.text = expression.text.dropLast(1)
        }
        clearAll.setOnClickListener {
            expression.text = ""
        }
    }

    private fun start(toCalculate: String): Double {
        if (toCalculate.isEmpty())
            return 0.0
        line = toCalculate
        position = 0
        return calculate()
    }

    private fun calculate(): Double {
        var expr = ""
        var firstNumber = 0.0
        var secondNumber: Double
        if (line[position].isDigit()) {
            firstNumber = parseNumber()
        } else if (line[position] == '(') {
            ++position
            firstNumber = calculate()
        }
        while (position != line.length && line[position] != ')') {
            val oldPos = position++
            secondNumber = if (line[position].isDigit()) {
                parseNumber()
            } else {
                ++position
                calculate()
            }
            if (line[oldPos] == '+' || line[oldPos] == '-') {
                if (firstNumber < 0.0 && expr.isNotEmpty()) {
                    when (expr[expr.lastIndex]) {
                        '+' -> expr = expr.dropLast(1) + "-"
                        '-' -> expr = expr.dropLast(1) + "+"
                    }
                    firstNumber *= -1
                }
                expr = expr + firstNumber.toString() + line[oldPos]
                firstNumber = secondNumber
            } else {
                when (line[oldPos]) {
                    '*' -> firstNumber *= secondNumber
                    '/' -> if (secondNumber != 0.0) firstNumber /= secondNumber else
                        firstNumber = 0.0
                }
            }
        }
        if (position != line.length && line[position] == ')')
            ++position
        if (firstNumber < 0.0 && expr.isNotEmpty()) {
            when (expr[expr.lastIndex]) {
                '+' -> expr = expr.dropLast(1) + "-"
                '-' -> expr = expr.dropLast(1) + "+"
            }
            firstNumber *= -1
        }
        expr += firstNumber.toString()
        return calculateSimple(expr)
    }

    private fun parseNumber(): Double {
        var temp = ""
        while (Character.isDigit(line[position]) || line[position] == '.') {
            temp += line[position++]
            if (position == line.length)
                break
        }
        return temp.toDouble()
    }

    private fun calculateSimple(toCalculate: String): Double {
        var pos = 0
        var number = 0.0
        if (toCalculate[pos].isDigit()) {
            var temp = ""
            while (Character.isDigit(toCalculate[pos]) || toCalculate[pos] == '.') {
                temp += toCalculate[pos++]
                if (pos == toCalculate.length)
                    break
            }
            number = temp.toDouble()
        }
        while (pos != toCalculate.length) {
            val oldPos = pos++
            var temp = ""
            while (Character.isDigit(toCalculate[pos]) || toCalculate[pos] == '.') {
                temp += toCalculate[pos++]
                if (pos == toCalculate.length)
                    break
            }
            if (toCalculate[oldPos] == '+') {
                number += temp.toDouble()
            } else {
                number -= temp.toDouble()
            }
        }
        return number
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putCharSequence("expression", expression.text)
        outState.putCharSequence("answer", finalAnswer.text)
        super.onSaveInstanceState(outState)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        expression.text = savedInstanceState.getCharSequence("expression")
        finalAnswer.text = savedInstanceState.getCharSequence("answer")
    }
}
