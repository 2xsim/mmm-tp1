package tech.abracadabra.mmmtp1

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var canAddOperation = false
    private var canCalculate = false
    private var isEqualPreviousClick = false
    private var reset = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberAction(view: View) {
        if (view is Button) {
            if (isEqualPreviousClick || reset) {
                output.text = view.text
            } else {
                output.append(view.text)
            }
            canAddOperation = true
            reset = false
        }
    }

    fun operationAction(view: View) {
        if (view is Button && canAddOperation) {
            if (!canCalculate) {
                output.append(view.text)
                canAddOperation = false
                canCalculate = true
            } else {
                output.text = autoCalculateResults()
                canAddOperation = false
            }
            isEqualPreviousClick = false
        }
    }

    fun allClearAction(view: View) {
        output.text = "0"
        println(output.text == "0")
        isEqualPreviousClick = false
        reset = true
    }

    fun backSpaceAction(view: View) {
        val length = output.length()

        if (length > 0) {
            if (length - 1 == 0) {
                output.text = "0"
                reset = true
            } else if (output.text.endsWith('+') || output.text.endsWith('−') ||
                output.text.endsWith('×') || output.text.endsWith('÷') || output.text.endsWith('%')
            ) {
                output.text = output.text.subSequence(0, length - 1)
                canCalculate = false
                canAddOperation = true
            } else
                output.text = output.text.subSequence(0, length - 1)
        }
        isEqualPreviousClick = false
    }

    fun equalsAction(view: View) {
        output.text = calculateResult()
        canCalculate = false
        isEqualPreviousClick = true
    }

    private fun calculateResult(): String {
        var digitsOperators = mutableListOf<Any>()
        var currentDigit = ""
        for (character in output.text) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else {
                digitsOperators.add(currentDigit.toFloat())
                currentDigit = ""
                digitsOperators.add(character)
            }
        }

        if (currentDigit != "")
            digitsOperators.add(currentDigit.toFloat())
        if (digitsOperators.isEmpty()) return ""

        var result = digitsOperators[0] as Float

        for (i in digitsOperators.indices) {
            if (digitsOperators[i] is Char && i != digitsOperators.lastIndex) {
                val operator = digitsOperators[i]
                val nextDigit = digitsOperators[i + 1] as Float
                if (operator == '+') {
                    result += nextDigit
                }
                if (operator == '−') {
                    result -= nextDigit
                }
                if (operator == '×') {
                    result *= nextDigit
                }
                if (operator == '÷') {
                    result /= nextDigit
                }
                if (operator == '%') {
                    result %= nextDigit
                }
            }
        }
        return stringifyResult(result)
    }

    private fun stringifyResult(result: Float): String {
        val stringResult = result.toString()
        val indexOfDot = stringResult.indexOf('.')

        if (stringResult.substring(indexOfDot) == ".0")
            return stringResult.substring(0, indexOfDot)
        else
            return stringResult
    }

    private fun stringifyResult(result: Float, operator: Any?): String {
        val stringResult = result.toString()
        val indexOfDot = stringResult.indexOf('.')
        if (stringResult.substring(indexOfDot) == ".0")
            return stringResult.substring(0, indexOfDot) + operator as Char
        else
            return stringResult + operator as Char
    }

    private fun autoCalculateResults(): String {
        val digitsOperators = mutableListOf<Any>()
        var currentDigit = ""
        for (character in output.text) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else {
                digitsOperators.add(currentDigit.toFloat())
                currentDigit = ""
                digitsOperators.add(character)
            }
        }

        if (currentDigit != "")
            digitsOperators.add(currentDigit.toFloat())
        if (digitsOperators.isEmpty()) return ""

        var result = digitsOperators[0] as Float

        var operatorParam: Any? = null

        for (i in digitsOperators.indices) {
            if (digitsOperators[i] is Char && i != digitsOperators.lastIndex) {
                val operator = digitsOperators[i]
                val nextDigit = digitsOperators[i + 1] as Float
                if (operator == '+') {
                    result += nextDigit
                }
                if (operator == '−') {
                    result -= nextDigit
                }
                if (operator == '×') {
                    result *= nextDigit
                }
                if (operator == '÷') {
                    result /= nextDigit
                }
                if (operator == '%') {
                    result %= nextDigit
                }
                operatorParam = operator as Char
            }
        }

        return stringifyResult(result, operatorParam)
    }

    private fun addSubtractCalculate(passedList: MutableList<Any>): Float {
        var result = passedList[0] as Float

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex) {
                val operator = passedList[i]
                val nextDigit = passedList[i + 1] as Float
                if (operator == '+') {
                    result += nextDigit
                    println("add")
                }
                if (operator == '−') {
                    result -= nextDigit
                    println("sous")
                }
            }
        }

        return result
    }

    private fun timesDivisionCalculate(passedList: MutableList<Any>): MutableList<Any> {
        var list = passedList
        while (list.contains('×') || list.contains('÷')) {
            list = calcTimesDiv(list)
        }
        return list
    }

    private fun calcTimesDiv(passedList: MutableList<Any>): MutableList<Any> {
        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size

        for (i in passedList.indices) {
            if (passedList[i] is Char && i != passedList.lastIndex && i < restartIndex) {
                val operator = passedList[i]
                val prevDigit = passedList[i - 1] as Int
                val nextDigit = passedList[i + 1] as Int
                when (operator) {
                    '×' -> {
                        newList.add(prevDigit * nextDigit)
                        restartIndex = i + 1
                    }
                    '÷' -> {
                        newList.add(prevDigit / nextDigit)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDigit)
                        newList.add(operator)
                    }
                }
            }

            if (i > restartIndex)
                newList.add(passedList[i])
        }

        return newList
    }

    private fun digitsOperators(): MutableList<Any> {
        val list = mutableListOf<Any>()
        var currentDigit = ""
        for (character in output.text) {
            if (character.isDigit() || character == '.')
                currentDigit += character
            else {
                list.add(currentDigit.toFloat())
                currentDigit = ""
                list.add(character)
            }
        }

        if (currentDigit != "")
            list.add(currentDigit.toFloat())

        return list
    }

}