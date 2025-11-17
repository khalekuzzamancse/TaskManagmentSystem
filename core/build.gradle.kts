plugins {
    alias(libs.plugins.convention.composeMultiplatfrom)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.ksp)
    alias(libs.plugins.androidx.room)
    alias(libs.plugins.composeCompiler)
}
kotlin {
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.androidx.room.runtime)
                implementation(libs.androidx.sqlite.bundled)
            }
        }
        val androidMain by getting{
            dependencies{
                implementation(project.dependencies.platform(libs.androidx.compose.bom))
                implementation(libs.bundles.compose)
            }
        }

    }

    
}
android {
    namespace = "core"
}
room {
    schemaDirectory("$projectDir/schemas")
}
dependencies {
    add("kspAndroid", libs.androidx.room.compiler)
}
