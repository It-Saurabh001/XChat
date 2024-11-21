plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
    id("org.jetbrains.kotlin.kapt")
    id ("com.google.dagger.hilt.android")
    alias(libs.plugins.google.gms.google.services)
}

android {
    namespace = "com.example.xchat"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.xchat"
        minSdk = 25
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
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
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    configurations.all {
        exclude(group = "com.google.protobuf", module = "protobuf-java")

        resolutionStrategy {
            eachDependency {
                if (requested.group == "com.google.protobuf") {
                    useVersion("3.25.1")
                    because("Standardize Protobuf version")
                }
            }
        }
    }
}
dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.androidx.runtime.livedata)
    implementation(libs.firebase.storage.ktx)
    implementation(libs.firebase.storage)
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.tools.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation(libs.androidx.material)
    implementation(libs.ui)
    implementation(libs.ui.tooling)
    kapt("androidx.room:room-compiler:2.6.1")
//    dagger dependency
    implementation (libs.hilt.android)
    kapt (libs.google.hilt.compiler)

    // For instrumentation tests
    androidTestImplementation (libs.dagger.hilt.android.testing)
    kaptAndroidTest (libs.google.hilt.compiler)
    // For local unit tests
    testImplementation (libs.dagger.hilt.android.testing)
    kaptTest (libs.google.hilt.compiler)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    // firebase
    implementation(platform(libs.firebase.bom))
    implementation (libs.firebase.auth.ktx)
    implementation (libs.firebase.firestore.ktx)
    implementation (libs.firebase.storage.ktx.v2010)
    implementation(libs.firebase.messaging)
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.firebase.firestore.v2511)
    implementation(libs.firebase.auth.v2111)
    implementation(libs.google.firebase.database)
    implementation("com.google.protobuf:protobuf-javalite:3.25.1")
//    implementation("com.google.protobuf:protobuf-kotlin:3.25.1")
}
buildscript {
    dependencies {
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.9.1")
    }
}