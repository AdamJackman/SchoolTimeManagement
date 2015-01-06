# Release & Iteration Planning

The User Stories we plan on implementing for our first release are:

* As Isaac, I want to be able to see my deadlines for assignments, so that I can manage my time effectively and not miss any deadlines.
* As Isaac, I want to be able to add assignments, so that I can stay on top of due dates.
* As Sam, I want be able to see my schedule, so that I can see upcoming deadlines.
* As Sam, I want to be able to log in and view assignments for my courses without having to add them myself.
* As Isaac,  I want to be able to add/remove my courses  to my schedule, so that I can keep track of my classes.

We feel that these user stories are all needed to create the MVP as well as relate to one another.
So in our first release we plan on having a user log in to our system through the console, and at this point there will be no username or password authentication. When the user has “logged in” he will be able to list his assignment deadlines using the CourseDAO and CalendarDAO. Which will issue a get request from our database and display them on the console for the user. In the same way he will be able to use the add command which will take an assignment name and due date as well as a percentage that it is worth and connect with the database and add the assignment. In this release there is no classmate approval of assignments. With the CourseDAO and CalendarDAO the user will be able to call the list schedule command which will display all assignment deadlines as well as the users lectures and tutorials, in order of the soonest events. This list of deadlines and schedules will show us all the deadlines for the course, which means that any user that has made an addition to the database will also have those assignments appear in the courses we have added as well. So when I say list deadlines we request the deadlines from the database without checking who actually added them. When a user wants to join a class they can run the add course command from our console and it will add them to the course they specified. Our code will connect to the database and add the user with the course code. Then when we list all the deadlines for a course, we find all the unique user entries in the table of courses then select all the assignments for each course we appear in and can display it to the user. 

Three user stories/features that we will not be implementing from the first release are:

* As Sam, I want a notification system that will remind me of deadlines so that I do not have to remember them myself.
* As Isaac, I want to add personal events to my calendar. So I have one central place to view all my tasks.
* As Isaac, I want to be able to organize a private group where I can notify other users of this system. So that I can easily organize study groups.

The reason we have excluded these from our first release is because we have identified these features as a level 3 (might be valuable but not crucial to the functionality).
The first one is the notification system. This feature could be valuable to implement and may be relevant but in our first release we are just focusing on our MVP and the main component of our system is adding courses and deadlines and allowing users to verify each others updates. Receiving these updates through email/phone is not necessary to achieve these goals, so for that reason we can leave this for a later release.
Adding personal events to a calendar is the same concept as it will help users organize themselves easier but it does not contribute to our MVP and is not a necessity to our first release as our product can work without it.
Organizing private groups that can add other users of our system is an even more advanced feature because this feature will be used by an even smaller subset of our users. This will be one of the last features will implement for our project because it contributes the least satisfaction to the majority of our users.

The stories we plan on implementing in our first iteration are:
As Isaac, I want to be able to add assignments, so that I can stay on top of due dates.
As Sam, I want to be able to log in and view assignments for my courses without having to add them myself.

For our first week we think the two most important stories to implement have to do with being able to view and add assignments.
