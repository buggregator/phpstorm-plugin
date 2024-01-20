package com.intellij.plugins.phpstorm_dd

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class ClearAllViewAction(
//        private val tableModel: com.intellij.plugins.phpstorm_dd.NetworkTableModel
) : AnAction("Clear All", "", AllIcons.Actions.GC) {
    override fun actionPerformed(event: AnActionEvent) {
//        tableModel.clear()
    }
}
