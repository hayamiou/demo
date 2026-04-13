package com.example.demo

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow

class CypherTest : StringSpec({

    "cypher('A', 2) should return 'C'" {
        cypher('A', 2) shouldBe 'C'
    }

    "cypher('Z', 1) should wrap around to 'A'" {
        cypher('Z', 1) shouldBe 'A'
    }

    "cypher('A', 0) should return 'A'" {
        cypher('A', 0) shouldBe 'A'
    }

    "cypher('A', 26) should return 'A' - full cycle" {
        cypher('A', 26) shouldBe 'A'
    }

    "cypher('A', 28) should behave like key=2" {
        cypher('A', 28) shouldBe 'C'
    }

    "cypher with negative key should throw IllegalArgumentException" {
        shouldThrow<IllegalArgumentException> {
            cypher('A', -1)
        }
    }
})