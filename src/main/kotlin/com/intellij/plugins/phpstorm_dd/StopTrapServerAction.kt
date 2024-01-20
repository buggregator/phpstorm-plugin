package com.intellij.plugins.phpstorm_dd

import com.intellij.icons.AllIcons
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class StopTrapServerAction : AnAction("Stop Server", "", AllIcons.Actions.Suspend) {

    override fun actionPerformed(event: AnActionEvent) {
        notifyProxyShutdown()
        val trapServerService = event.project!!.getService(TrapServerService::class.java)
        GlobalScope.launch {
            trapServerService.stopTrapServer()
        }
        
    }

    override fun update(event: AnActionEvent) {
        val trapServerService = event.project!!.getService(TrapServerService::class.java)
        event.presentation.isEnabled = trapServerService.trapDaemon?.isActive !== null && trapServerService.trapDaemon?.isActive == true
    }

    private fun notifyProxyShutdown() {
        val notification = Notification(NotificationConstants.NOTIFICATION_GROUP_ID,
                NotificationConstants.NOTIFICATION_TITLE,
                "Stopping proxy server.",
                NotificationType.INFORMATION)

        Notifications.Bus.notify(notification)
    }

    override fun getActionUpdateThread(): ActionUpdateThread {
        return ActionUpdateThread.BGT
    }
}
