repositories {
    jcenter()
    mavenCentral()
}

plugins {
    `kotlin-dsl`
}

tasks.test {
    useJUnitPlatform()
}