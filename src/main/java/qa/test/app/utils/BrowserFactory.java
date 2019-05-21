package qa.test.app.utils;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.UnexpectedAlertBehaviour;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BrowserFactory
{
   private static final Logger logger = LoggerFactory.getLogger(BrowserFactory.class);
   private static final String BROWSER_PROP_KEY = "browser";
   private static final String LOCALE_PROP_KEY = "locale";
   private static final String WINDOW_HIDE_PROP_KEY = "window.hide";
   private static final String SELENIUM_URL_PROP_KEY = "selenium.url";
   public static final String REF_DEFAULT_LOCALE = "en-us";

   private BrowserFactory()
   {}

   /**
    * Creates the browser driver specified in the system property "browser" if no property is set, then a firefox browser driver is created.
    * @return WebDriver
    */
   public static WebDriver createDriver()
   {
      WebDriver driver = null;
      URL targetUrl = getPropertySeleniumURL();
      Browsers browser = getPropertyBrowser();
      driver = createRemoteDriver(browser, targetUrl);
      return driver;
   }

   private static WebDriver createRemoteDriver(Browsers browser, URL targetUrl)
   {
      DesiredCapabilities capabilities = null;
      logger.info(String.format("Opening browser %s on %s", browser.toString(), targetUrl));
      switch (browser)
      {
      case CHROME:
         capabilities = getChromeCapabilities();
         break;
      case INTERNETEXPLORER:
         capabilities = getInternetExplorerCapabilities();
         break;
      case FIREFOX:
      default:
         capabilities = getFirefoxCapabilities();
      }
      return instantiateDriver(targetUrl, capabilities);
   }

   private static RemoteWebDriver instantiateDriver(URL targetUrl, DesiredCapabilities capabilities)
   {
      RemoteWebDriver driver = new RemoteWebDriver(targetUrl, capabilities);
      driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
      if (getPropertyWindowHide())
      {
         hideWindow(driver);
      }
      else
      {
         driver.manage().window().maximize();
      }
      return driver;
   }

   private static void hideWindow(RemoteWebDriver driver)
   {
      Dimension dim = new Dimension(1900, 1000);
      driver.manage().window().setSize(dim);
      driver.manage().window().setPosition(new Point(-1800, 0));
   }

   private static DesiredCapabilities getChromeCapabilities()
   {
      DesiredCapabilities dcap = DesiredCapabilities.chrome();
      ChromeOptions options = new ChromeOptions();
      List<String> optArgs = Arrays.asList("lang=" + getPropertyLocale(), "test-type");
      options.addArguments(optArgs);
      dcap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
      dcap.setCapability(ChromeOptions.CAPABILITY, options);
      dcap.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
      return dcap;
   }

   private static DesiredCapabilities getInternetExplorerCapabilities()
   {
      DesiredCapabilities dcap = DesiredCapabilities.internetExplorer();
      dcap.setCapability("requireWindowFocus", true);
      dcap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
      dcap.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
      return dcap;
   }

   private static DesiredCapabilities getFirefoxCapabilities()
   {
      DesiredCapabilities dcap = DesiredCapabilities.firefox();
      FirefoxProfile fp = new FirefoxProfile();
      fp.setPreference("intl.accept_languages", getPropertyLocale());
      dcap.setCapability(CapabilityType.UNEXPECTED_ALERT_BEHAVIOUR, UnexpectedAlertBehaviour.ACCEPT);
      dcap.setCapability(FirefoxDriver.PROFILE, fp);
      dcap.setCapability(CapabilityType.ForSeleniumServer.ENSURING_CLEAN_SESSION, true);
      return dcap;
   }

   /**
    * The allowed properties are firefox, internetexplorer and chrome e.g to run with chrome, pass in the option -Dbrowser=chrome at runtime
    * @return Browsers
    */
 
   private static Browsers getPropertyBrowser()
   {
      Browsers browser = null;
      browser = Browsers.getBrowser(Util.getSystemProperty(BROWSER_PROP_KEY, true, false));
      return browser;
   }

   /**
    * The allowed properties are firefox, internetexplorer and chrome e.g to run with chrome, pass in the option -Dbrowser=chrome at runtime
    * @return String
    */
   //@Nullable
   private static String getPropertyLocale()
   {
      String locale = Util.getSystemProperty(LOCALE_PROP_KEY, false, false);
      if (locale == null)
      {
         locale = REF_DEFAULT_LOCALE;
         logger.warn(String.format("Locale could not be found in the system properties. Default locale %s used.", locale));
      }

      return locale;
   }

   /**
    * The allowed properties are firefox, internetexplorer and chrome e.g to run with chrome, pass in the option -Dbrowser=chrome at runtime
    * @return String
    */
  // @Nullable
   private static boolean getPropertyWindowHide()
   {
      boolean windowHide = false;
      String windowHideProp = Util.getSystemProperty(WINDOW_HIDE_PROP_KEY, false, false);

      if (windowHideProp != null)
      {
         try
         {
            windowHide = Boolean.valueOf(windowHideProp);
         }
         catch (Exception e)
         {
            logger.warn(String.format("Cannot parse %s property. Must be a boolean", WINDOW_HIDE_PROP_KEY));
         }
      }
      else
      {
         logger.warn(WINDOW_HIDE_PROP_KEY + " property could not be found in the system properties. Window will be maximized.");
      }

      return windowHide;
   }

   /**
    * The allowed property is a url -Dselenium.url=http://localhost:4444/wd/hub at runtime
    * @return URL
    */
  // @Nullable
   private static URL getPropertySeleniumURL()
   {
      URL seleniumUrl = null;
      try
      {
         seleniumUrl = new URL(Util.getSystemProperty(SELENIUM_URL_PROP_KEY));
      }
      catch (MalformedURLException e)
      {
         e.printStackTrace();
      }
      return seleniumUrl;
   }
   
}
