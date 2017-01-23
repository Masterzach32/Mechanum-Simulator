package net.masterzach32.simulator

import java.awt.Dimension
import java.awt.FlowLayout
import java.awt.GridLayout

import java.util.*
import javax.swing.*

class ConfigPanel : JPanel() {

    val labels = ArrayList<JLabel>()
    val sliders = ArrayList<JSlider>()

    val driveGroup = ButtonGroup()
    val modeGroup = ButtonGroup()

    var driveMode = DriveMode.CARTESIAN
    var mode = Mode.ABSOLUTE

    init {
        layout = GridLayout(6, 2, 10, 0)

        labels.add(JLabel("Left Pitch: 0.0"))
        labels.add(JLabel("Left Roll: 0.0"))
        labels.add(JLabel("Right Pitch: 0.0"))
        labels.add(JLabel("Right Roll: 0.0"))
        labels.add(JLabel("Gyro Heading: 0.0"))

        for (i in labels.indices) {
            sliders.add(JSlider(-100, 100, 0))
            sliders[i].majorTickSpacing = 10
            sliders[i].minorTickSpacing = 5
            sliders[i].paintTicks = true
            sliders[i].snapToTicks = true

            add(sliders[i])
            add(labels[i])

            sliders[i].addChangeListener {
                val str: String
                when (i) {
                    0 -> str = "Left Pitch"
                    1 -> str = "Left Roll"
                    2 -> str = "Right Pitch"
                    3 -> str = "Right Roll"
                    4 -> str = "Gyro Heading"
                    else -> str = "null"
                }
                var sliderValue = sliders[i].value.toDouble()
                if (i != 4)
                    sliderValue /= 100

                labels[i].text = "$str: $sliderValue"

                updateSpeedVectors()
            }
        }

        val cartesian: JRadioButton = JRadioButton("Cartesian Drive Mode")
        cartesian.addActionListener { updateDriveMode(DriveMode.CARTESIAN) }
        val polar: JRadioButton = JRadioButton("Polar Drive Mode")
        polar.addActionListener { updateDriveMode(DriveMode.POLAR) }
        val direct: JRadioButton = JRadioButton("Direct Drive Mode")
        direct.addActionListener { updateDriveMode(DriveMode.DIRECT) }

        val absolute = JRadioButton("Absolute Drive")
        absolute.addActionListener { updateMode(Mode.ABSOLUTE) }
        val relative = JRadioButton("Relative Drive")
        relative.addActionListener { updateMode(Mode.RELATIVE) }

        val drivePanel = JPanel()
        drivePanel.layout = FlowLayout(FlowLayout.LEFT, 0, 0)
        cartesian.isSelected = true
        driveGroup.add(cartesian)
        driveGroup.add(polar)
        driveGroup.add(direct)
        drivePanel.add(cartesian)
        drivePanel.add(polar)
        drivePanel.add(direct)
        add(drivePanel)

        val modePanel = JPanel()
        modePanel.layout = FlowLayout(FlowLayout.LEFT, 0, 0)
        absolute.isSelected = true
        modeGroup.add(absolute)
        modeGroup.add(relative)
        modePanel.add(absolute)
        modePanel.add(relative)
        val reset = JButton("Reset")
        reset.addActionListener {
            sliders.forEach { it.value = 0 }
        }
        modePanel.add(reset)
        add(modePanel)
    }

    override fun getPreferredSize() = Dimension(300, 400)

    private fun updateDriveMode(driveMode: DriveMode) {
        this.driveMode = driveMode
        updateSpeedVectors()
    }

    private fun updateMode(mode: Mode) {
        this.mode = mode
        updateSpeedVectors()
    }

    private fun updateSpeedVectors() {
        val speeds = DoubleArray(4)
        when (driveMode) {
            DriveMode.CARTESIAN -> {

            }
            DriveMode.POLAR -> {

            }
            DriveMode.DIRECT -> {
                val leftPitch = sliders[0].value/100.0
                val rightPitch = sliders[2].value/100.0
                speeds[0] = leftPitch
                speeds[1] = rightPitch
                speeds[2] = leftPitch
                speeds[3] = rightPitch
            }
        }
        val simPanel = WINDOW!!.simPanel
        speeds.indices.forEach { simPanel.arrows[it].magnitude = speeds[it] }
        simPanel.repaint()
    }

    enum class DriveMode {
        CARTESIAN,
        POLAR,
        DIRECT
    }

    enum class Mode {
        ABSOLUTE,
        RELATIVE
    }
}