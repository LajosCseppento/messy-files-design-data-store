buildscript {
    repositories {
        mavenLocal()
        gradlePluginPortal()
    }

    dependencies {
        classpath("dev.lajoscseppento.ruthless:ruthless-plugin:0.1.1-SNAPSHOT")
    }
}

apply(plugin = "dev.lajoscseppento.ruthless")

rootProject.name = "smart-files-design-data-store"
include(
        "core",
        "arangodb",
        "orientdb"
)
