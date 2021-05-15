package com.udacity.jwdnd.course1.cloudstorage;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;

public class NotePage {

    @FindBy(id = "nav-notes-tab")
    private WebElement noteTab;

    @FindBy(id = "add-note-btn")
    private WebElement addNoteBtn;

    @FindBy(id = "note-title")
    private WebElement noteTitle;

    @FindBy(id = "note-description")
    private WebElement noteDescription;

    @FindBy(id = "note-save-button")
    private WebElement saveEditNoteButton;

    @FindBy(id = "note-editBtn")
    private WebElement editNoteBtn;


    @FindBy(id = "note-title-edit")
    private WebElement noteTitleEdit;

    @FindBy(id = "note-description-edit")
    private WebElement noteDescriptionEdit;

    @FindBy(id = "note-savechanges-btn")
    private WebElement noteEditSubmit;

    @FindBy(id = "note-deleteBtn")
    private WebElement deleteNoteBtn;

    @FindBy(id = "success-message")
    private WebElement successMessage;




    private WebDriver driver;

    public NotePage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }

    public void addNote(String title, String description) {
        this.noteTitle.sendKeys(title);
        this.noteDescription.sendKeys(description);
    }


    public void clickNoteTab() {
        noteTab.click();
    }

    public void clickAddNoteBtn() {
        addNoteBtn.click();
    }

    public void clickSaveEditNoteBtn() {
        this.saveEditNoteButton.click();
    }

    public void clickEditBtn() {
        this.editNoteBtn.click();
    }

    public void editNote(String title, String description) {
        this.noteTitleEdit.sendKeys(title);
        this.noteDescriptionEdit.sendKeys(description);

    }
    public void noteEditSubmitBtn() {
        this.noteEditSubmit.click();
    }


    public void clickDeleteNoteBtn() {
        this.deleteNoteBtn.click();
    }

    public boolean getSuccessMessage() {
        return this.successMessage.isDisplayed();
    }
}
