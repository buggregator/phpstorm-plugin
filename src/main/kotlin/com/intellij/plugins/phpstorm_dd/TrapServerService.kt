package com.intellij.plugins.phpstorm_dd

import com.intellij.openapi.Disposable
import com.intellij.openapi.project.Project
import com.intellij.ui.jcef.JBCefBrowser
import java.io.File
import com.jetbrains.php.config.interpreters.PhpInterpreter
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.Channel
import java.io.InputStream

class TrapServerService(
    private var project: Project?
) : Disposable {

    var webview: JBCefBrowser? = null
    var trapDaemon: Process? = null

    override fun dispose() {
        if (trapDaemon != null) {
            trapDaemon!!.destroy()
            trapDaemon = null
        }
    }

//    private var processChannel: Channel<Process?>? = null
    private var processChannel = Channel<Process?>()

    suspend fun startTrapServer(interpreter: PhpInterpreter) {
        
        if (trapDaemon?.isAlive == true) {
            return
        }
        
        val cmdString = "%s %s/vendor/bin/trap --ui".format(interpreter.pathToPhpExecutable, project?.basePath)
        val cmdMap = cmdString.split(" ")
        val workingDir = File(project?.basePath!!)
        
        CoroutineScope(Dispatchers.IO).launch {
            executeCommand(cmdMap, workingDir)
        }

        trapDaemon = processChannel.receive()
        
        println("here")
    }

    fun stopTrapServer() {

        if (trapDaemon == null || !trapDaemon!!.isAlive) {
            return
        }

        trapDaemon?.destroy()
    }
    
    fun statusChanged() {
        if (trapDaemon == null || !trapDaemon!!.isAlive) {
            webview?.loadHTML("Not Running");
        } else {
            webview?.loadURL("http://127.0.0.1:8000")
        }
    }

    private suspend fun executeCommand(commandArgs: List<String>, workingDir: File): CoroutineScope = withContext(
        Dispatchers.IO
    ) {
        var process: Process? = null
        runCatching {
            process = ProcessBuilder(commandArgs)
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.INHERIT)
                .redirectError(ProcessBuilder.Redirect.INHERIT)
                .start()

            processChannel.send(process)
            
            val outputStream = async {
                readStream(process!!.inputStream) }
            val errorStream = async {
                readStream(process!!.errorStream)
            }
            val exitCode =  process?.waitFor()

            ProcessResult(
                exitCode = exitCode!!,
                message = outputStream.await(),
                errorMessage = errorStream.await()
            )
        }.onFailure{
            ProcessResult(
                exitCode = -1,
                message = "",
                errorMessage = it.localizedMessage
            )
        }.getOrThrow()
            

        return@withContext this
    }

    data class ProcessResult(val exitCode: Int, val message: String, val errorMessage: String)

    private fun readStream(inputStream: InputStream) =
        inputStream.bufferedReader().use { reader -> reader.readText() }
}
