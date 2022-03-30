import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

val ktorVersion: String = "1.6.8"
val kotlinVersion: String = "1.6.10"
val logbackVersion: String = "1.2.11"

group = "gargoyle.cal"
version = "0.0.1-SNAPSHOT"

plugins {
    application
    kotlin("jvm")
}

application {
    mainClass.set("gargoyle.calendar.web.ApplicationKt")

    val isDevelopment: Boolean = project.ext.has("development")
    applicationDefaultJvmArgs = listOf("-Dio.ktor.development=$isDevelopment")
}

tasks.processResources {
    filesMatching("**/*.properties") {
        filter { line ->
            { it: String ->
                when {
                    "project.build" == it -> LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd-HHmm"))
                    project.ext.has(it) -> project.ext[it]
                    it.substringAfter("project.") in project.properties -> project.properties[it.substringAfter("project.")]
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
    maven { url = uri("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers") }
}

dependencies {
    implementation(project(":cal-core"))
    implementation(project(":cal-cli"))

    implementation("com.google.code.gson:gson:2.9.0")

    implementation(kotlin("stdlib"))
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine")

    implementation("org.jetbrains.kotlinx:kotlinx-html-jvm:0.7.3")
    implementation("io.ktor:ktor-html-builder:$ktorVersion")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
    implementation("org.jetbrains:kotlin-css-jvm:1.0.0-pre.129-kotlin-1.4.20")
    implementation("io.ktor:ktor-webjars:$ktorVersion")
    implementation("org.webjars:webjars-locator:0.45")
    implementation("org.webjars:jquery:3.6.0")
    implementation("org.webjars:bootstrap:5.1.3")
    implementation("org.webjars.npm:bootstrap-icons:1.8.1")
    implementation("org.webjars:popper.js:2.9.3")
    implementation("io.ktor:ktor-server-host-common:$ktorVersion")
    implementation("io.ktor:ktor-locations:$ktorVersion")
    implementation("io.ktor:ktor-server-sessions:$ktorVersion")
    implementation("io.ktor:ktor-server-netty:$ktorVersion")
    implementation("ch.qos.logback:logback-classic:$logbackVersion")
    testImplementation("io.ktor:ktor-server-tests:$ktorVersion")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit:$kotlinVersion")
}
