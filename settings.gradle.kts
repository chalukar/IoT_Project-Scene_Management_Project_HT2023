pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        google()
        maven { url = uri("https://repo.eclipse.org/content/repositories/paho-snapshots/") }
    }

}

rootProject.name = "IoT Automate Scene Management Project HT2023"
include(":app")
 