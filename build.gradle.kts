plugins {
    id("java")
}

group = "org.diaduck"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    implementation("net.dv8tion:JDA:5.2.2") { // replace $version with the latest version
        // Optionally disable audio natives to reduce jar size by excluding `opus-java`
        // Gradle DSL:
        // exclude module: 'opus-java'
        // Kotlin DSL:
        // exclude(module="opus-java")
    }
}

tasks.test {
    useJUnitPlatform()
}