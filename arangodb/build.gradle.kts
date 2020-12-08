plugins {
    id("dev.lajoscseppento.ruthless.spring-boot-application")
}

ruthless.lombok()

dependencies {
    implementation(project(":core"))

//    implementation("com.google.guava:guava:$guavaVersion")
//    implementation("io.projectreactor:reactor-core")
//
//    implementation("com.fasterxml.jackson.core:jackson-databind")
//    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-csv")
//    implementation("com.fasterxml.jackson.datatype:jackson-datatype-guava")
//    implementation("com.fasterxml.jackson.datatype:jackson-datatype-jdk8")
//
//    implementation("org.springframework.boot:spring-boot-starter-actuator")
//    implementation("org.springframework.boot:spring-boot-starter-webflux")
//
//    developmentOnly("org.springframework.boot:spring-boot-devtools")
//
//    testImplementation("org.springframework.boot:spring-boot-starter-test") {
////        exclude(group = "org.junit.vintage", module = "junit-vintage-engine")
//    }
//    testImplementation("io.projectreactor:reactor-test")
}

application {
    mainClass.set("dev.lajoscseppento.smartfiles.design.datastore.arangodb.ArangoDbApp")
}
