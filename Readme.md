# EventBrite Backend

This backend App was built to carefully mimic most of the main functionalities of eventbrite's website .. added some interesting twists of mine too

## Entity Relationship Design (ERD):

- still in the works,
  link : [google drive link](https://drive.google.com/file/d/1qb7bFaIcIgAPtbLbdWXLl_9qBGn5lBK4/view?usp=share_link)

  ## Functionalities

User can :

- register,
- login ,
- logout -> using spring security and JWT for auth.

Users can become organizers and :

- create events
- create tickets specifically for type of event
- check in other users for created event

- other users can:
  - purchase tickets
  - find events by town
  - find events by organizer

## Under The Hood

implemented multiple Schedulers to run 24/7 (2-3 sec threads) to make changes in the database automatically

emails buyer a reciept after purchasing a ticket.

todo: use barcode in the email content

## Technologies

- Java 8+
- Spring (Boot, Security, Data JPA)
- Hibernate
- PostgreSQL
- Lombok
- JavaMail API
- JUnit, Mockito
