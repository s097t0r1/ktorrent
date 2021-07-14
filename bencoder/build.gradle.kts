plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "me.s097t0r1"
version = "0.1"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.0.0-RC")
}
