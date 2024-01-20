package com.intellij.plugins.phpstorm_dd

import com.intellij.openapi.actionSystem.ActionManager
import com.intellij.openapi.actionSystem.DefaultActionGroup
import com.intellij.openapi.ui.SimpleToolWindowPanel
import com.intellij.ui.components.JBScrollPane
import com.intellij.ui.components.JBTabbedPane
import com.intellij.ui.jcef.JBCefBrowser
import java.awt.Component
import java.awt.GridLayout
import javax.swing.*

class TrapToolWindowPanel(
        vertical: Boolean,
        borderless: Boolean
) : SimpleToolWindowPanel(vertical, borderless) {

//    private val tableModel = NetworkTableModel()

    private var requestHeaderTextArea: JTextArea? = null
    private var requestContentTextArea: JTextArea? = null
    private var responseHeaderTextArea: JTextArea? = null
    private var responseContentTextArea: JTextArea? = null
    private var curlTextArea: JTextArea? = null
    private var webview: JBCefBrowser? = null

    private fun createToolBar() {
        val defaultActionGroup = DefaultActionGroup()
        defaultActionGroup.add(StartTrapServerAction())
        defaultActionGroup.add(StopTrapServerAction())
//        defaultActionGroup.add(com.intellij.plugins.phpstorm_dd.ClearAllViewAction(tableModel))
        defaultActionGroup.addSeparator()
        defaultActionGroup.add(PreferencesAction())

        val toolBarPanel = JPanel(GridLayout())
        toolBarPanel.add(ActionManager.getInstance().createActionToolbar(TRAP_TOOLBAR, defaultActionGroup, false).component)

        toolbar = toolBarPanel
    }

    private fun createContent() {
        val webviewComponent = createWebviewComponent()
//        val splitPane = JSplitPane(JSplitPane.HORIZONTAL_SPLIT, createTableComponent(), webviewComponent)
//        splitPane.dividerSize = 0
        
        setContent(webviewComponent as JComponent)
    }

//    private fun createTableComponent(): Component {
//        val rightAlignedTableCellRenderer = DefaultTableCellRenderer()
//        rightAlignedTableCellRenderer.horizontalAlignment = JLabel.CENTER
//
//        val table = JBTable(tableModel)
////        table.columnModel.getColumn(1).cellRenderer = rightAlignedTableCellRenderer
////        table.columnModel.getColumn(2).cellRenderer = rightAlignedTableCellRenderer
////        table.columnModel.getColumn(3).cellRenderer = rightAlignedTableCellRenderer
////        table.columnModel.getColumn(4).cellRenderer = rightAlignedTableCellRenderer
//        table.selectionModel.addListSelectionListener { _: ListSelectionEvent? ->
//            if (table.selectedRow >= 0) {
//                ApplicationManager.getApplication().invokeLater {
//                    val requestResponse = tableModel.getRow(table.selectedRow)
//                    val dd = Json.parseToJsonElement(requestResponse.requestContent).jsonObject
//                    webview!!.loadHTML(
//                        "" +
//                            "<style>html{background:#18171b;} a{color:#fff}</style>" +
//                            "<a href=\"phpstorm://"+dd["file"]?.jsonPrimitive?.content+"\">"+dd["file"]?.jsonPrimitive?.content+"</a><br>" +
//                            "<pre>" + dd["dump"]?.jsonPrimitive?.content + "</pre>"
//                    )
//                    
////                    requestHeaderTextArea!!.text = requestResponse.requestHeaders
////                    requestHeaderTextArea!!.caretPosition = 0
////                    requestContentTextArea!!.text = requestResponse.requestContent
////                    requestContentTextArea!!.caretPosition = 0
////                    responseHeaderTextArea!!.text = requestResponse.responseHeaders
////                    responseHeaderTextArea!!.caretPosition = 0
////                    responseContentTextArea!!.text = requestResponse.responseContent
////                    responseContentTextArea!!.caretPosition = 0
////                    curlTextArea!!.text = requestResponse.curlRequest
////                    curlTextArea!!.caretPosition = 0
//                }
//            } else {
//                ApplicationManager.getApplication().invokeLater {
////                    requestHeaderTextArea!!.text = ""
//                    requestContentTextArea!!.text = ""
////                    responseHeaderTextArea!!.text = ""
////                    responseContentTextArea!!.text = ""
////                    curlTextArea!!.text = ""
//                }
//            }
//        }
//        return JBScrollPane(table)
//    }

    private fun createTabbedComponent(): Component {
        val tabbedPane = JBTabbedPane(SwingConstants.TOP)
        tabbedPane.insertTab("Request Headers", null, createRequestHeaderComponent(), "", 0)
        tabbedPane.insertTab("Request Content", null, createRequestContentComponent(), "", 1)
        tabbedPane.insertTab("Response Headers", null, createResponseHeaderComponent(), "", 2)
        tabbedPane.insertTab("Response Content", null, createResponseContentComponent(), "", 3)
        tabbedPane.insertTab("Curl Request", null, createCurlRequestComponent(), "", 4)
        return tabbedPane
    }

    private fun createRequestHeaderComponent(): Component {
        requestHeaderTextArea = JTextArea()
        requestHeaderTextArea!!.isEditable = false
        return JBScrollPane(requestHeaderTextArea)
    }

    private fun createRequestContentComponent(): Component {
        requestContentTextArea = JTextArea()
        requestContentTextArea!!.isEditable = false
        return JBScrollPane(requestContentTextArea)
    }
    
    private fun createWebviewComponent(): Component {
        webview = JBCefBrowser()
        
        return JBScrollPane(webview!!.component)
    }

    private fun createResponseHeaderComponent(): Component {
        responseHeaderTextArea = JTextArea()
        responseHeaderTextArea!!.isEditable = false
        return JBScrollPane(responseHeaderTextArea)
    }

    private fun createResponseContentComponent(): Component {
        responseContentTextArea = JTextArea()
        responseContentTextArea!!.isEditable = false
        return JBScrollPane(responseContentTextArea)
    }

    private fun createCurlRequestComponent(): Component {
        curlTextArea = JTextArea()
        curlTextArea!!.isEditable = false
        return JBScrollPane(curlTextArea)
    }

    companion object {
        const val TRAP_TOOLBAR = "TrapToolbar"
    }

    /**
     * Constructor. Used to build the necessary components.
     *
     * @param vertical
     * @param borderless
     */
    init {
        createToolBar()
        createContent()
    }
}
