plugins {
    id("java")
}

group = "org.diaduck"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.dv8tion:JDA:5.2.2") {
        // Optionally disable audio natives to reduce jar size by excluding `opus-java`
        exclude(module = "opus-java")
    }
    implementation("ch.qos.logback:logback-classic:1.4.11") // Correct placement for logback
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}
