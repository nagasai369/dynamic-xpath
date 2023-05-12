package com.example.demo.dto;

public class returnXpath {
    private String action;
	private String inputParameter;
    private String xpathCss;
    public returnXpath(String action, String inputParameter, String xpathCss) {
        this.action=action;
        this.inputParameter=inputParameter;
        this.xpathCss=xpathCss;
    }
    public String getAction() {
        return action;
    }
    public void setAction(String action) {
        this.action = action;
    }
    public String getInputParameter() {
        return inputParameter;
    }
    public void setInputParameter(String inputParameter) {
        this.inputParameter = inputParameter;
    }
    public String getXpathCss() {
        return xpathCss;
    }
    public void setXpathCss(String xpathCss) {
        this.xpathCss = xpathCss;
    }
}
