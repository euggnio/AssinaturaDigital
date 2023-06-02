plugins {
    id("java")
}

group = "com.codigo"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.apache.pdfbox:pdfbox:2.0.28")
    implementation("org.apache.pdfbox:pdfbox-tools:2.0.28")

    testImplementation("org.apache.pdfbox:pdfbox-examples:2.0.26")


    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    implementation("com.itextpdf:itextpdf:5.5.13.3")
    implementation("com.itextpdf:itext7-core:7.0.4")

    implementation("org.bouncycastle:bcprov-jdk15on:1.70")

}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}