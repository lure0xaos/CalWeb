val javaVersion: String = JavaVersion.VERSION_1_8.toString()

group = "gargoyle.cal"
version = "0.0.1-SNAPSHOT"
description = "Cal"

plugins {
    java
    kotlin("jvm") version "1.6.10"
}

repositories {
    mavenCentral()
}
