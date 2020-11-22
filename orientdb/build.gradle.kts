plugins {
    id("dev.lajoscseppento.smartfiles.design.datastore.java-application-conventions")
}

dependencies {
    implementation(project(":core"))
}

application {
    mainClass.set("dev.lajoscseppento.smartfiles.design.datastore.orientdb.OrientDbApp")
}
