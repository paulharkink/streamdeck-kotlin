import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	id("org.springframework.boot") version "3.1.3"
	id("io.spring.dependency-management") version "1.1.3"
	kotlin("jvm") version "1.8.22"
	kotlin("plugin.spring") version "1.8.22"
}

group = "nl.paulharkink.streamdeck.api"
version = "0.0.1-SNAPSHOT"

java {
	sourceCompatibility = JavaVersion.VERSION_17
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
	maven { setUrl("https://jitpack.io") } // jackson-module-kogera
}

dependencies {
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	implementation("info.picocli:picocli-spring-boot-starter:4.7.5")
	implementation("org.springframework:spring-websocket")
	implementation("jakarta.websocket:jakarta.websocket-client-api")
//	implementation("com.fasterxml.jackson.module:jackson-module-kotlin") //:2.12.5
	// jackson-module-kotlin can't handle value-classes, see: https://github.com/FasterXML/jackson-module-kotlin/issues/650
	implementation("com.github.ProjectMapK:jackson-module-kogera:2.15.2-beta3")

	implementation("org.springframework.boot:spring-boot-starter-websocket")

	// KoTest
	testImplementation("io.kotest:kotest-runner-junit5:5.6.2")
	testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
}
tasks {
	val copyJar by registering(Copy::class) {
		dependsOn("bootJar", "jar")  // Make sure the jar is built before copying

		from("$buildDir/libs") {
			include("*.jar")
			exclude("*-plain.jar")
		}
		into("../streamdeck-plugin/backend")
		rename {"app.jar"}

		duplicatesStrategy = DuplicatesStrategy.INCLUDE
	}

    named("bootJar") {
        finalizedBy(copyJar)
    }
}

tasks.withType<KotlinCompile> {
	kotlinOptions {
		freeCompilerArgs += "-Xjsr305=strict"
		jvmTarget = "17"
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}
