package com.intellij.plugins.phpstorm_dd

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.ToolWindow
import com.intellij.openapi.wm.ToolWindowFactory
import com.intellij.ui.content.ContentFactory

class TrapToolWindowFactory : ToolWindowFactory {
    override fun createToolWindowContent(project: Project, toolWindow: ToolWindow) {
        val trapToolWindowPanel =
            TrapToolWindowPanel(
                vertical = false,
                borderless = true
            )
        val contentFactory = ContentFactory.SERVICE.getInstance()
        val content = contentFactory.createContent(trapToolWindowPanel, "", false)
        toolWindow.contentManager.addContent(content)
    }
}
