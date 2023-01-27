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
            } else if (view.text == "0") {
            } else
                output.append(view.text)
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
                output.text = autoCalculateResults(view.text[0])
                canAddOperation = false
            }
            isEqualPreviousClick = false
        }
    }

    fun allClearAction(view: View) {
        output.text = "0"
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
        var isFirstIteration = true
        for (character in output.text) {
            if (isFirstIteration) {
                if (character.isDigit() || character == '.' || character == '-')
                    currentDigit += character
                else {
                    digitsOperators.add(currentDigit.toFloat())
                    currentDigit = ""
                    digitsOperators.add(character)
                }
                isFirstIteration = false
            } else {
                if (character.isDigit() || character == '.')
                    currentDigit += character
                else {
                    digitsOperators.add(currentDigit.toFloat())
                    currentDigit = ""
                    digitsOperators.add(character)
                }
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

    private fun autoCalculateResults(nextOperator: Char): String {
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

        return stringifyResult(result, nextOperator)
    }

    fun negAction(view: View) {
        if (view is Button) {
            if (canAddOperation && !reset) {
                val outputText = output.text
                println(outputText[0])
                if (outputText[0] == '-') {
                    output.text = outputText.substring(1)
                } else {
                    val outputText = output.text
                    val newOut = "-$outputText"
                    output.text = newOut
                }
            }
        }
    }

}