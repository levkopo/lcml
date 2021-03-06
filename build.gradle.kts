plugins {
    kotlin("multiplatform") version "1.5.10"
}

group = "com.github.levkoposc"
version = "1.0.0"

repositories {
    mavenCentral()
}

kotlin {
    jvm {
        withJava()
        compilations.all {
            kotlinOptions.jvmTarget = "1.8"
        }

        testRuns["test"].executionTask.configure {
            useJUnit()
        }
    }

    js(LEGACY) {
        browser {
            commonWebpackConfig {
                cssSupport.enabled = true
            }
        }
    }

    val hostOs = System.getProperty("os.name")
    val isMingwX64 = hostOs.startsWith("Windows")
    val nativeTarget = when {
        hostOs == "Mac OS X" -> macosX64("native")
        hostOs == "Linux" -> linuxX64("native")
        isMingwX64 -> mingwX64("native")
        else -> throw GradleException("Host OS is not supported in Kotlin/Native.")
    }

    
    sourceSets {
        val commonMain by getting
        val jvmMain by getting
        val jvmTest by getting {
            dependencies {
                implementation("junit:junit:4.13.2")
                implementation(kotlin("test"))
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
        val jsMain by getting
        val nativeMain by getting
    }
}
