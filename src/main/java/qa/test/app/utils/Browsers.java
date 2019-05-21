package qa.test.app.utils;

public enum Browsers
{
   FIREFOX("firefox"), CHROME("chrome"), INTERNETEXPLORER("internetexplorer");

   private String name;

   Browsers(String name)
   {
      this.name = name;
   }

   public String getName()
   {
      return this.name;
   }

   public static Browsers getBrowser(String name)
   {
      for (Browsers browser : values())
      {
         if (browser.getName().equalsIgnoreCase(name))
         {
            return browser;
         }
      }
      return FIREFOX;
   }
}
