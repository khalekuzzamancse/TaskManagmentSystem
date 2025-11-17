plugins {
    alias(libs.plugins.convention.composeMultiplatfrom)
    alias(libs.plugins.composeCompiler)
}
kotlin {
    sourceSets{
        val commonMain by getting{
            dependencies {
                implementation(libs.androidx.lifecycle.viewmodel.compose)
                implementation(projects.core)
            }
        }
        val androidMain by getting{
            dependencies {
                implementation(project.dependencies.platform(libs.androidx.compose.bom))
                implementation(libs.bundles.compose)
                implementation(libs.bundles.navigation3)
            }
        }

    }


}
android {
    namespace = "features"
}
