package qa.test.app.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Util
{
   private static final Logger logger = LoggerFactory.getLogger(Util.class);
  // @Nullable
   public static String getSystemProperty(String property, boolean logWarning, boolean failOnError)
   {
      String serviceUrl = null;
      if (System.getProperty(property) != null)
         serviceUrl = System.getProperty(property);
      else
      {
         if (logWarning)
            logger.warn("Property not found : " + property);
         if (failOnError)
            throw new RuntimeException("Exiting test. Property not found : " + property);
      }
      return serviceUrl;
   }

  // @Nullable
   public static String getSystemProperty(String property)
   {
      String serviceUrl = null;
      if (System.getProperty(property) != null)
         serviceUrl = System.getProperty(property);
      else
         logger.warn("Property not found : " + property);
      return serviceUrl;
   }
   
   public static void waitForMillis(int milliSeconds)
   {
      try
      {
         Thread.sleep(milliSeconds);
      }
      catch (InterruptedException e)
      {
         e.printStackTrace();
      }
   }
}
