# Version 1.3
2019-07-29

* Support for Product, Protocol, ReactionComponent, TermocyclerProfile
* JSON Schema
* JSON Patch support
* RSQL support for complex filtering

# Version 1.2

2019-02-04

* Complete support for PCRPrimer
* API responses now include total count
* Added support for Service-to-Service authentication
* Updated documentation
* Updated dbi module

# Version 1.1

2018-09-07

* Authentication support (local, LDAP and Keycloak)
* Authorization support (group based)
* https support
* Liquibase support (database migration)
* Initial data validation

# Version 1.0

2018-07-17

* Initial project structure and setup
* Generic JSON-API support (including support for relationships)
* CRUD for 4 existing SeqDB entities (Region, PcrPrimer, PcrReaction, PctBatch)
* Initial unit and integration tests structure
* Support for in-memory database for tests (and quick local setup without RDBMS)
