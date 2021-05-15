package com.udacity.jwdnd.course1.cloudstorage;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.*;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class CloudStorageApplicationTests {

    @LocalServerPort
    private int port;

    public static WebDriver driver;

    public String baseURL;

    @BeforeAll
    static void beforeAll() {
        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    public void beforeEach() throws InterruptedException {
        this.driver = new ChromeDriver();
        baseURL = "http://localhost:" + port;
    }

    @AfterEach
    public void afterEach() throws InterruptedException {
        if (this.driver != null) {
            Thread.sleep(3000);
            driver.quit();
            //driver=null;
        }
    }

    @Test
    @Order(1)
    public void testUnauthorizedUser() {
        driver.get(baseURL + "/home");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.fillLogin("Nel", "0123");
        loginPage.clickLoginButton();
        Assertions.assertEquals("Login", driver.getTitle());


    }

    @Test
    @Order(2)
    public void testLoginUserWithoutSignUp() throws InterruptedException {
        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.fillLogin("Nel", "0123");
        Thread.sleep(2000);
        loginPage.clickLoginButton();
        Thread.sleep(2000);

        new WebDriverWait(driver, 4).until(ExpectedConditions.titleIs("Login"));

        Assertions.assertEquals("Login", driver.getTitle());
    }


    @Test
    @Order(3)
    public void testSignUpUser() {
        driver.get(baseURL + "/signup");
        SignUpPage signUpPage = new SignUpPage(driver);
        signUpPage.fillSignUp("Neylon", "Muteu", "Nel", "0123");
        signUpPage.clickSignUpButton();
        Assertions.assertEquals("Sign Up", driver.getTitle());

    }

    @Test
    @Order(4)
    public void testLogoutUser() throws InterruptedException {
        driver.get(baseURL + "/login");

        Assertions.assertEquals("Login", driver.getTitle());
    }

    @Test
    @Order(5)
    public void testLoginUserSignUpAndLogout() throws InterruptedException {
        testSignUpUser();
        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.fillLogin("Nel", "0123");
        loginPage.clickLoginButton();
        Thread.sleep(2000);

        Assertions.assertEquals("Home", driver.getTitle());
        Thread.sleep(3000);
        HomePage homePage = new HomePage(driver);
        homePage.clickLogoutButton();
        Thread.sleep(3000);
        Assertions.assertEquals("Login", driver.getTitle());


    }


    @Test
    @Order(6)
    public void testNotesCreateEditDeleteAndList() throws InterruptedException {
        login();
        driver.get(baseURL + "/home");
        NotePage notePage = new NotePage(driver);
        notePage.clickNoteTab();
        Thread.sleep(2000);
        notePage.clickAddNoteBtn();
        Thread.sleep(2000);
        notePage.addNote("glass","Teste the glas");
        Thread.sleep(2000);
        notePage.clickSaveEditNoteBtn();
        Thread.sleep(2000);
        notePage.clickEditBtn();
        Thread.sleep(2000);
        notePage.editNote("glass Edited","Teste the glas");
        Thread.sleep(2000);
        notePage.noteEditSubmitBtn();
        Thread.sleep(2000);
        notePage.clickDeleteNoteBtn();

        Assertions.assertTrue(notePage.getSuccessMessage());


    }

    @Test
    @Order(6)
    public CredentialPage testCreateCredentialAndList() throws InterruptedException {
        login();
        driver.get(baseURL + "/home");
        CredentialPage credentialPage = new CredentialPage(driver);
        credentialPage.clickCredTab();
        Thread.sleep(2000);
        credentialPage.clickAddCredBtn();
        Thread.sleep(2000);
        credentialPage.fillCredential("https://classroom.udacity.com/nanodegrees/","Nelio","0123");
        Thread.sleep(2000);
        credentialPage.saveCredBtn();
        Assertions.assertTrue(credentialPage.getSuccessMessage());
        return credentialPage;

    }

    @Test
    @Order(7)
    public void testEditCredentialAndDelete() throws InterruptedException {
        CredentialPage cre = testCreateCredentialAndList();
        Thread.sleep(2000);
        cre.clickEditBtn();
        Thread.sleep(2000);
        cre.saveCredBtn();
        Thread.sleep(2000);
        cre.clickDeleteBtn();
        Thread.sleep(2000);
        Assertions.assertFalse(cre.getSuccessMessage());

    }

    public WebDriver login() throws InterruptedException {
        testSignUpUser();
        driver.get(baseURL + "/login");
        LoginPage loginPage = new LoginPage(driver);
        loginPage.fillLogin("Nel", "0123");
        loginPage.clickLoginButton();
        Thread.sleep(2000);
        return driver;

    }


}
