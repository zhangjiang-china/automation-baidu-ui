package page;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.How;
import org.openqa.selenium.support.PageFactory;

public class SearchPage {
    WebDriver driver;
    
    @FindBy(how = How.CSS, using = "input#kw")
    private WebElement searchInput;
    
    @FindBy(how = How.CSS, using = "input[type='submit']")
    private WebElement searchBtn;
    
    @FindBy(how = How.XPATH, using = "//div[@id='page']//a[@class='n'][last()]")
    private WebElement nextPageBtn;
    
    @FindBy(how = How.XPATH, using = "//div[@id='page']//a[@class='n'][1]")
    private WebElement previousPageBtn;
    
    @FindBy(how = How.XPATH, using = "//span[contains(@class,'page-item')][text()=2]")
    private WebElement secondPageBtn;

    @FindBy(how = How.XPATH, using = "//span[@name='tj_settingicon']")
    private WebElement settingIcon1;
    
    @FindBy(how = How.XPATH, using = "//a[@name='tj_settingicon']")
    private WebElement settingIcon2;

    @FindBy(how = How.XPATH, using = "//span[text() = '广告']")
    private List<WebElement> advList;

    @FindBy(how = How.CSS, using = "a.setpref span.set")
    private WebElement searchSetMenu;

    @FindBy(how = How.CSS, using = "a.prefpanelgo")
    private WebElement saveSettingBtn;

    @FindBy(how = How.XPATH, using = "//li[@data-tabid='advanced']")
    private WebElement advancedSearchTab;

    @FindBy(how = How.XPATH, using = "//span[@id='adv-setting-gpc']//div[contains(@class,'adv-gpc-select')]")
    private WebElement timeSelectBox;

    @FindBy(how = How.XPATH, using = "//div[@class='c-select-dropdown-list']//p[2]")
    private WebElement lastDaySelectItem;

    @FindBy(how = How.XPATH, using = "//span[@id='adv-setting-gpc']//span[@class='c-select-selected-value']")
    private WebElement selectedTimeSpan;

    @FindBy(how = How.XPATH, using = "//li[@class='result-setting']/div[1]//input")
    private WebElement containAllKeywordsEl;

    @FindBy(how = How.XPATH, using = "//input[@type='submit'][contains(@class,'advanced-search-btn')]")
    private WebElement advancedSearchSubmitBtn;
    
    // Exclude advertisement and recommend list
    @FindBy(how = How.XPATH, using = "//div[@id='content_left']/div[contains(@class,'result')][@tpl!='recommend_list'][not(descendant::span[text()='广告'])]")
    private List<WebElement> searchedOutList;
    
    @FindBy(how = How.CSS, using = "h3.c-title>a")
    private WebElement titleLink;
    
    @FindBy(how = How.CSS, using = ".c-color-gray2")
    private WebElement dateUpdateInSearchOutList;

    @FindBy(how = How.XPATH, using = "//meta[@itemprop='dateUpdate']")
    private WebElement dateUpdateInNewOpenPage;

    public SearchPage(WebDriver driver) {
        this.driver = driver;
        PageFactory.initElements(driver, this);
    }
    
    public void clearSearchInput(String searchKeyword) {
    	searchInput.clear();
    }
    
    public void sendKeysToSearchInput(String searchKeyword) {
    	searchInput.sendKeys(searchKeyword);
    }
    
    public void clearSearchInput() {
    	searchInput.clear();
    }
    
    public void clickSearchBtn() {
        searchBtn.click();
    }
    
    public void clickNextPageBtn() {
        nextPageBtn.click();
    }
    
    public void clickPreviousPageBtn() {
        previousPageBtn.click();
    }
    
    public void clickSecondPageBtn() {
        secondPageBtn.click();
    }

    public void clickSettingIcon1() {
        settingIcon1.click();
    }
    
    public void clickSettingIcon2() {
    	settingIcon2.click();
    }
    
    public int getAdvListSize() {
    	return advList.size();
    }
    public void clickSearchSetMenu() {
    	JavascriptExecutor js = (JavascriptExecutor) driver;
    	js.executeScript("arguments[0].click();", searchSetMenu);
    }
    
    public void clickSaveSettingBtn() {
    	saveSettingBtn.click();
    }
    
    public void clickAdvancedSearchTab() {
    	advancedSearchTab.click();
    }
    
    public void clickTimeSelectBox() {
    	timeSelectBox.click();
    }
    
    public void clickLastDaySelectItem() {
    	lastDaySelectItem.click();
    }
    
    public String getSelectedTimeSpan() {
    	return selectedTimeSpan.getText();
    }
    
    public void sendKeysToContainAllKeywordsEl(String searchKeyword) {
    	containAllKeywordsEl.sendKeys(searchKeyword);
    }
    
    public void clickAdvancedSearchSubmitBtn() {
    	JavascriptExecutor js = (JavascriptExecutor) driver;
    	js.executeScript("arguments[0].click();", advancedSearchSubmitBtn);
    }
    
 // Exclude advertisement and recommend list
    public List<WebElement> getSearchedOutList() {
    	return searchedOutList;
    }
    
 // Exclude advertisement and recommend list
    public int getSearchedOutListSize() {
    	return searchedOutList.size();
    }
    
    public String getTitleText() {
    	return titleLink.getText();
    }
    
    public void clickTitleLink() {
    	JavascriptExecutor js = (JavascriptExecutor) driver;
    	js.executeScript("arguments[0].click();", titleLink);
    }
    
    public String getDateUpdateInSearchOutList() {
    	return dateUpdateInSearchOutList.getText();
    }
    
    public String getDateUpdateInNewOpenPage() {
    	return dateUpdateInNewOpenPage.getAttribute("content");
    }

}