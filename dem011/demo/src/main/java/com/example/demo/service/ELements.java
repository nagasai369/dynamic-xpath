package com.example.demo.service;

import org.jsoup.nodes.Element;
import org.openqa.selenium.WebElement;

public class ELements {
    private WebElement webElement;
    private Element element;

    public WebElement getWebElement() {
        return webElement;
    }
    public Element getElement() {
        return element;
    }
    public void setWebElement(WebElement webElement) {
        this.webElement = webElement;
    }
    public void setElement(Element element) {
        this.element = element;
    }

    
    

}
