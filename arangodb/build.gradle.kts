plugins {
    id("dev.lajoscseppento.ruthless.spring-boot-application")
}

ruthless.lombok()

dependencies {
    implementation(project(":core"))
}

application {
    mainClass.set("dev.lajoscseppento.smartfiles.design.datastore.arangodb.ArangoDbApp")
}
