plugins {
    `kotlin-dsl`
}

repositories {
    gradlePluginPortal()
}

dependencies {
    implementation("org.springframework.boot:spring-boot-gradle-plugin:2.4.0")
    implementation("io.spring.gradle:dependency-management-plugin:1.0.10.RELEASE")
    implementation("com.diffplug.spotless:spotless-plugin-gradle:5.8.2")
}
