# tribefire.extension.hibernate

## Main module:

* [hibernate-module](hibernate-module/readme.md)

## See also:
* [hibernate-accesses-edr2cc-module](hibernate-accesses-edr2cc-module/readme.md)
* [hibernate-leadership-edr2cc-module](hibernate-leadership-edr2cc-module/readme.md)
* [hibernate-shared-md-priming](hibernate-shared-md-priming/readme.md)


## Hibernate 5 compatibility

For Hibernate 5 compatibility special steps need to be made!!!

### ID sequences

**Problem:**
Hibernate 5 was using a single sequence for generating IDs of all entities, Hibernate 6 has sequence per entity. Thus, deploying Hibernate 6 as is would create new sequences and start assigning IDs from 1, even if there are already existing entities with higher Ids.

**Solution:**
Add the following property to your `HibernateAccess.properties`:
> hibernate.id.db_structure_naming_strategy=single

