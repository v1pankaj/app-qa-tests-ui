package qa.test.app.utils;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.UnreachableBrowserException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserDriver
{
   private static final Logger logger = LoggerFactory.getLogger(BrowserDriver.class);

   private static WebDriver mDriver;

   private BrowserDriver()
   {}

   //creates a driver or return current (never null)
   public static synchronized WebDriver getCurrentDriver()
   {
      if (mDriver == null)
      {
         try
         {
            mDriver = BrowserFactory.createDriver();
         }
         finally
         {
            Runtime.getRuntime().addShutdownHook(new Thread(new BrowserCleanup()));
         }
      }
      return mDriver;
   }

   public static void close()
   {
      try
      {
         if (mDriver != null)
         {
            mDriver.quit();
         }
         mDriver = null;
         logger.debug("closing the browser");
      }
      catch (UnreachableBrowserException e)
      {
         logger.error("cannot close browser: unreachable browser");
      }
   }

   public static void refresh()
   {
      try
      {
         if (mDriver != null)
         {
            mDriver.navigate().refresh();
         }
         logger.debug("Refreshing the browser");
      }
      catch (UnreachableBrowserException e)
      {
         logger.error("cannot refresh browser: browser unreachable ");
      }
   }

   private static class BrowserCleanup implements Runnable
   {
      public void run()
      {
         close();
      }
   }
}
