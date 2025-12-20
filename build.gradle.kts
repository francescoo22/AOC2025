plugins {
    kotlin("jvm") version "2.2.21"
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    implementation("com.google.ortools:ortools-java:9.14.6206")
}

kotlin {
    jvmToolchain(22)
}

tasks.test {
    useJUnitPlatform()
}