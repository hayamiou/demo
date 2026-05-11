# MethodologieTests - Gestion de livres

Projet fil rouge de méthodologie des tests — Master Expert Dev Web, Ynov Bordeaux.

## Stack technique

- **Kotlin** 1.9.23 + **Spring Boot** 3.5.13
- **PostgreSQL** + **Liquibase**
- **Gradle** (Kotlin DSL)

## Fonctionnalités

- Ajouter un livre (titre, auteur)
- Lister tous les livres (triés alphabétiquement)
- Réserver un livre (un livre déjà réservé ne peut pas être réservé à nouveau)

## Lancer l'application

Démarrer PostgreSQL (Docker) :
```bash
docker run --name postgres-demo -e POSTGRES_PASSWORD=password -e POSTGRES_DB=demo -p 5433:5432 -d postgres:15
```

Lancer l'application :
```bash
./gradlew bootRun
```

## Tests

| Commande | Description |
|---|---|
| `./gradlew test` | Tests unitaires |
| `./gradlew testIntegration` | Tests d'intégration |
| `./gradlew testComponent` | Tests de composant (Cucumber) |
| `./gradlew testArchitecture` | Tests d'architecture (ArchUnit) |
| `./gradlew build` | Build complet |
| `./gradlew detekt` | Analyse statique |
| `./gradlew pitest` | Tests de mutation |

## Architecture

src/
├── main/kotlin/com/example/demo/
│   ├── domain/
│   │   ├── model/          # Entités métier
│   │   ├── port/           # Interfaces (ports)
│   │   └── usecase/        # Logique métier
│   └── infrastructure/
│       ├── driven/         # Adaptateurs BDD (BookDAO)
│       └── driving/        # Adaptateurs REST (BookController)
└── test/                   # Tests unitaires
└── testIntegration/        # Tests d'intégration
└── testComponent/          # Tests de composant (Cucumber/Gherkin)
└── testArchitecture/       # Tests d'architecture (ArchUnit)

## Auteur

Muriel Ighmouracène — Ynov Bordeaux M1 Expert Dev Web 2025-2027