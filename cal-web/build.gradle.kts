import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

group = "gargoyle.cal"
version = "0.0.1-SNAPSHOT"

plugins {
    kotlin("jvm")
    application
}

application {
    mainClass.set("gargoyle.calendar.web.ApplicationKt")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=${project.ext.has("development")}")
}

tasks.processResources {
    filesMatching("**/*.properties") {
        filter { line ->
            { it: String ->
                when {
                    "project.build" == it -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm"))
                    project.ext.has(it) -> project.ext[it]
                    it.substringAfter("project.") in project.properties ->
                        project.properties[it.substringAfter("project.")]
                    it in project.properties -> project.properties[it]
                    else -> it
                }.toString()
            }.let { function ->
                line
                    .replace("\\$\\{([^}]+)\\}".toRegex()) { r -> function(r.groupValues[1]) }
                    .replace("@([^@]+)@".toRegex()) { r -> function(r.groupValues[1]) }
            }
        }
    }
}

repositories {
    mavenCentral()
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers/") }
}

dependencies {
    implementation(project(":cal-core"))
    implementation(project(":cal-cli"))

    implementation("com.google.code.gson:gson:2.10.1")

    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.9.1")
    implementation("io.ktor:ktor-server-html-builder:2.3.6")
    implementation("io.ktor:ktor-server-core:2.3.6")
    implementation("io.ktor:ktor-server-status-pages:2.3.6")
    implementation("org.jetbrains:kotlin-css-jvm:1.0.0-pre.156-kotlin-1.5.0")
    implementation("io.ktor:ktor-server-webjars:2.3.6")
    implementation("org.webjars:webjars-locator:0.48")
    implementation("org.webjars:jquery:3.7.1")
    implementation("org.webjars:bootstrap:5.3.2")
    implementation("org.webjars.npm:bootstrap-icons:1.11.1")
    implementation("org.webjars:popper.js:2.11.7")
    implementation("io.ktor:ktor-server-host-common:2.3.6")
    implementation("io.ktor:ktor-server-locations:2.3.6")
    implementation("io.ktor:ktor-server-sessions:2.3.6")
    implementation("io.ktor:ktor-server-netty:2.3.6")
    implementation("ch.qos.logback:logback-classic:1.4.11")
    testImplementation("io.ktor:ktor-server-tests:2.3.6")
    testImplementation(kotlin("test-junit"))
}
