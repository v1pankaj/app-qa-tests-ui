Feature: New user account sign up
@newUser
Scenario: New user sign up

Given User is able to access the appDirect home page
When User clicks on "login" link
Then login page gets displayed
And User clicks on "Sign up for an account"
Then signup page gets displayed
When User provides email address as "test2@xyz.net" and clicks on "Sign Up" button
Then A message "Thanks for registering." gets displayed