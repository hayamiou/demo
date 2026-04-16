package com.example.demo

import com.tngtech.archunit.core.importer.ClassFileImporter
import com.tngtech.archunit.core.importer.ImportOption
import com.tngtech.archunit.lang.syntax.ArchRuleDefinition.noClasses
import io.kotest.core.spec.style.FunSpec

class ArchitectureTest : FunSpec({

    val basePackage = "com.example.demo"

    val importedClasses = ClassFileImporter()
        .withImportOption(ImportOption.DoNotIncludeTests())
        .importPackages(basePackage)

    test("domain should not depend on infrastructure") {
        noClasses()
            .that().resideInAPackage("$basePackage.domain..")
            .should().dependOnClassesThat()
            .resideInAPackage("$basePackage.infrastructure..")
            .check(importedClasses)
    }

    test("infrastructure driven should not depend on infrastructure driving") {
        noClasses()
            .that().resideInAPackage("$basePackage.infrastructure.driven..")
            .should().dependOnClassesThat()
            .resideInAPackage("$basePackage.infrastructure.driving..")
            .check(importedClasses)
    }
})