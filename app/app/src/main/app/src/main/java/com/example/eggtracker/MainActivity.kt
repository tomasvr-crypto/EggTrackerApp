package com.example.eggtracker

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardOptions
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.edit
import java.text.NumberFormat
import java.util.Locale

class MainActivity : ComponentActivity() {
    private val PREFS = "eggtracker_prefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val prefs = getSharedPreferences(PREFS, Context.MODE_PRIVATE)

                    // vstupy
                    val hens = remember { mutableStateOf(prefs.getInt("hens", 10)) }
                    val dailyEggsPerHen = remember { mutableStateOf(prefs.getFloat("dailyEggsPerHen", 0.8f)) }

                    val feedCost = remember { mutableStateOf(prefs.getFloat("feedCost", 0f)) }
                    val beddingCost = remember { mutableStateOf(prefs.getFloat("beddingCost", 0f)) }
                    val otherCost = remember { mutableStateOf(prefs.getFloat("otherCost", 0f)) }

                    val eggsSoldPerMonth = remember { mutableStateOf(prefs.getInt("eggsSoldPerMonth", 0)) }
                    val eggPrice = remember { mutableStateOf(prefs.getFloat("eggPrice", 0.0f)) }

                    val hensSold = remember { mutableStateOf(prefs.getInt("hensSold", 0)) }
                    val henSalePrice = remember { mutableStateOf(prefs.getFloat("henSalePrice", 0.0f)) }

                    val scroll = rememberScrollState()

                    // výpočty
                    val avgDailyLay = remember { mutableStateOf(0.0) }
                    val monthlyEggsProduced = remember { mutableStateOf(0) }
                    val totalExpenses = remember { mutableStateOf(0.0) }
                    val incomeFromEggs = remember { mutableStateOf(0.0) }
                    val incomeFromHens = remember { mutableStateOf(0.0) }
                    val monthlyBalance = remember { mutableStateOf(0.0) }

                    fun recalc() {
                        avgDailyLay.value = hens.value * dailyEggsPerHen.value
                        monthlyEggsProduced.value = (avgDailyLay.value * 30).toInt()
                        totalExpenses.value = (feedCost.value + beddingCost.value + other
