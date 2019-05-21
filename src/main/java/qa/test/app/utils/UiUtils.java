package qa.test.app.utils;

import java.util.concurrent.TimeUnit;

import org.fluentlenium.core.FluentPage;
import org.fluentlenium.core.wait.FluentWait;
import org.fluentlenium.core.wait.FluentWaitMatcher;
import org.openqa.selenium.TimeoutException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import qa.test.app.utils.Util;

public class UiUtils
{
   private static final Logger logger = LoggerFactory.getLogger(UiUtils.class);
   private static final String WAIT_TIMEOUT_PROP_KEY = "wait.timeout";
   private static final int REF_REFRESH_MAX_WAIT_SECONDS = 10;
   
   private UiUtils()
   {}
   
   public enum AwaitType
   {
      HASSIZEEQUAL, HASSIZEGREATEROREQUAL, AREENABLED, HASTEXT, AREDISPLAYED, ISPRESENT, ISNOTPRESENT, ARENOTDISPLAYED;
   }
   
   public static int getPropertyWaitTimeoutSeconds()
   {
      int uiWaitTimeout = REF_REFRESH_MAX_WAIT_SECONDS;
      String uiWaitTimeoutParam = Util.getSystemProperty(WAIT_TIMEOUT_PROP_KEY, false, false);
      if (uiWaitTimeoutParam != null)
      {
         try
         {
            uiWaitTimeout = Integer.parseInt(uiWaitTimeoutParam);
         }
         catch (Exception e)
         {
            logger.debug(e.getMessage());
         }
      }
      return uiWaitTimeout;
   }
   
   public static void awaitUntilPageStartsWithUrl(String expUrl, int waitTimeOutSec)
   {
      int iter = 0;
      while (iter++ < waitTimeOutSec && !BrowserDriver.getCurrentDriver().getCurrentUrl().startsWith(expUrl))
      {
         Util.waitForMillis(1000);
      }
      if (!BrowserDriver.getCurrentDriver().getCurrentUrl().startsWith(expUrl))
      {
         throw new TimeoutException(String.format("URL does not start with %s after %d seconds", expUrl, waitTimeOutSec));
      }
   }
   
   public static void awaitUntil(FluentPage page, String findPattern, AwaitType awaitType)
   {
      try
      {
         FluentWaitMatcher matcher = await(page).until(findPattern);
         callCorrespondingMatcher(matcher, awaitType);
      }
      catch (TimeoutException t)
      {
         logger.error(t.getMessage());
         throw new TimeoutException(t);
      }
   }
   
   private static FluentWait await(FluentPage page)
   {
      return page.await().pollingEvery(250, TimeUnit.MILLISECONDS).atMost(getPropertyWaitTimeoutSeconds(), TimeUnit.SECONDS);
   }
   
   private static void callCorrespondingMatcher(FluentWaitMatcher matcher, AwaitType awaitType)
   {
      if (awaitType.equals(AwaitType.AREENABLED))
         matcher.areEnabled();
      else if (awaitType.equals(AwaitType.AREDISPLAYED))
         matcher.areDisplayed();
      else if (awaitType.equals(AwaitType.ARENOTDISPLAYED))
         matcher.areNotDisplayed();
      else if (awaitType.equals(AwaitType.ISPRESENT))
         matcher.isPresent();
      else if (awaitType.equals(AwaitType.ISNOTPRESENT))
         matcher.isNotPresent();
   }
}
