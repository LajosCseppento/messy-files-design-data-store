buildscript {
    repositories {
        gradlePluginPortal()
    }

    dependencies {
        classpath("dev.lajoscseppento.ruthless:ruthless-plugin:0.2.0")
    }
}

apply(plugin = "dev.lajoscseppento.ruthless")

rootProject.name = "messy-files-design-data-store"
include(
        "core",
        "arangodb",
        "orientdb"
)
