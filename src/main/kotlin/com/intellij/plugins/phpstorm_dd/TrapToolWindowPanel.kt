package com.intellij.plugins.phpstorm_dd

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.jcef.JBCefBrowser
import java.awt.Component
import java.awt.GridLayout
import javax.swing.*

class TrapToolWindowPanel(
        vertical: Boolean,
        borderless: Boolean
) : SimpleToolWindowPanel(vertical, borderless) {

    var webview: JBCefBrowser? = null

    private fun createToolBar() {
        val defaultActionGroup = DefaultActionGroup()
        defaultActionGroup.add(StartTrapServerAction())
        defaultActionGroup.add(StopTrapServerAction())
        defaultActionGroup.addSeparator()
        defaultActionGroup.add(PreferencesAction())
        
        val toolBarPanel = JPanel(GridLayout())
        toolBarPanel.add(ActionManager.getInstance().createActionToolbar(TRAP_TOOLBAR, defaultActionGroup, false).component)

        toolbar = toolBarPanel
    }

    private fun createContent() {
        val webviewComponent = createWebviewComponent()
        setContent(webviewComponent as JComponent)
    }
    
    private fun createWebviewComponent(): Component {
        webview = JBCefBrowser()

//        webview!!.loadURL("http://127.0.0.1:8000")
        
        return JBScrollPane(webview!!.component)
    }

    companion object {
        const val TRAP_TOOLBAR = "TrapToolbar"
    }

    init {
        createToolBar()
        createContent()
    }
}
