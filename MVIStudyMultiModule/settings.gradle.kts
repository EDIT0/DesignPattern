pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "MVIStudyMultiModule"
include(":app")
include(":feature:home")
include(":core:base")
include(":core:database")
include(":core:di")
include(":core:model")
include(":feature:xml")
include(":feature:compose")
include(":core:util")
include(":domain")
include(":data")
