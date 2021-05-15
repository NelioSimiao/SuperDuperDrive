package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class CredentialPage {

    @FindBy(id = "nav-credentials-tab")
    private WebElement noteTab;

    @FindBy(id = "add-cred-btn")
    private WebElement addCredBtn;


    @FindBy(id = "credential-url")
    private WebElement credentialUrl;

    @FindBy(id = "credential-password")
    private WebElement credentialPassword;

    @FindBy(id = "credential-username")
    private WebElement credentialUsername;

    @FindBy(id = "saveCredentialSubmit")
    private WebElement submitBtn;

    @FindBy(id = "cred-EditBtn")
    private WebElement editBtn;

    @FindBy(id = "cred-DeleteBtn")
    private WebElement deleteBtn;

    @FindBy(id = "success-message")
    private WebElement successMessage;

    private WebDriver driver;

    public CredentialPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void clickCredTab() {
        noteTab.click();
    }

    public void clickAddCredBtn() {
        addCredBtn.click();
    }

    public void fillCredential(String url, String username, String password) {
        this.credentialUrl.sendKeys(url);
        this.credentialUsername.sendKeys(username);
        this.credentialPassword.sendKeys(password);
    }

    public void saveCredBtn() {
        this.submitBtn.click();
    }

    public boolean getSuccessMessage() {
        return this.successMessage.isDisplayed();
    }

    public void clickEditBtn() {
        editBtn.click();
    }

    public void clickDeleteBtn() {
        deleteBtn.click();
    }


}
