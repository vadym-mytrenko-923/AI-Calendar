import java.util.Locale
import kotlin.io.path.isDirectory

// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.android.ksp) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.detekt) apply false

    // DI
    alias(libs.plugins.hilt.android) apply false

    // Firebase
    alias(libs.plugins.google.services) apply false
}

val execHookTask = tasks.register("markGitHookExecutable", Exec::class) {
    description = "Marks Git Hooks Executable"
    group = "Setup"
    workingDir = rootDir
    commandLine = listOf("chmod")
    setArgs(listOf("-R", "+x", ".git/hooks/"))
    notCompatibleWithConfigurationCache("Executes commands directly on CLI.")

    onlyIf { isLinuxOrMacOS() }
    doLast {
        logger.lifecycle("Git hook installed successfully.")
    }
}

val githubDesktopFix = tasks.register("installGithubDesktopSupport", DefaultTask::class) {
    description = "Installs Cygpath for Github Desktop support for Windows"
    group = "Setup"
    notCompatibleWithConfigurationCache("Uses user home directory property.")
    onlyIf { !isLinuxOrMacOS() }

    doLast {
        try {
            val githubDesktopInstallDir =
                file("${System.getProperty("user.home")}\\AppData\\Local\\GitHubDesktop\\")
            java.nio.file.Files.walk(githubDesktopInstallDir.toPath(), 1)
                .filter { it.isDirectory() }
                .filter { it.fileName.toString().startsWith("app-") }
                .forEach { location ->
                    copy {
                        from("C:\\Program Files\\Git\\usr\\bin\\cygpath.exe")
                        into("$location\\resources\\app\\git\\usr\\bin")
                    }
                }
        } catch (_: java.nio.file.NoSuchFileException) {
            logger.lifecycle("Github Desktop not installed. Skipping Github Desktop setup")
        }
    }
}

tasks.register("installGitHooks", Copy::class) {
    description = "Installs Git Hooks"
    group = "Setup"

    if (isLinuxOrMacOS()) {
        from("$rootDir/hooks/pre-push-unix") {
            rename("pre-push-unix", "pre-push")
        }
        into("$rootDir/.git/hooks")
    } else {
        from("$rootDir/hooks/pre-push-windows") {
            rename("pre-push-windows", "pre-push")
        }
        into("$rootDir/.git/hooks")
        doLast {
            logger.lifecycle("Git hook installed successfully.")
        }
    }

    finalizedBy(execHookTask, githubDesktopFix)
}

fun isLinuxOrMacOS(): Boolean {
    val osName = System.getProperty("os.name").lowercase(Locale.ROOT)
    return osName.contains("linux") || osName.contains("mac os") || osName.contains("macos")
}

tasks.register("deleteGitHooks", Delete::class) {
    description = "Deletes Git Hooks"
    group = "Setup"

    delete("$rootDir/.git/hooks")
}
