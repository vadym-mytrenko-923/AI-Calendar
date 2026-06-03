import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    alias(libs.plugins.kotlin.android.ksp)
    alias(libs.plugins.detekt)

    // Serialization
    alias(libs.plugins.kotlin.serialization)
    id("kotlin-parcelize")

    // DI
    alias(libs.plugins.hilt.android)
}

val VERSION_BUILD = 1
val VERSION_MAJOR = 1
val VERSION_MINOR = 0
val VERSION_PATCH = 0
val QA_BASE_URL: String by project
val UAT_BASE_URL: String by project
val PROD_BASE_URL: String by project

android {
    namespace = "com.agents.app.demo"
    compileSdk {
        version = release(36)
    }

    defaultConfig {
        applicationId = "com.agents.app.demo"
        minSdk = 26
        targetSdk = 36
        versionCode = VERSION_BUILD
        versionName = "$VERSION_MAJOR.$VERSION_MINOR.$VERSION_PATCH"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            addManifestPlaceholders(mapOf("enableCrashReporting" to true))
            isMinifyEnabled = true
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
        debug {
            addManifestPlaceholders(mapOf("enableCrashReporting" to false))
        }
    }
    flavorDimensions += listOf("default")
    productFlavors {
        create("qa") {
            dimension = "default"
            buildConfigField("String", "BASE_URL", "\"$QA_BASE_URL\"")
            resValue("string", "app_name", "AI Agentic App QA")
            versionCode = VERSION_BUILD
        }
        create("uat") {
            dimension = "default"
            buildConfigField("String", "BASE_URL", "\"$UAT_BASE_URL\"")
            resValue("string", "app_name", "AI Agentic App UAT")
            versionCode = VERSION_BUILD + 1
        }
        create("production") {
            dimension = "default"
            buildConfigField("String", "BASE_URL", "\"$PROD_BASE_URL\"")
            resValue("string", "app_name", "AI Agentic App")
            versionCode = VERSION_BUILD + 2
        }
    }

    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val separator = "_"
                val flavorName = variant.productFlavors.firstOrNull()?.name?.uppercase() ?: ""
                val buildTypeName = variant.buildType.name.uppercase()
                val versionName = variant.versionName
                val versionCode = variant.versionCode

                val outputFileName = "${flavorName}${separator}${buildTypeName}${separator}${versionName}${separator}(${versionCode}).apk"
                output.outputFileName = outputFileName
            }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
}

dependencies {
    // Common
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.core.splash.screen)

    // UI
    implementation(libs.coil.compose)
    implementation(libs.paging.runtime)
    implementation(libs.paging.compose)
    testImplementation(libs.paging.common)

    // Navigation
    implementation(libs.androidx.navigation.compose)

    // DI
    implementation(libs.hilt.android)
    implementation(libs.hilt.navigation.compose)
    ksp(libs.hilt.compiler)

    // Network
    implementation(libs.retrofit)
    implementation(libs.retrofit.gson)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)

    // Serialization
    implementation(libs.kotlinx.serialization.json)

    // Storage
    implementation(libs.data.store)
    implementation(libs.room.runtime)
    implementation(libs.room.ktx)
    ksp(libs.room.compiler)
    implementation(libs.room.paging)

    // Security
    implementation(libs.android.tink)

    // Logs
    implementation(libs.timber)

    detektPlugins(libs.detektFormatting)

    // Tests
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
}

detekt {
    config.setFrom(file("../config/detekt/detekt.yml"))
    // Run rules in parallel
    parallel = true
    // Builds on the default configuration. Our changes overlay it.
    buildUponDefaultConfig = true
    // Enables or disables auto correction. Should be applied during local builds.
    autoCorrect = !isRunningOnCI()
}

fun isRunningOnCI(): Boolean {
    return System.getenv()["CI"].toBoolean()
}
