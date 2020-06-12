package com.cybertek.library.utilities.ui;


import com.cybertek.library.utilities.common.Environment;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;


public class Driver {
    private static final String GRID_URL = "http://100.26.171.118:4444/wd/hub";
    public static final String USERNAME = "vasylfomiuk1";
    public static final String AUTOMATE_KEY = "shPeppvXmzdSTZqAZH3f";
    public static final String URL = "https://" + USERNAME + ":" + AUTOMATE_KEY + "@hub-cloud.browserstack.com/wd/hub";

    private Driver() {
    }

    private static ThreadLocal<WebDriver> driverPool = new ThreadLocal<>();


    public static synchronized WebDriver getDriver() {
        if (driverPool.get() == null) {

            // check the command line argument browser. if it has value, use that value
            // if no browser value is passed from command line, the user properties file
            // mvn test -Dbrowser=remote-chrome
            // mvn test -Dbrowser=remote-firefox
            // mvn test -Dcucumber.filter.tags=@regression -Dbrowser=remote-firefox
            String browser = System.getProperty("browser") != null ? System.getProperty("browser") : Environment.getProperty("browser");

            switch (browser) {
                case "chrome":
                    WebDriverManager.chromedriver().setup();
                    driverPool.set(new ChromeDriver());
                    break;
                case "chrome-headless":
                    WebDriverManager.chromedriver().setup();
                    driverPool.set(new ChromeDriver(new ChromeOptions().setHeadless(true)));
                    break;
                case "firefox":
                    WebDriverManager.firefoxdriver().setup();
                    driverPool.set(new FirefoxDriver());
                    break;
                case "firefox-headless":
                    WebDriverManager.firefoxdriver().setup();
                    driverPool.set(new FirefoxDriver(new FirefoxOptions().setHeadless(true)));
                    break;
                case "ie":
                    if (!System.getProperty("os.name").toLowerCase().contains("windows"))
                        throw new WebDriverException("Your OS doesn't support Internet Explorer");
                    WebDriverManager.iedriver().setup();
                    driverPool.set(new InternetExplorerDriver());
                    break;

                case "edge":
                    if (!System.getProperty("os.name").toLowerCase().contains("windows"))
                        throw new WebDriverException("Your OS doesn't support Edge");
                    WebDriverManager.edgedriver().setup();
                    driverPool.set(new EdgeDriver());
                    break;

                case "safari":
                    if (!System.getProperty("os.name").toLowerCase().contains("mac"))
                        throw new WebDriverException("Your OS doesn't support Safari");
                    WebDriverManager.getInstance(SafariDriver.class).setup();
                    driverPool.set(new SafariDriver());
                    break;

                case "remote-chrome":
                    try {
                        URL url = new URL(GRID_URL);
                        ChromeOptions chromeOptions = new ChromeOptions();
                        driverPool.set(new RemoteWebDriver(url, chromeOptions));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "remote-firefox-linux":
                    try {
                        URL url = new URL(GRID_URL);
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        firefoxOptions.setCapability("platform", Platform.LINUX);
                        driverPool.set(new RemoteWebDriver(url, firefoxOptions));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                case "remote-firefox-win":
                    try {
                        URL url = new URL(GRID_URL);
                        FirefoxOptions firefoxOptions = new FirefoxOptions();
                        firefoxOptions.setCapability("versopm", "asd");
                        driverPool.set(new RemoteWebDriver(url, firefoxOptions));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                case "remote-safari":
                    try {
                        URL url = new URL(GRID_URL);
                        SafariOptions s = new SafariOptions();
                        driverPool.set(new RemoteWebDriver(url, s));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                case "browser-stack-chrome":
                    try {
                        URL url = new URL(URL);
                        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
                        desiredCapabilities.setCapability("browser", "Chrome");
                        desiredCapabilities.setCapability("browser_version", "83.0");
                        desiredCapabilities.setCapability("os", "Windows");
                        desiredCapabilities.setCapability("os_version", "10");
                        desiredCapabilities.setCapability("resolution", "1920x1080");
                        desiredCapabilities.setCapability("name", "Library Automation");
                        driverPool.set(new RemoteWebDriver(url, desiredCapabilities));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;
                case "browser-stack-android":
                    try {
                        URL url = new URL(URL);
                        DesiredCapabilities caps = new DesiredCapabilities();
                        caps.setCapability("browserName", "android");
                        caps.setCapability("device", "Samsung Galaxy S20");
                        caps.setCapability("realMobile", "true");
                        caps.setCapability("os_version", "10.0");
                        caps.setCapability("name", "Library Automation");
                        driverPool.set(new RemoteWebDriver(url, caps));
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        }
        return driverPool.get();
    }

    public static void closeDriver() {
        if (driverPool.get() != null) {
            driverPool.get().quit();
            driverPool.remove();
        }
    }
}
