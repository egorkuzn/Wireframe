package ru.nsu.fit.g20204.kuznetsov.wireframe.b_spline_editor

class BSplineParamPane(private val splinePane: BSplinePane, splineEditor: BSplineEditor) : JPanel() {
    private val splinePointsPerSegmentSpinnerModel = SpinnerNumberModel(10, 2, 100, 1)
    private val acrossLayersSpinnerModel = SpinnerNumberModel(0, 0, 0, 1)
    private val rotationSpinnerModel = SpinnerNumberModel(6, 1, 360, 1)
    private val alongLayersSpinnerModel = SpinnerNumberModel(6, 1, 6, 1)

    init {
        addAll(splineEditor)
    }

    private fun addAll(splineEditor: BSplineEditor) {
        add(splineParameterPane)
        add(keyPointParametersPane)
        add(modelParametersPane)
        add(getApplyButton(splineEditor))
    }

    private fun getApplyButton(splineEditor: BSplineEditor): JButton {
        val applyButton = JButton("Apply")
        applyButton.addActionListener { e: ActionEvent? -> splineEditor.applySpline() }
        return applyButton
    }

    private val modelParametersPane: JPanel
        private get() {
            val modelParametersPane = JPanel()
            modelParametersPane.layout = BoxLayout(modelParametersPane, BoxLayout.PAGE_AXIS)

            // Rotation count
            val rotationSpinner = JSpinner(rotationSpinnerModel)
            modelParametersPane.add(getSpinnerPane("Rotation count", rotationSpinner))

            // Along-layer count
            val alongLayersSpinner = JSpinner(alongLayersSpinnerModel)
            modelParametersPane.add(getSpinnerPane("Number of along-layers", alongLayersSpinner))

            // Rotation count
            val acrossLayersSpinner = JSpinner(acrossLayersSpinnerModel)
            modelParametersPane.add(getSpinnerPane("Number of across-layers", acrossLayersSpinner))
            rotationSpinner.addChangeListener { e: ChangeEvent? ->
                alongLayersSpinnerModel.maximum = rotationSpinnerModel.number as Int
                alongLayersSpinnerModel.value =
                    Math.min((alongLayersSpinnerModel.number as Int), (alongLayersSpinnerModel.maximum as Int))
            }
            splinePane.addPointModifiedListener { i: Int?, p: Point2D.Double? ->
                val splinePointCount = splinePane.spline.splinePoints.size
                acrossLayersSpinnerModel.maximum = splinePointCount
                acrossLayersSpinnerModel.value =
                    Math.min((acrossLayersSpinnerModel.number as Int), (acrossLayersSpinnerModel.maximum as Int))
                null
            }
            return modelParametersPane
        }

    private fun getSpinnerPane(name: String, spinner: JSpinner): Component {
        val pane = JPanel()
        pane.layout = FlowLayout(FlowLayout.LEFT)
        val label = JLabel("$name: ")
        pane.add(label)
        pane.add(spinner)
        return pane
    }

    private val keyPointParametersPane: JPanel
        private get() {
            val keyPointParametersPane = JPanel()
            keyPointParametersPane.layout = BoxLayout(keyPointParametersPane, BoxLayout.PAGE_AXIS)

            // Key point index
            val indexPane = JPanel()
            indexPane.layout = FlowLayout(FlowLayout.LEFT)
            indexPane.add(JLabel("Index: "))
            val indexField = JTextArea()
            indexField.isEditable = false
            indexPane.add(indexField)
            keyPointParametersPane.add(indexPane)

            // X coordinate
            val xSpinnerModel = SpinnerNumberModel(0.0, -100.0, 100.0, 1.0)
            val xSpinner = JSpinner(xSpinnerModel)
            keyPointParametersPane.add(getSpinnerPane("X", xSpinner))

            // Y coordinate
            val ySpinnerModel = SpinnerNumberModel(0.0, -100.0, 100.0, 1.0)
            val ySpinner = JSpinner(ySpinnerModel)
            keyPointParametersPane.add(getSpinnerPane("Y", ySpinner))
            xSpinner.addChangeListener { c: ChangeEvent? -> splinePane.setSelectedX(xSpinnerModel.number as Double) }
            ySpinner.addChangeListener { c: ChangeEvent? -> splinePane.setSelectedY(ySpinnerModel.number as Double) }
            splinePane.addPointModifiedListener { i: Int, p: Point2D.Double ->
                if (i == -1) {
                    indexField.text = "None"
                    xSpinner.isEnabled = false
                    ySpinner.isEnabled = false
                    return@addPointModifiedListener null
                }
                indexField.text = i.toString()
                xSpinner.isEnabled = true
                ySpinner.isEnabled = true
                xSpinnerModel.value = p.x
                ySpinnerModel.value = p.y
                null
            }
            return keyPointParametersPane
        }
    private val splineParameterPane: JPanel
        private get() {
            val splineParametersPane = JPanel()
            splineParametersPane.layout = BoxLayout(splineParametersPane, BoxLayout.PAGE_AXIS)

            // Spline points per segment
            val splinePointsPerSegmentSpinner = JSpinner(splinePointsPerSegmentSpinnerModel)
            splineParametersPane.add(
                getSpinnerPane(
                    "<html>Points per<br/>spline segment</html>",
                    splinePointsPerSegmentSpinner
                )
            )
            splinePointsPerSegmentSpinner.addChangeListener { l: ChangeEvent? ->
                splinePane.setSplinePointsPerSegment(splinePointsPerSegmentSpinnerModel.value as Int)
                acrossLayersSpinnerModel.maximum = splinePane.spline.splinePoints.size
                acrossLayersSpinnerModel.value =
                    Math.min((acrossLayersSpinnerModel.number as Int), (acrossLayersSpinnerModel.maximum as Int))
            }
            return splineParametersPane
        }

    @get:Throws(IllegalArgumentException::class)
    val splineModel: Geometry
        get() {
            val spline = splinePane.spline
            val rotatingCount = rotationSpinnerModel.number as Int
            val alongLayerCount = alongLayersSpinnerModel.number as Int
            val acrossLayerCount = acrossLayersSpinnerModel.number as Int
            return ModelFactory.createRoutedSplineModel(spline, rotatingCount, alongLayerCount, acrossLayerCount)
        }
}