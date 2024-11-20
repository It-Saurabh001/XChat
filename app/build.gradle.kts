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
}
configurations {
    all {
        resolutionStrategy {
            force ("com.google.protobuf:protobuf-java:3.25.1")
            force("com.google.protobuf:protobuf-lite:3.25.1")
            exclude(group = "com.google.protobuf", module = "protobuf-javalite")
        }
    }
}
configurations.all {
    resolutionStrategy.eachDependency {
        if (requested.group == "com.google.protobuf" && requested.name == "protobuf-javalite") {
            exclude(group = "com.google.protobuf", module = "protobuf-javalite")
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

    implementation("androidx.compose.material:material:1.7.4")
    implementation("androidx.compose.ui:ui:1.7.4")
    implementation("androidx.compose.ui:ui-tooling:1.7.4")



    kapt("androidx.room:room-compiler:2.6.1")

//    dagger dependency
    implementation ("com.google.dagger:hilt-android:2.52")
    kapt ("com.google.dagger:hilt-compiler:2.52")

    // For instrumentation tests
    androidTestImplementation ("com.google.dagger:hilt-android-testing:2.52")
    kaptAndroidTest ("com.google.dagger:hilt-compiler:2.52")
    // For local unit tests
    testImplementation ("com.google.dagger:hilt-android-testing:2.52")
    kaptTest ("com.google.dagger:hilt-compiler:2.52")
    implementation("androidx.navigation:navigation-compose:2.8.4")
    implementation("androidx.hilt:hilt-navigation-compose:1.2.0")
    // firebase
    implementation(platform("com.google.firebase:firebase-bom:33.6.0"))
    implementation ("com.google.firebase:firebase-auth-ktx"){
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
    }
    implementation ("com.google.firebase:firebase-firestore-ktx")
    implementation ("com.google.firebase:firebase-storage-ktx:20.1.0"){
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
    }
    implementation("com.google.firebase:firebase-messaging:22.0.0") {
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
    }

    implementation("io.coil-kt.coil3:coil-compose:3.0.0-rc01"){
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
    }
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0-rc01"){
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
    }
    implementation("com.google.firebase:firebase-firestore:25.1.1") {
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
        exclude(group = "com.google.firebase", module = "protolite-well-known-types")
    }

    implementation("com.google.protobuf:protobuf-java:3.25.1")

    implementation("com.google.firebase:firebase-auth:21.1.1") {
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
    }

    implementation("com.google.firebase:firebase-database:21.0.0") {
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
    }

    implementation("com.google.firebase:firebase-database:21.0.0") {
        exclude (group= "com.google.protobuf", module= "protobuf-javalite")
    }

    implementation("io.grpc:grpc-protobuf-lite:1.62.2") {
        exclude (group= "com.google.protobuf", module= "protobuf-javalite")
    }
    implementation("com.google.firebase:protolite-well-known-types:18.0.0") {
        exclude (group= "com.google.protobuf", module= "protobuf-javalite")
    }
    implementation("io.coil-kt.coil3:coil-compose:3.0.0-rc01") {
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
    }
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.0-rc01") {
        exclude(group = "com.google.protobuf", module = "protobuf-javalite")
    }

}