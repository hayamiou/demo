plugins {
	kotlin("jvm") version "1.9.23"
	kotlin("plugin.spring") version "1.9.23"
	id("org.springframework.boot") version "3.5.13"
	id("io.spring.dependency-management") version "1.1.7"
	jacoco
	id("info.solidsoft.pitest") version "1.15.0"
	id("io.gitlab.arturbosch.detekt") version "1.23.6"
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
	implementation("org.springframework.boot:spring-boot-starter-jdbc")
	implementation("org.postgresql:postgresql")
	implementation("org.liquibase:liquibase-core")
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

		val testComponent by registering(JvmTestSuite::class) {
			sources {
				kotlin {
					setSrcDirs(listOf("src/testComponent/kotlin"))
					compileClasspath += sourceSets.main.get().output
					runtimeClasspath += sourceSets.main.get().output
				}
				resources {
					setSrcDirs(listOf("src/testComponent/resources"))
				}
			}
			targets {
				all {
					testTask.configure {
						shouldRunAfter(tasks.named("testIntegration"))
					}
				}
			}
		}

		val testArchitecture by registering(JvmTestSuite::class) {
			sources {
				kotlin {
					setSrcDirs(listOf("src/testArchitecture/kotlin"))
					compileClasspath += sourceSets.main.get().output
					runtimeClasspath += sourceSets.main.get().output
				}
			}
			targets {
				all {
					testTask.configure {
						shouldRunAfter(tasks.named("testComponent"))
					}
				}
			}
		}
	}
}

val testIntegrationImplementation: Configuration by configurations.getting {
	extendsFrom(configurations.implementation.get())
}

val testComponentImplementation: Configuration by configurations.getting {
	extendsFrom(configurations.implementation.get())
}

val testArchitectureImplementation: Configuration by configurations.getting {
	extendsFrom(configurations.implementation.get())
}

dependencies {
	testIntegrationImplementation("io.mockk:mockk:1.13.10")
	testIntegrationImplementation("io.kotest:kotest-assertions-core:5.9.1")
	testIntegrationImplementation("io.kotest:kotest-runner-junit5:5.9.1")
	testIntegrationImplementation("com.ninja-squad:springmockk:4.0.2")
	testIntegrationImplementation("io.kotest.extensions:kotest-extensions-spring:1.3.0")
	testIntegrationImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
	}
	testIntegrationImplementation("org.testcontainers:postgresql:1.19.1")
	testIntegrationImplementation("org.testcontainers:testcontainers:1.19.1")
	testIntegrationImplementation("io.kotest.extensions:kotest-extensions-testcontainers:2.0.2")
	testIntegrationImplementation("org.testcontainers:junit-jupiter:1.19.1")

	testComponentImplementation("io.cucumber:cucumber-java:7.14.0")
	testComponentImplementation("io.cucumber:cucumber-spring:7.14.0")
	testComponentImplementation("io.cucumber:cucumber-junit:7.14.0")
	testComponentImplementation("io.cucumber:cucumber-junit-platform-engine:7.14.0")
	testComponentImplementation("io.rest-assured:rest-assured:5.3.2")
	testComponentImplementation("org.junit.platform:junit-platform-suite:1.10.0")
	testComponentImplementation("org.testcontainers:postgresql:1.19.1")
	testComponentImplementation("io.kotest:kotest-assertions-core:5.9.1")
	testComponentImplementation("org.springframework.boot:spring-boot-starter-test") {
		exclude(module = "mockito-core")
	}
	testComponentImplementation("org.springframework.boot:spring-boot-starter-web")
	testComponentImplementation("org.testcontainers:junit-jupiter:1.19.1")

	testArchitectureImplementation("com.tngtech.archunit:archunit-junit5:1.3.0")
	testArchitectureImplementation("io.kotest:kotest-assertions-core:5.9.1")
	testArchitectureImplementation("io.kotest:kotest-runner-junit5:5.9.1")

	detektPlugins("io.gitlab.arturbosch.detekt:detekt-formatting:1.23.6")
}

jacoco {
	toolVersion = "0.8.11"
}

tasks.jacocoTestReport {
	dependsOn(tasks.test, tasks.named("testIntegration"))
	executionData.setFrom(
		fileTree(layout.buildDirectory).include(
			"jacoco/test.exec",
			"jacoco/testIntegration.exec"
		)
	)
	reports {
		xml.required = true
		html.required = true
	}
}

tasks.test {
	finalizedBy(tasks.jacocoTestReport)
}

tasks.named("testIntegration") {
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

detekt {
	config.setFrom(files("config/detekt.yml"))
	buildUponDefaultConfig = true
}