import org.jetbrains.compose.compose

plugins {
    kotlin("multiplatform") version "1.5.31"
    id("org.jetbrains.compose") version "1.0.0"
}

group = "me.jlengrand"
version = "1.0"

repositories {
    google()
    mavenCentral()
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    js(IR) {
        browser {
            testTask {
                testLogging.showStandardStreams = true
                useKarma {
                    useChromeHeadless()
                    useFirefox()
                }
            }
        }
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(compose.web.core)
                implementation(compose.runtime)
                implementation(npm("@supabase/supabase-js", "1.31.1"))
                implementation(npm("firebase", "9.6.10"))

                val pathToLocalNpmModule = rootProject.projectDir.resolve("src/jsMain/resources/firebase-ports").canonicalPath
                implementation(npm("@jlengrand/firebase-ports", "file:$pathToLocalNpmModule"))
            }
        }
        val jsTest by getting {
            dependencies {
                implementation(kotlin("test-js"))
            }
        }
    }
}
