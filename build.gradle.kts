plugins {
	kotlin("jvm") version "1.9.25"
	kotlin("plugin.spring") version "1.9.25"
	id("org.springframework.boot") version "3.5.13"
	id("io.spring.dependency-management") version "1.1.7"
	jacoco
	id("info.solidsoft.pitest") version "1.15.0"
}

group = "com.example"
version = "0.0.1-SNAPSHOT"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
	testImplementation("io.kotest:kotest-runner-junit5:5.9.1")
	testImplementation("io.kotest:kotest-assertions-core:5.9.1")
	testImplementation("io.kotest:kotest-property:5.9.1")
	testImplementation("io.mockk:mockk:1.13.10")
	testImplementation("io.kotest.extensions:kotest-extensions-pitest:1.2.0")
	testImplementation("com.ninja-squad:springmockk:4.0.2")
}

kotlin {
	compilerOptions {
		freeCompilerArgs.addAll("-Xjsr305=strict")
	}
}

tasks.withType<Test> {
	useJUnitPlatform()
}

jacoco {
	toolVersion = "0.8.11"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test)
	reports {
		xml.required = true
		html.required = true
	}
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}

pitest {
	junit5PluginVersion = "1.2.1"
	targetClasses = setOf("com.example.demo.domain.*")
	targetTests = setOf("com.example.demo.*", "com.example.demo.domain.*")
	outputFormats = setOf("HTML", "XML")
	timestampedReports = false
	useClasspathFile = true
	pitestVersion = "1.15.3"
}

testing {
	suites {
		val testIntegration by registering(JvmTestSuite::class) {
			sources {
				kotlin {
					setSrcDirs(listOf("src/testIntegration/kotlin"))
					compileClasspath += sourceSets.main.get().output
					runtimeClasspath += sourceSets.main.get().output
				}
				resources {
					setSrcDirs(listOf("src/testIntegration/resources"))
				}
			}
			targets {
				all {
					testTask.configure {
						shouldRunAfter(tasks.test)
					}
				}
			}
		}
	}
}

val testIntegrationImplementation: Configuration by configurations.getting {
	extendsFrom(configurations.implementation.get())
}

dependencies {
	testImplementation(project(":"))
	testImplementation(project(":"))
	testImplementation(project(":"))
	testImplementation(project(":"))
	testImplementation(project(":"))
	testIntegrationImplementation("io.mockk:mockk:1.13.10")
	testIntegrationImplementation("io.kotest:kotest-assertions-core:5.9.1")
	testIntegrationImplementation("io.kotest:kotest-runner-junit5:5.9.1")
	testIntegrationImplementation("com.ninja-squad:springmockk:4.0.2")
	testIntegrationImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
	testIntegrationImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
	}
}