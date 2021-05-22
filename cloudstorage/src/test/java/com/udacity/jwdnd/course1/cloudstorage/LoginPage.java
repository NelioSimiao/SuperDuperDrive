package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class LoginPage {
    @FindBy(id = "inputUsername")
    private WebElement usernameField;

    @FindBy(id = "inputPassword")
    private WebElement inputPasswordField;

    @FindBy(id="loginId")
    private WebElement loginButton;

    @FindBy(id = "logoutId")
    private WebElement logoutBtn;


    private WebDriver webDriver;


    public LoginPage(WebDriver webDriver) {
        this.webDriver = webDriver;
        PageFactory.initElements(webDriver, this);
    }

    public void fillLogin(String username, String password) {
        this.usernameField.sendKeys(username);
        this.inputPasswordField.sendKeys(password);
    }
    public void clickLoginButton(){
        loginButton.click();
    }


    public void clicklogoutBtn() {
        logoutBtn.click();
    }


}
