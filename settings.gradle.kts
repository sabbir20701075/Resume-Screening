pluginManagement {

    repositories {
        google() // For Android and Google libraries
        mavenCentral() // Primary repository for most libraries
        gradlePluginPortal() // For Gradle plugins
        maven("https://jitpack.io")
        //jcenter()
        //maven { url=uri ("https://jcenter.bintray.com") }


    }

}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
       maven("https://jitpack.io")
        //jcenter()
        //maven { url=uri ("https://jcenter.bintray.com") }
        
    }
}

rootProject.name = "Resume Screening"
include(":app")


