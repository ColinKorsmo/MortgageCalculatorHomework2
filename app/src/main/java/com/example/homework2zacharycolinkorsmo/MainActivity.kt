package com.example.homework2zacharycolinkorsmo

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.NumberFormat
import kotlin.math.pow

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    MortgageCalculatorLayout()
                }
            }
        }
    }
}

@Composable
fun MortgageCalculatorLayout() {
    var loanAmountInput by remember { mutableStateOf("") }
    var interestRateInput by remember { mutableStateOf("") }
    var numberOfYearsInput by remember { mutableStateOf("") }
    var monthlyPayment by remember { mutableStateOf("$0.00") }

    val loanAmount = loanAmountInput.toIntOrNull() ?: 0
    val interestRate = interestRateInput.toDoubleOrNull() ?: 0.0
    val numberOfYears = numberOfYearsInput.toIntOrNull() ?: 0

    if (loanAmount > 0 && interestRate > 0 && numberOfYears > 0) {
        monthlyPayment = calculateMortgage(loanAmount, interestRate / 100 / 12, numberOfYears * 12)
    } else {
        monthlyPayment = "$0.00"
    }

    Column(
        modifier = Modifier
            .padding(40.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = stringResource(R.string.app_name),
            modifier = Modifier
                .padding(bottom = 16.dp)
                .align(Alignment.Start)
        )
        EditNumberField(
            label = R.string.loan_amount,
            leadingIcon = R.drawable.money,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = loanAmountInput,
            onValueChange = { loanAmountInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        EditNumberField(
            label = R.string.interest_rate,
            leadingIcon = R.drawable.percent,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Next
            ),
            value = interestRateInput,
            onValueChange = { interestRateInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        EditNumberField(
            label = R.string.number_of_years,
            leadingIcon = R.drawable.calendar,
            keyboardOptions = KeyboardOptions.Default.copy(
                keyboardType = KeyboardType.Number,
                imeAction = ImeAction.Done
            ),
            value = numberOfYearsInput,
            onValueChange = { numberOfYearsInput = it },
            modifier = Modifier
                .padding(bottom = 32.dp)
                .fillMaxWidth()
        )
        // Display the monthly payment
        Text(
            text = stringResource(R.string.monthly_payment, monthlyPayment),
            fontWeight = FontWeight.Bold,
            fontSize = 22.sp
        )
    }
}


@Composable
fun EditNumberField(
    @StringRes label: Int,
    @DrawableRes leadingIcon: Int,
    keyboardOptions: KeyboardOptions,
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    TextField(
        value = value,
        leadingIcon = { Icon(painter = painterResource(id = leadingIcon), null) },
        onValueChange = onValueChange,
        label = { Text(stringResource(label)) },
        singleLine = true,
        keyboardOptions = keyboardOptions,
        modifier = modifier
    )
}

private fun calculateMortgage(p: Int, r: Double, n: Int): String {

    if (n == 0) return "$0.00"

    val mortgage = p * ((r * (1.0 + r).pow(n)) / ((1.0 + r).pow(n) - 1))
    return NumberFormat.getCurrencyInstance().format(mortgage)
}

@Preview(showBackground = true)
@Composable
fun MortgageCalculatorLayoutPreview() {
    MaterialTheme {
        MortgageCalculatorLayout()
    }
}
