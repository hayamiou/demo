package com.example.demo

import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.property.Arb
import io.kotest.property.arbitrary.char
import io.kotest.property.arbitrary.int
import io.kotest.property.checkAll as kotestCheckAll

class CypherTest : StringSpec({

    // Tests classiques (exemples)
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

    // Property-based tests (invariants)
    "property: key=0 is identity" {
        kotestCheckAll(Arb.char('A'..'Z')) { c ->
            cypher(c, 0) shouldBe c
        }
    }

    "property: key=26 is full cycle - returns original char" {
        kotestCheckAll(Arb.char('A'..'Z')) { c ->
            cypher(c, 26) shouldBe c
        }
    }

    "property: cypher(c, key) == cypher(c, key % 26)" {
        kotestCheckAll(Arb.char('A'..'Z'), Arb.int(0..1000)) { c, key ->
            cypher(c, key) shouldBe cypher(c, key % 26)
        }
    }

    "property: cypher then reverse returns original char" {
        kotestCheckAll(Arb.char('A'..'Z'), Arb.int(0..25)) { c, key ->
            cypher(cypher(c, key), 26 - key) shouldBe c
        }
    }

    "property: keys are commutative" {
        kotestCheckAll(Arb.char('A'..'Z'), Arb.int(0..25), Arb.int(0..25)) { c, key1, key2 ->
            cypher(cypher(c, key1), key2) shouldBe cypher(cypher(c, key2), key1)
        }
    }

    "property: keys are additive" {
        kotestCheckAll(Arb.char('A'..'Z'), Arb.int(0..25), Arb.int(0..25)) { c, key1, key2 ->
            cypher(c, (key1 + key2) % 26) shouldBe cypher(cypher(c, key1), key2)
        }
    }
})