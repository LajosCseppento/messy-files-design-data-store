plugins {
    id("dev.lajoscseppento.ruthless.spring-boot-application")
}

ruthless.lombok()

dependencies {
    implementation(project(":core"))
    implementation("com.arangodb:arangodb-java-driver:6.16.0")
    implementation("com.arangodb:jackson-dataformat-velocypack:3.0.0")
}

application {
    mainClass.set("dev.lajoscseppento.messyfiles.design.datastore.arangodb.ArangoDbApp")
}
