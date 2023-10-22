This program demonstrates multithreaded producer-consumer pattern implemented in low-level Java native way.

The producer randomly creates commands on it's own which are being pushed to object-locked FIFO queue.
3 types of commands are implementing `Command` interface ( see the comments in the implementations ).

Consumer implements layered architecture (service -> DAO).
In memory H2 database is accessed by DAO layer which solves transaction clashing by object locking too.

Not really necessary, but entity -> DTO is done by mapstruct used in service layer.
Logback is being used for logging -- as it is most widely used logging facade with declarative way of configuration.

Testing is being performed by unit testing with help of JUnit 5. 

For further discussion: for this to become more than demonstration application, producer, consumer and commons parts should be separated as maven modules. 
The testing is done on `consumer` side (service, mapper, DAO) and it does not cover the source as for enterprise application needs; however as a demonstration of `Mockito` framework I find this plenty enough.


The application action can be seen by running the `ThreadingProducerConsumerTest::testProduceConsumeReporting` test or by packaging it the standard maven way which will compile out two artefacts one of which you can run by standard `java -jar produce-consume-34-mm-1.0-SNAPSHOT-jar-with-dependencies.jar` way.

