package com.intellij.plugins.phpstorm_dd

import com.intellij.openapi.Disposable
import com.intellij.openapi.components.ServiceManager
import com.intellij.openapi.externalSystem.service.ui.completion.collector.TextCompletionCollector.Companion.async
import com.intellij.openapi.project.Project
import com.intellij.platform.ijent.IjentProcessWatcher.Companion.launch
import java.io.File
import com.jetbrains.php.config.interpreters.PhpInterpreter
import com.jetbrains.rd.generator.nova.PredefinedType
import kotlinx.coroutines.*
import java.io.InputStream
import java.util.concurrent.TimeUnit

class TrapServerService(
    private var project: Project?
) : Disposable {

    var trapDaemon: Job? = null

    override fun dispose() {
        if (trapDaemon != null) {
            trapDaemon!!.cancel()
            trapDaemon = null
        }
    }
    
    fun startTrapServer(interpreter: PhpInterpreter) {
        
        if (trapDaemon?.isActive == true) {
            return
        }
        
        val cmdString = "%s %s/vendor/bin/trap --ui".format(interpreter.pathToPhpExecutable, project?.basePath)
        val cmdMap = cmdString.split(" ")
        val workingDir = File(project?.basePath!!)
        
//        trapDaemon = ProcessBuilder(cmdMap)
//            .directory(workingDir)
//            .redirectOutput(ProcessBuilder.Redirect.INHERIT)
//            .redirectError(ProcessBuilder.Redirect.INHERIT)
//            .start()


        trapDaemon = CoroutineScope(Dispatchers.IO).launch {
            executeCommand(cmdMap, workingDir)
        }
        
        println("here")
    }

    suspend fun stopTrapServer() {

        if (trapDaemon == null || !trapDaemon!!.isActive) {
            return
        }
        
        trapDaemon!!.cancel()
        trapDaemon!!.join()
    }

    private suspend fun executeCommand(commandArgs: List<String>, workingDir: File): CoroutineScope = withContext(
        Dispatchers.IO
    ) {
        runCatching {
            val process = ProcessBuilder(commandArgs)
                .directory(workingDir)
                .start()
            val outputStream = async {
                println("Context for output stream -> $coroutineContext -> Thread -> ${Thread.currentThread()}")
                readStream(process.inputStream) }
            val errorStream = async {
                println("Context for error stream -> $coroutineContext -> Thread -> ${Thread.currentThread()}")
                readStream(process.errorStream)
            }
            println("Context for exit code -> $coroutineContext -> Thread -> ${Thread.currentThread()}")
            val exitCode =  process.waitFor()
            ProcessResult(
                exitCode = exitCode,
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

    private fun readStream(inputStream: InputStream) =
        inputStream.bufferedReader().use { reader -> reader.readText() }

    data class ProcessResult(val exitCode: Int, val message: String, val errorMessage: String)

    companion object {
        fun getInstance(project: Project): TrapServerService {
            return ServiceManager.getService(project, TrapServerService::class.java)
        }
    }
}
