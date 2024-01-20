package com.intellij.plugins.phpstorm_dd

import com.intellij.icons.AllIcons
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.jetbrains.php.config.interpreters.PhpInterpretersManagerImpl
import kotlinx.coroutines.isActive


class StartTrapServerAction()
    : AnAction("Start Server", "", AllIcons.Actions.Execute) {

    override fun actionPerformed(event: AnActionEvent) {

        val project = event.dataContext.getData(CommonDataKeys.PROJECT)

        val interpretersManager = PhpInterpretersManagerImpl.getInstance(project)

        for (interpreter in interpretersManager.interpreters) {
            println(interpreter.name)
            println(interpreter.pathToPhpExecutable)
        }

        val trapServerService = event.project!!.getService(TrapServerService::class.java)
        trapServerService.startTrapServer(interpretersManager.interpreters[1])
    }

    override fun update(event: AnActionEvent) {
        val trapServerService = event.project!!.getService(TrapServerService::class.java)
        event.presentation.isEnabled = trapServerService.trapDaemon?.isActive == null || trapServerService.trapDaemon?.isActive == false
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
