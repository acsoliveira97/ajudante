plugins {
	kotlin("jvm") version "2.2.21"
	kotlin("plugin.spring") version "2.2.21"
	kotlin("plugin.jpa") version "2.2.21"

	id("org.springframework.boot") version "4.0.2"
	id("io.spring.dependency-management") version "1.1.7"
}

group = "pt.cinzarosa"
version = "0.0.1-SNAPSHOT"
description = "Ajudante ajuda a registar limpezas diárias e a gerar relatórios mensais."

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	// --- implementation ---
	implementation("org.springframework.boot:spring-boot-starter-webmvc")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-thymeleaf")
	implementation("org.springframework.boot:spring-boot-starter-flyway")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
	implementation(libs.bootstrap)
	implementation(libs.springdoc.openapi.webmvc)

	// --- runtimeOnly ---
	runtimeOnly("org.postgresql:postgresql")

	// --- developmentOnly ---
	developmentOnly("org.springframework.boot:spring-boot-docker-compose")

	// --- testImplementation ---
	testImplementation(platform(libs.testcontainers.bom))
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.springframework.boot:spring-boot-testcontainers")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation(libs.instancio.junit)
	testImplementation(libs.testcontainers.junit)
	testImplementation(libs.testcontainers.postgresql)

	// --- testRuntimeOnly ---
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll(
			"-Xjsr305=strict",
			"-Xannotation-default-target=param-property"
		)
	}
}

allOpen {
	annotation("jakarta.persistence.Entity")
	annotation("jakarta.persistence.MappedSuperclass")
	annotation("jakarta.persistence.Embeddable")
}

tasks.withType<Test> {
	useJUnitPlatform()
}