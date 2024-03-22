package com.intellij.plugins.phpstorm_dd

import com.google.common.base.Strings
import com.intellij.ide.util.PropertiesComponent
import com.intellij.openapi.diagnostic.Logger
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.DialogWrapper
import java.awt.FlowLayout
import javax.swing.JComponent
import javax.swing.JLabel
import javax.swing.JPanel
import javax.swing.JTextField

class PreferencesDialog(
        project: Project?
) : DialogWrapper(project) {

    private var portTextField: JTextField? = null

    override fun createCenterPanel(): JComponent? {
        val panel = JPanel(FlowLayout())
        panel.add(JLabel("Port: "))
        val propertiesComponent = PropertiesComponent.getInstance()
        val httpPort = propertiesComponent.getInt(Preferences.HTTP_PORT_KEY, Preferences.HTTP_PORT_DEFAULT)
        portTextField = JTextField(httpPort.toString(), 6)
        panel.add(portTextField)
        return panel
    }

    override fun doOKAction() {
        if (!Strings.isNullOrEmpty(portTextField!!.text)) {
            try {
                val httpPort = Integer.valueOf(portTextField!!.text)
                val propertiesComponent = PropertiesComponent.getInstance()
                propertiesComponent.setValue(Preferences.HTTP_PORT_KEY, httpPort, Preferences.HTTP_PORT_DEFAULT)
            } catch (nfe: NumberFormatException) {
                LOGGER.error("Failed to convert proxy port[" + portTextField!!.text + "] to an Integer.", nfe)
            }
        }
        super.doOKAction()
    }

    companion object {
        private val LOGGER = Logger.getInstance(PreferencesDialog::class.java)
    }

    init {
        init()
        title = "Preferences"
    }
}
