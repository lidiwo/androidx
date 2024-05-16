plugins {
    alias(libs.plugins.android.library)
    id("maven-publish")
}


publishing {
    repositories {
        maven {
            isAllowInsecureProtocol = true
            url =  uri("")
            credentials {
                username = ""
                password = ""
            }
        }
        publications {
            create<MavenPublication>("config") {
                afterEvaluate { artifact(tasks.getByName("bundleReleaseAar")) }
                groupId = ""
                artifactId = ""
                version =""
            }
        }
    }
}


android {
    namespace = "androidx.studio"
    compileSdk = 34

    defaultConfig {
        minSdk = 26

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)

    implementation(libs.rxjava3)
    implementation(libs.rxandroid3)
    implementation(libs.eventbus)
    implementation(libs.okhttp)

    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}