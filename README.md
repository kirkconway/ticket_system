
Setup and run instructions
-

requires a redis instance running on localhost:6379

run the "gradle:test" to run all the tests

or run the "gradle:bootRun" to start the application

Description of locking strategy
-

I used the locking mechanism provided by redisson using the ticket id as the key for the lock. The lock has a zero wait time so that secondary requests that cannot get the lock will fail immediately. 


AI tool usage and validation steps
-

initially used chat-gpt to generate the code based on prompts from the assignment.

once it got to a point where it looked like it was functional I moved over to intellij.

a few issues with the code were identified and fixed
- model was only half complete, seemed to think it only needed the model from the create request
- only 2 endpoints were implemented create and update
- no request dtos only the enitity for ticket
- the lock provided had a 5 second wait, so my locking test failed until i realised the problem