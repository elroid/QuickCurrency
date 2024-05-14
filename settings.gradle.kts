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

rootProject.name = "Quick Currency"
include(":app")
include(":data:model")
include(":data:repository")
include(":data:remote")
include(":data:local")
include(":core:model")
include(":core:domain")
include(":core:common")
include(":feature:common")
include(":feature:converter")
