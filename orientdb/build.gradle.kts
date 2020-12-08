plugins {
    id("dev.lajoscseppento.ruthless.spring-boot-application")
}

dependencies {
    implementation(project(":core"))
}

application {
    mainClass.set("dev.lajoscseppento.smartfiles.design.datastore.orientdb.OrientDbApp")
}
