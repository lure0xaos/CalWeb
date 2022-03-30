val javaVersion: String = JavaVersion.VERSION_1_8.toString()

group = "gargoyle.cal"
version = "0.0.1-SNAPSHOT"

plugins {
    kotlin("jvm")
    java
}

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation(kotlin("reflect"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")
}

java.sourceCompatibility = JavaVersion.toVersion(javaVersion)

tasks.compileJava {
    options.encoding = Charsets.UTF_8.name()
}

tasks.test {
    useJUnitPlatform()
}
