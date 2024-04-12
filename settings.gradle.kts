pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        jcenter()
        mavenCentral()
        maven {
            setUrl( "https://android-sdk.is.com/" )
        }
        maven {
            setUrl ("https://unity3ddist.jfrog.io/artifactory/unity-mediation-mvn-prod-local/")
        }
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        jcenter()
        mavenCentral()
        maven { setUrl("https://jitpack.io") }
        maven {
            setUrl( "https://android-sdk.is.com/" )
        }
        maven {
            setUrl ("https://unity3ddist.jfrog.io/artifactory/unity-mediation-mvn-prod-local/")
        }
    }
}

rootProject.name = "Player"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")
include(":app")

// TODO: This will be deprecated in future. Migrate to the newer `pluginManagement { includeBuild() }` mechanism instead of explicitly substituting dependency.
/*includeBuild("../Commons") {
    dependencySubstitution {
        substitute(module("com.cyberkey:commons")).using(project(":commons"))
    }
}*/
include(":doubletapplayerview",":android-file-chooser",":commons")
