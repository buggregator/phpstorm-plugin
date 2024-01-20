package com.intellij.plugins.phpstorm_dd

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent

class PreferencesAction : AnAction("Preferences", "Open the preferences dialog", AllIcons.General.Settings) {
    override fun actionPerformed(anActionEvent: AnActionEvent) {
        val preferencesDialog = PreferencesDialog(anActionEvent.project)
        preferencesDialog.show()
    }
}
