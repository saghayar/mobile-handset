# mobile-handset
Mobile handset assignment

Goal
Design and implement a Java application following the requirements below.
Deliverables
• A fully running Java application.
• Source code: sent through email, or uploaded to a Git repository.
• Running instructions.
Technologies & frameworks:
Java 8, Spring Boot (preferably version 2.0+)
Requirements
At this URL: https://a511e938-a640-4868-939e-6eef06127ca1.mock.pstmn.io/handsets/list,
you will find a JSON file with a sample “Mobile handset” database. The data in this JSON is
static, I.e it doesn’t get updated.
1. Create a Spring Boot application exposing a search API (GET /mobile/search?) that will
allow the caller to retrieve one or more mobile handset record based on the passed
search criteria.
The criteria can be any field in the handset data along with any value. Examples:
• /search?priceEur=200. Will return 10 devices.
• /search?sim=eSim. Will return 18 devices.
• /search?announceDate=1999&price=200. Will return 2 devices.
2. Consume the JSON API in the best way you see suitable.
3. Create Unit Test cases as you see suitable.
4. Provide a documentation and a high-level architecture of your application. You can
do one or more diagrams, as you see suitable, to describe your application functionality.

#Running options
Project can be imported to one of IDEs or run using maven 

#Some Notes
Project uses embedded MongoDB database,when you run application it will be downloaded , it might take a few minutes (only first time)
