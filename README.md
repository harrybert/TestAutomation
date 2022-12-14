****About the Tests****

Following tests are created in the CreateBookingTests class.
 
    1. Create a new booking with given restrictions 
        + A room cannot be booked more than once for a given data
        + The checkout data must be greater than the check-in date
    2. Fetch the created booking by id
    3. Atleast 1 existing booking is returned n the response when you get all the booking
    4. Update the booking
    5. delete the booking.

**Requirements**

    maven should be installed
    JDK should be installed
    git installed
    intellij (optional)

''The tests can be run in two different ways'''

*Info:*

**file contains the baseURL in the Util directory**: 
      
      "utils/src/main/resources/env_config/env.properties".

***How to run the tests***

**Option 1**
       
    1. open cmd.
    2. switch to the directory where you want to clone the repository.
    3. Clone the repository to your local environment with "git clone https://mik27@bitbucket.org/TestAutomation/adjust.automation.git" command.
    4. Switch to the project root directory "cd adjust.automation"
    4. run the following maven commands to run the tests
         "mvn clean test -Dsurefire.suiteXmlFiles=src/test/resources/suites/Booking.xml"
    5. validate the result in console.


**Option 2**
 
    1. Follow the steps 1-3 from Option 1
    2. Open intellij
    3. import the project
    4. open "src/test/resources/suites/" directory in desktop project.
    5. right click on the xml file and run the tests.

*OR* 

    1. Follow the steps 1-3 from Option 1
    2. Open intellij
    3. import the project
    4. go to project desktop "/src/test/java/com/adjust/Tests/Booking/CreateBookingTests.java"
    5. run the whole class or individual tests (When running individual tests, tests should be run based on priority (Prio.1 should be run before prio. 2 and so on) as the tests are dependent on createBooking test.
