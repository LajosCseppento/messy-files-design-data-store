plugins {
    id("dev.lajoscseppento.ruthless") version "0.3.0"
}

rootProject.name = "messy-files-design-data-store"
include(
        "core",
        "arangodb",
        "orientdb"
)
