plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)

    id("com.google.devtools.ksp")
    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.my.mvistudymultimodule"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.my.mvistudymultimodule"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        dataBinding = true
    }
}

dependencies {

    /* Module */
    implementation(project(":core:base"))
    implementation(project(":core:di"))
    implementation(project(":core:model"))
    implementation(project(":core:util"))
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":feature:home"))
    implementation(project(":feature:xml"))
    implementation(project(":feature:compose"))

    /* Hilt */
    implementation(libs.hilt.android)
    ksp(libs.hilt.android.compiler)

    /* Coroutine */
    implementation(libs.kotlinx.coroutines.core)

    /* Navigation */
    implementation(libs.androidx.navigation.fragment.ktx)
    implementation(libs.androidx.navigation.ui.ktx)

    /* Retrofit */
    implementation(libs.retrofit)
    implementation(libs.converter.gson)

    /* Okhttp Interceptor */
    implementation(libs.logging.interceptor)

    /* Paging3 */
    implementation(libs.androidx.paging.runtime)

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}