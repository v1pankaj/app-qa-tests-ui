package qa.test.appDirect.features_ui;

import org.fluentlenium.core.FluentAdapter;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.When;
import qa.test.app.domain.LoginPage;
import cucumber.api.java.en.Then;
import org.fest.assertions.Assertions;

public class ManageLoginPage extends FluentAdapter
{
   public LoginPage loginPage = new LoginPage();
   public boolean status = true;

   @Given("^User is able to access the appDirect home page$")
   public void user_is_able_to_access_the_appDirect_home_page() throws Throwable
   {
      goTo(loginPage);
   }

   @When("^User clicks on \"(.*?)\" link$")
   public void user_clicks_on_link(String linkName) throws Throwable
   {
      boolean status = loginPage.clickOnLoginLink(loginPage, linkName);
   }

   @Then("^login page gets displayed$")
   public void login_page_gets_displayed() throws Throwable
   {
      Assertions.assertThat(status).as("login page displayed successfully").isTrue();
   }

   @Then("^User clicks on \"(.*?)\"$")
   public void user_clicks_on(String singUpLinkName) throws Throwable
   {
      boolean status = loginPage.clickOnSignUpLink(loginPage, singUpLinkName);
   }

   @Then("^signup page gets displayed$")
   public void signup_page_gets_displayed() throws Throwable
   {
      Assertions.assertThat(status).as("Signup page displayed successfully").isTrue();
   }

   @When("^User provides email address as \"(.*?)\" and clicks on \"(.*?)\" button$")
   public void user_provides_email_address_as_and_clicks_on(String emailAddress, String buttonName) throws Throwable
   {
      loginPage.fillEmailAddress(loginPage, emailAddress, buttonName);
   }

   @Then("^A message \"(.*?)\" gets displayed$")
   public void a_message_gets_displayed(String expMsg) throws Throwable
   {
      loginPage.getMessageDisplayed(loginPage, expMsg);
   }
}
