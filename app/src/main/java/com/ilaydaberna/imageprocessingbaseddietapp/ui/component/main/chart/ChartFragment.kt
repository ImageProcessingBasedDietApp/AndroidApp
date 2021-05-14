package com.ilaydaberna.imageprocessingbaseddietapp.ui.component.main.chart

import android.graphics.Color
import android.graphics.DashPathEffect
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.XAxis.XAxisPosition
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.utils.ColorTemplate
import com.github.mikephil.charting.utils.Utils
import com.google.firebase.auth.FirebaseUser
import com.ilaydaberna.imageprocessingbaseddietapp.R
import com.ilaydaberna.imageprocessingbaseddietapp.databinding.FragmentChartBinding
import com.ilaydaberna.imageprocessingbaseddietapp.model.firebase.*


class ChartFragment : Fragment() {

    private var _binding: FragmentChartBinding? = null
    private val binding get() = _binding!!
    val currentUser: FirebaseUser? = FirebaseSource().getAuth().currentUser

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
        createWeightLineChart(binding.weightLineChart)


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

    private fun createWeightLineChart(mChart: LineChart) {
        var set1: LineDataSet
        val weightsValues = arrayListOf<Entry>()
        val weightXAxisValues = arrayListOf<String>()
        mChart.setTouchEnabled(true)
        mChart.setPinchZoom(false)
        mChart.description.isEnabled = false

        if (currentUser != null) {
            FirestoreSource().getWeightTrackingValues(currentUser, object : GetUserWeightTrackigCallBack {
                override fun onCallback(userWeights: ArrayList<WeightTrackValue>) {
                    var i = 0F
                    for (value in userWeights) {
                        weightsValues.add(Entry(i, value.weight))
                        weightXAxisValues.add(value.date)
                        i += 1F
                    }

                    mChart.xAxis.valueFormatter = IndexAxisValueFormatter(weightXAxisValues)
                    mChart.xAxis.position = XAxis.XAxisPosition.BOTTOM

                    set1 = LineDataSet(weightsValues, "Kilo")
                    set1.setDrawIcons(false)
                    set1.enableDashedLine(10f, 5f, 0f)
                    set1.enableDashedHighlightLine(10f, 5f, 0f)
                    set1.color = Color.DKGRAY
                    set1.setCircleColor(Color.DKGRAY)
                    set1.lineWidth = 1f
                    set1.circleRadius = 3f
                    set1.setDrawCircleHole(false)
                    set1.valueTextSize = 9f
                    set1.setDrawFilled(true)
                    set1.formLineWidth = 1f
                    set1.formLineDashEffect = DashPathEffect(floatArrayOf(10f, 5f), 0f)
                    set1.formSize = 15f

                    if (Utils.getSDKInt() >= 18) {
                        set1.fillDrawable = resources.getDrawable(R.drawable.fade_green)
                    } else {
                        set1.fillColor = Color.DKGRAY
                    }
                    val dataSets: ArrayList<ILineDataSet> = ArrayList()
                    dataSets.add(set1)
                    val data = LineData(dataSets)
                    mChart.data = data
                }
            })
        }
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

        if (chart.data != null && chart.data.dataSetCount > 0) {
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

    private fun getWaterChartData(seekBarValue: Int): ArrayList<BarEntry> {
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

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as BarDataSet
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

    private fun getCoffeeChartData(seekBarValue: Int): ArrayList<BarEntry> {
        val values = arrayListOf<BarEntry>()
        for (i in 1..seekBarValue) {
            val multi = 20f
            val `val` = (Math.random() * multi).toFloat() + multi / 3
            values.add(BarEntry(i.toFloat(), `val`))
        }
        return values
    }

    private fun createTeaBarChart(chart: BarChart, seekBarValue: Int) {
        var set1: BarDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as BarDataSet
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

    private fun getTeaChartData(seekBarValue: Int): ArrayList<BarEntry> {
        val values = arrayListOf<BarEntry>()
        for (i in 1..seekBarValue) {
            val multi = 20f
            val `val` = (Math.random() * multi).toFloat() + multi / 3
            values.add(BarEntry(i.toFloat(), `val`))
        }
        return values
    }

    private fun createStepBarChart(chart: BarChart, seekBarValue: Int) {
        var set1: BarDataSet

        if (chart.data != null && chart.data.dataSetCount > 0) {
            set1 = chart.data.getDataSetByIndex(0) as BarDataSet
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

    private fun getStepChartData(seekBarValue: Int): ArrayList<BarEntry> {
        val values = arrayListOf<BarEntry>()
        for (i in 1..seekBarValue) {
            val multi = 20f
            val `val` = (Math.random() * multi).toFloat() + multi / 3
            values.add(BarEntry(i.toFloat(), `val`))
        }

        return values
    }
}
