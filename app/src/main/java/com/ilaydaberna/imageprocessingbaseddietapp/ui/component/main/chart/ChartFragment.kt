package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.chart

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.FragmentChartBinding


class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentChartBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initializeBarChart(binding.waterBarChart, binding.waterSeekBar)
        initializeBarChart(binding.coffeeBarChart, binding.coffeeSeekBar)
        initializeBarChart(binding.teaBarChart, binding.teaSeekBar)
        initializeBarChart(binding.stepBarChart, binding.stepSeekBar)

        binding.waterSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                // write custom code for progress is changed
                createWaterBarChart(binding.waterBarChart, progress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
            // write custom code for progress is stopped
            }
        })

        binding.coffeeSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                // write custom code for progress is changed
                createCoffeeBarChart(binding.coffeeBarChart, progress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
            // write custom code for progress is stopped
            }
        })

        binding.teaSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                // write custom code for progress is changed
                createTeaBarChart(binding.teaBarChart, progress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
            // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
            // write custom code for progress is stopped
            }
        })

        binding.stepSeekBar.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(
                seek: SeekBar,
                progress: Int, fromUser: Boolean
            ) {
                // write custom code for progress is changed
                createStepBarChart(binding.stepBarChart, progress)
            }

            override fun onStartTrackingTouch(seek: SeekBar) {
                // write custom code for progress is started
            }

            override fun onStopTrackingTouch(seek: SeekBar) {
                // write custom code for progress is stopped
            }
        })
    }
}

    private fun initializeLineChart(chart: LineChart){

    }

    private fun createWeightLineChart(chart: LineChart){

    }

    private fun initializeBarChart(chart: BarChart, seekBarX: SeekBar) {
        chart.description.isEnabled = false

        // if more than 60 entries are displayed in the chart, no values will be
        // drawn
        chart.setMaxVisibleValueCount(60)

        // scaling can now only be done on x- and y-axis separately
        chart.setPinchZoom(false)
        chart.setDrawBarShadow(false)
        chart.setDrawGridBackground(false)
        val xAxis = chart.xAxis
        xAxis.position = XAxisPosition.BOTTOM
        xAxis.setDrawGridLines(false)
        chart.axisLeft.setDrawGridLines(false)

        // setting data
        seekBarX.setProgress(10)
        seekBarX.max = 30
        seekBarX.min = 3

        // add a nice and smooth animation
        chart.animateY(1500)
        chart.legend.isEnabled = false
    }

    private fun createWaterBarChart(chart: BarChart, seekBarValue: Int) {
        var set1: BarDataSet

        if (chart.getData() != null &&
            chart.getData().getDataSetCount() > 0) {
            set1 = chart.getData().getDataSetByIndex(0) as BarDataSet
            set1.values = getWaterChartData(seekBarValue)
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(getWaterChartData(seekBarValue), "Data Set")
            set1.setColors(*ColorTemplate.PASTEL_COLORS)
            set1.setDrawValues(false)
            val dataSets = java.util.ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            chart.setData(data)
            chart.setFitBars(true)
        }

        chart.invalidate()
    }

    private fun getWaterChartData(seekBarValue: Int): ArrayList<BarEntry>{
        val values = java.util.ArrayList<BarEntry>()
        for (i in 1..seekBarValue) {
            val multi = 20f
            val `val` = (Math.random() * multi).toFloat() + multi / 3
            values.add(BarEntry(i.toFloat(), `val`))
        }
        return values
    }

    private fun createCoffeeBarChart(chart: BarChart, seekBarValue: Int) {
        var set1: BarDataSet

        if (chart.getData() != null &&
            chart.getData().getDataSetCount() > 0) {
            set1 = chart.getData().getDataSetByIndex(0) as BarDataSet
            set1.values = getCoffeeChartData(seekBarValue)
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(getCoffeeChartData(seekBarValue), "Data Set")
            set1.setColors(*ColorTemplate.PASTEL_COLORS)
            set1.setDrawValues(false)
            val dataSets = java.util.ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            chart.setData(data)
            chart.setFitBars(true)
        }

        chart.invalidate()
    }

    private fun getCoffeeChartData(seekBarValue: Int): ArrayList<BarEntry>{
        val values = java.util.ArrayList<BarEntry>()
        for (i in 1..seekBarValue) {
            val multi = 20f
            val `val` = (Math.random() * multi).toFloat() + multi / 3
            values.add(BarEntry(i.toFloat(), `val`))
        }
        return values
    }

    private fun createTeaBarChart(chart: BarChart, seekBarValue: Int) {
        var set1: BarDataSet

        if (chart.getData() != null &&
            chart.getData().getDataSetCount() > 0) {
            set1 = chart.getData().getDataSetByIndex(0) as BarDataSet
            set1.values = getTeaChartData(seekBarValue)
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(getTeaChartData(seekBarValue), "Data Set")
            set1.setColors(*ColorTemplate.PASTEL_COLORS)
            set1.setDrawValues(false)
            val dataSets = java.util.ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            chart.setData(data)
            chart.setFitBars(true)
        }

        chart.invalidate()
    }

    private fun getTeaChartData(seekBarValue: Int): ArrayList<BarEntry>{
        val values = java.util.ArrayList<BarEntry>()
        for (i in 1..seekBarValue) {
            val multi = 20f
            val `val` = (Math.random() * multi).toFloat() + multi / 3
            values.add(BarEntry(i.toFloat(), `val`))
        }
        return values
    }

    private fun createStepBarChart(chart: BarChart, seekBarValue: Int) {
        var set1: BarDataSet

        if (chart.getData() != null &&
            chart.getData().getDataSetCount() > 0) {
            set1 = chart.getData().getDataSetByIndex(0) as BarDataSet
            set1.values = getStepChartData(seekBarValue)
            chart.getData().notifyDataChanged()
            chart.notifyDataSetChanged()
        } else {
            set1 = BarDataSet(getStepChartData(seekBarValue), "Data Set")
            set1.setColors(*ColorTemplate.PASTEL_COLORS)
            set1.setDrawValues(false)
            val dataSets = java.util.ArrayList<IBarDataSet>()
            dataSets.add(set1)
            val data = BarData(dataSets)
            chart.setData(data)
            chart.setFitBars(true)
        }

        chart.invalidate()
    }

    private fun getStepChartData(seekBarValue: Int): ArrayList<BarEntry>{
        val values = java.util.ArrayList<BarEntry>()
        for (i in 1..seekBarValue) {
            val multi = 20f
            val `val` = (Math.random() * multi).toFloat() + multi / 3
            values.add(BarEntry(i.toFloat(), `val`))
        }
        return values
    }


