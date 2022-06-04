val javaVersion: String = JavaVersion.VERSION_1_8.toString()

group = "gargoyle.cal"
version = "0.0.1-SNAPSHOT"

plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("reflect"))
}

java.sourceCompatibility = JavaVersion.toVersion(javaVersion)

tasks.compileJava {
    options.encoding = Charsets.UTF_8.name()
}

tasks.test {
    useJUnitPlatform()
}
