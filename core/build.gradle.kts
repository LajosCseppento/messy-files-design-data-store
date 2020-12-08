plugins {
    id("dev.lajoscseppento.ruthless.spring-boot-library")
}

ruthless.lombok()

dependencies {
    api("org.springframework.boot:spring-boot-starter:2.4.0")
    implementation("com.google.jimfs:jimfs:1.1")
}
