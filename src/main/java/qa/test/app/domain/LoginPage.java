package qa.test.app.domain;

import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.domain.FluentList;
import org.fluentlenium.core.filter.FilterConstructor;
import org.openqa.selenium.WebDriver;

import qa.test.app.utils.BrowserDriver;
import qa.test.app.utils.UiUtils;
import qa.test.app.utils.Util;
import qa.test.app.utils.UiUtils.AwaitType;

import org.fest.assertions.Assertions;

public class LoginPage extends FluentPage
{
   private final WebDriver driver;
   private static final String APP_BASE_URL = Util.getSystemProperty("app.url");
   private static final String APP_LOGIN_URL = "/login?";

   public LoginPage()
   {
      driver = BrowserDriver.getCurrentDriver();
      initFluent(driver);
      initTest();
   }

   @Override
   public String getUrl()
   {
      return APP_BASE_URL;
   }

   // @Override
   public void isAt()
   {
      UiUtils.awaitUntilPageStartsWithUrl(getUrl(), UiUtils.getPropertyWaitTimeoutSeconds());
   }

   public boolean clickOnLoginLink(FluentPage page, String linkName)
   {
      FluentList fl = page.find("li", FilterConstructor.withClass(linkName));

      if (fl.size() == 1)
      {
         fl.click();
         UiUtils.awaitUntil(page, "#login_page", AwaitType.ISPRESENT);
         
         return true;
      }
      return false;
   }
   
   public boolean clickOnSignUpLink(FluentPage page, String linkName)
   {
      FluentList div1 = page.find("div", FilterConstructor.withClass("adb-footnote"));
      FluentList div2 = div1.find("p a");
      Assertions.assertThat(div2.getText()).isEqualTo(linkName);
      if (div2.size() == 1)
      {
         div2.click();
         UiUtils.awaitUntil(page, ".adb-layout-dialog", AwaitType.ISPRESENT);

         return true;
      }
      return false;
   }
   
   public void fillEmailAddress(FluentPage page, String emailAddress, String buttonName)
   {
      fill("input").with(emailAddress);
      FluentList fl = page.find("button", FilterConstructor.withText(buttonName));
      fl.click();
   }
   
   public void getMessageDisplayed(FluentPage page, String expMsg)
   {
      FluentList div1 = page.find("div", FilterConstructor.withClass("adb-local_alert--content")).find("h3");
      String actMsg = div1.getText();
      
      Assertions.assertThat(actMsg).isEqualTo(expMsg);
   }
}
