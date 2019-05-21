package qa.test.appDirect.features_ui;

import org.junit.runner.RunWith;

import cucumber.api.junit.Cucumber;

@SuppressWarnings("deprecation")
@RunWith(Cucumber.class)
@Cucumber.Options(format = { "pretty", "html:target/cucumber", "json:target/cucumber.json" }, tags = { "~@ignore" })
public class ITRunCukesUI
{}
