# EventBrite Backend

This backend App was built to carefully mimic the main functionalities of eventbrite's website .. added some interesting twists of mine too

User can :
-register,
-login ,
-logout -> using spring security and JWT for auth.

User can (organizer) :

- create
- create tickets specifically for type of event
- check in other users for created event

  - other users can:
    - purchase tickets
    - find events by town
    - find events by organizer

## Under The Hood

implemented multiple Schedulers to run 24/7 (2-3 sec threads) to make changes in the database automatically

coming soon: email buyer a reciept after purchasing a ticket.

## Technologies

- Java 8+
- Spring (Boot, Security, Data JPA)
- Hibernate
- PostgreSQL
- Lombok
- JUnit, Mockito
