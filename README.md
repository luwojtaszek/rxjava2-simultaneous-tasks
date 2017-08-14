# rxjava2-simultaneous-tasks
Simultaneous tasks execution using RxJava2

## Description

Using RxJava2 library I prepared ITask implementation (class: `ConcurrentTasksExecutor`) which takes given tasks (implementations of ITask) and executes them simultaneously using multiple threads.
Number of threads is mandatory parameter defined in constructor and must be higher than zero.


## Used technologies

* Spring Boot
* RxJava 2
* Lombok
* Google Guava

## Instruction

### Extract project

Extract project in some location on your local machine.

### Build application

Go to the project location:

```bash
cd /path/to/project/location
```

Build project with maven by executing command:

```bash
./mvnw clean install
```

### Start application

Execute .jar file:

```bash
java -jar target/rxjava2-simultaneous-tasks-0.0.1-SNAPSHOT.jar
```

By default application should be accessible under localhost:8080. There are only two endpoints described in next sections.

## Testing through the API

### Executing tasks simultaneously (multiple threads)

You can use API method to check how long will it take to execute couple of tasks simultaneously.

Assume that we want to test how long will take to execute 3 tasks using 2 threads.

Threads: 2

Tasks:

* Task1 - we define that this task should finished after 3 seconds
* Task2 - we define that this task should finished after 2 seconds
* Task3 - we define that this task should finished after 2 seconds

API call: GET http://localhost:8080/tasks/concurrent?task=3&task=2&task=2&threads=2

### Executing tasks sequentially (one thread)

Test the same example by executing tasks in one thread.

API call: GET http://localhost:8080/tasks/sequential?task=3&task=2&task=2