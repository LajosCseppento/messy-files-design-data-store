import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    id("dev.lajoscseppento.smartfiles.design.datastore.java-common-conventions")
    `java-library`
}

tasks.getByName<Jar>("jar") {
    enabled = true
}

tasks.getByName<BootJar>("bootJar") {
    enabled = false
}
