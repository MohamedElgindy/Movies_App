package com.example.movies_app.util

import android.content.Context
import android.graphics.Color
import androidx.core.content.ContextCompat
import com.example.movies_app.R
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry

fun setupPieChart(context: Context, pieChart: PieChart, movieRate: Double) {
    val pieEntries = arrayListOf<PieEntry>()
    pieEntries.add(PieEntry(movieRate.toFloat()))
    pieEntries.add(PieEntry(100 - movieRate.toFloat()))
    val pieDataSet = PieDataSet(pieEntries, null)

    when {
        movieRate >= 70 -> {
            pieDataSet.setColors(
                ContextCompat.getColor(context, R.color.green),
                ContextCompat.getColor(context, R.color.darkGreen),
            )
        }
        movieRate >= 40 -> {
            pieDataSet.setColors(
                ContextCompat.getColor(context, R.color.golden),
                ContextCompat.getColor(context, R.color.darkGolden),
            )
        }
        else -> {
            pieDataSet.setColors(
                ContextCompat.getColor(context, R.color.purple),
                ContextCompat.getColor(context, R.color.darkPurple),
            )
        }

    }
    // Setup Pie Chart Animation
    pieChart.animateXY(1000, 1000) // This 1000 is time that how much time piechart chreated


    // Setup Pie Data Set in PieData
    val pieData = PieData(pieDataSet)
    pieData.setDrawValues(false)     // This is for values in pie entries.
    pieData.isHighlightEnabled = false

    pieChart.description.isEnabled = false
    pieChart.isRotationEnabled = false
    pieChart.setCenterTextColor(Color.WHITE)
    pieChart.centerText = "${movieRate.toInt()}%"
    pieChart.setHoleColor(ContextCompat.getColor(context, R.color.lightBlack))

    pieChart.setCenterTextSize(16f)
    pieChart.legend.isEnabled = true

    pieChart.isDrawHoleEnabled = true
    pieChart.holeRadius = 85f

    pieChart.legend.isEnabled = false
    pieChart.description.isEnabled = false

    // Finally Setup the add Values in PieChart.
    pieChart.data = pieData
}
