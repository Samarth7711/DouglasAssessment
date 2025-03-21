package BaseClass;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class DouglasProductFilterTest {
    private WebDriver driver;

    @BeforeClass
    public void setUp() {
        
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();
    }

    @Test(priority = 1)
    public void navigateToWebsite() {
        driver.get("https://www.douglas.de/de");
        Assert.assertTrue(driver.getTitle().contains("Douglas"), "Website did not load correctly.");
    }

    @Test(priority = 2)
    public void handleCookieConsent() {
        try {
            WebElement cookieButton = driver.findElement(By.xpath("//button[contains(text(),'Zustimmen')]"));
            if (cookieButton.isDisplayed()) {
                cookieButton.click();
            }
        } catch (Exception e) {
            System.out.println("Cookie consent not found or already accepted.");
        }
    }

    @Test(priority = 3)
    public void clickOnParfum() {
        WebElement parfumLink = driver.findElement(By.xpath("//a[contains(text(),'Parfum')]"));
        parfumLink.click();
        Assert.assertTrue(driver.getCurrentUrl().contains("parfum"), "Parfum page did not open.");
    }

    @Test(priority = 4, dataProvider = "filterCriteria")
    public void applyFilters(String category, String filterOption) {
        try {
            WebElement filter = driver.findElement(By.xpath("//button[contains(text(),'" + category + "')]"));
            filter.click();

            WebElement filterValue = driver.findElement(By.xpath("//label[contains(text(),'" + filterOption + "')]"));
            filterValue.click();

            List<WebElement> products = driver.findElements(By.cssSelector(".product-tile"));
            Assert.assertTrue(products.size() > 0, "No products found for filter: " + filterOption);
        } catch (Exception e) {
            System.out.println("Filter not found: " + category + " - " + filterOption);
        }
    }

    @DataProvider(name = "filterCriteria")
    public Object[][] filterData() {
        return new Object[][]{
                {"Marke", "Chanel"},
                {"Produktart", "Eau de Parfum"},
                {"Sale", "Ja"},
                {"Geschenk für", "Damen"},
                {"Für Wen", "Herren"},
                {"Neu", "Ja"},
                {"Limitier", "Ja"}
        };
    }

    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
