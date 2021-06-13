plugins {
    id("dev.lajoscseppento.ruthless") version "0.2.0"
}

rootProject.name = "messy-files-design-data-store"
include(
        "core",
        "arangodb",
        "orientdb"
)
