package ru.nsu.fit.g20204.kuznetsov.wireframe.b_spline_editor

import ru.nsu.fit.g20204.kuznetsov.wireframe.math.BSpline
import ru.nsu.fit.g20204.kuznetsov.wireframe.model.Geometry
import java.awt.BorderLayout
import java.awt.Dimension
import javax.swing.JFrame

class BSplineEditor : JFrame("B-Spline Editor") {
    private val splinePane = BSplinePane()
    private val paramPane = BSplineParamPane(splinePane, this)
    private val splineChangedListeners: MutableList<(Geometry) -> Unit> = ArrayList()

    init {
        minimumSize = Dimension(640, 480)
        setLocation(600, 160)
        setLocation(600, 160)
        isVisible = true
        add(splinePane, BorderLayout.CENTER)
        add(paramPane, BorderLayout.PAGE_END)
    }

    val spline: BSpline
        get() = splinePane.spline
    val splineModel: Geometry
        get() = paramPane.splineModel

    fun addSplineModelChangeListener(listener: (Geometry) -> Unit) {
        splineChangedListeners.add(listener)
    }

    fun applySpline() {
        for (l in splineChangedListeners) {
            l(paramPane.splineModel)
        }
    }
}
