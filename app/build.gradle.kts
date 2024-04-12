import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.android)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.ksp)
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")

}



android {
    compileSdk = project.libs.versions.app.build.compileSDKVersion.get().toInt()
    ndkVersion ="21.4.7075529"
    defaultConfig {
        applicationId = libs.versions.app.version.appId.get()
        minSdk = project.libs.versions.app.build.minimumSDK.get().toInt()
        targetSdk = project.libs.versions.app.build.targetSDK.get().toInt()
        versionName = project.libs.versions.app.version.versionName.get()
        versionCode = project.libs.versions.app.version.versionCode.get().toInt()
        setProperty("archivesBaseName", "gallery-$versionCode")
    }




    buildFeatures {
        viewBinding = true
        buildConfig = true
        //compose = true
    }

    buildTypes {

        release {
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )

        }
        create("pro_release") {
            applicationIdSuffix = ".pro"
            isMinifyEnabled = false
            isShrinkResources = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    /* flavorDimensions.add("licensing")
     productFlavors {
         register("foss")
         register("prepaid")
     }*/

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

    compileOptions {
        val currentJavaVersionFromLibs = JavaVersion.valueOf(libs.versions.app.build.javaVersion.get().toString())
        sourceCompatibility = currentJavaVersionFromLibs
        targetCompatibility = currentJavaVersionFromLibs
    }

    tasks.withType<KotlinCompile> {
        kotlinOptions.jvmTarget = project.libs.versions.app.build.kotlinJVMTarget.get()
    }

    namespace = libs.versions.app.version.appId.get()

    lint {
        checkReleaseBuilds = false
        abortOnError = false
    }

    packaging {
        resources {
            excludes += "META-INF/library_release.kotlin_module"
        }
    }
}
//noinspection UseTomlInstead
dependencies {
    implementation(project(":commons"))
    implementation(project(":doubletapplayerview"))
    implementation(project(":android-file-chooser"))
    implementation(libs.android.image.cropper)
    implementation(libs.exif)
    implementation(libs.android.gif.drawable)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.sanselan)
    implementation(libs.imagefilters)
    implementation(libs.androidsvg.aar)
    implementation(libs.androidx.swiperefreshlayout)
    implementation(libs.awebp)
    implementation(libs.apng)
    implementation(libs.okio)
    implementation(libs.picasso)
    implementation(libs.okhttp)

    ksp(libs.glide.compiler)
    implementation(libs.zjupure.webpdecoder)

    implementation(libs.bundles.room)
    ksp(libs.androidx.room.compiler)

    implementation("com.google.android.play:app-update-ktx:2.1.0")
    implementation("com.onesignal:OneSignal:5.1.6")

    implementation(platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-crashlytics-ktx")
    implementation("com.google.firebase:firebase-analytics-ktx")

    // implementation 'com.huawei.hms:video-editor-ui:1.1.0.303'

    implementation("com.ironsource.sdk:mediationsdk:7.3.1.1")
    implementation("com.ironsource.adapters:unityadsadapter:4.3.31")
    implementation("com.unity3d.ads:unity-ads:4.10.0")
    implementation("com.google.android.gms:play-services-appset:16.0.2")
    implementation("com.google.android.gms:play-services-ads-identifier:18.0.1")
    implementation("com.google.android.gms:play-services-basement:18.3.0")
// Define version constants
    val media3Version = "1.3.1"
    val androidxCoreVersion = "1.8.0"


    implementation("androidx.media3:media3-session:$media3Version")
    implementation("androidx.media3:media3-datasource:$media3Version")
    implementation("androidx.media3:media3-decoder:$media3Version")
    implementation("androidx.media3:media3-common:$media3Version")
    implementation("androidx.media3:media3-container:$media3Version")

    // Exclude transitive dependency from media3-exoplayer-dash
    implementation("androidx.media3:media3-exoplayer-dash:$media3Version") {
        exclude(group = "androidx.media3", module = "media3-exoplayer")
    }

    // Exclude transitive dependency from media3-exoplayer-hls
    implementation("androidx.media3:media3-exoplayer-hls:$media3Version") {
        exclude(group = "androidx.media3", module = "media3-exoplayer")
    }

    // Exclude transitive dependency from media3-exoplayer-smoothstreaming
    implementation("androidx.media3:media3-exoplayer-smoothstreaming:$media3Version") {
        exclude(group = "androidx.media3", module = "media3-exoplayer")
    }

    // Exclude transitive dependency from media3-exoplayer-rtsp
    implementation("androidx.media3:media3-exoplayer-rtsp:$media3Version") {
        exclude(group = "androidx.media3", module = "media3-exoplayer")
    }

    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("com.getkeepsafe.taptargetview:taptargetview:1.13.3")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.coordinatorlayout:coordinatorlayout:1.2.0")
    implementation("androidx.core:core:$androidxCoreVersion")
    implementation("com.ibm.icu:icu4j:74.2")
    implementation("com.arthenica:ffmpeg-kit-https:6.0-2.LTS")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.preference:preference:1.2.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")

    // Implementation of local projects
    implementation(project(":doubletapplayerview"))
    implementation(project(":android-file-chooser"))

    // Implementation of files in libs directory
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("lib-*.aar"))))
}

