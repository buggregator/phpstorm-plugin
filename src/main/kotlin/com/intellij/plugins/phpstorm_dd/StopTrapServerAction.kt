package com.intellij.plugins.phpstorm_dd

import com.intellij.icons.AllIcons
import com.intellij.notification.Notification
import com.intellij.notification.NotificationType
import com.intellij.notification.Notifications
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent


class StopTrapServerAction()
    : AnAction("Stop Server", "", AllIcons.Actions.Suspend) {

    override fun actionPerformed(event: AnActionEvent) {
        event.presentation.isEnabled = false
//        notifyProxyShutdown()
        val trapServerService = event.project!!.getService(TrapServerService::class.java)
        trapServerService.stopTrapServer()
    }

    override fun update(event: AnActionEvent) {
        val trapServerService = event.project!!.getService(TrapServerService::class.java)
        val oldValue = event.presentation.isEnabled
        event.presentation.isEnabled = trapServerService.trapDaemon !== null && trapServerService.trapDaemon?.isAlive == true
        
        if (oldValue != event.presentation.isEnabled) {
            trapServerService.statusChanged()
        }
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
