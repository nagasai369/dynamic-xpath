package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import com.example.demo.dto.Response;
import com.example.demo.dto.Xpaths;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;

@Service
public class XpathService {

	private WebDriver driver;
	private Document doc;
	private String pageSource;
	private String currentUrl;
	private Xpaths clickMoreXpath;
	private WebElement previousEle;
	private String copyNumber;

	public XpathService() {
	}

	public void execute(Response response) {

		// setting Driver
		String driverPath = "C:\\Users\\NagasaiKoneti\\Downloads\\chromedriver_win32\\chromedriver.exe";
		System.setProperty("webdriver.chrome.driver", driverPath);
		this.driver = new ChromeDriver();
		String Url = response.getUrl();
		driver.get(Url);
		this.pageSource = driver.getPageSource();

		// jsoup document
		this.doc = Jsoup.parse(pageSource);
		JavascriptExecutor js = (JavascriptExecutor) driver;

		// Response Actions
		List<Xpaths> xpaths = response.getXpaths();
		xpaths.forEach(xpath -> {

			if (xpath.getAction().equals("navigator")) {
				try {
					clickOnNavbar(xpath);
					// Navigate(xpath);
					Thread.sleep(5000);
					CompletableFuture<Object> updateDom = this.updateDOM(js);
					Object update = updateDom.get();
				} catch (Exception e) {

				}
			} else if (xpath.getAction().equals("keyTab")) {
				previousEle.sendKeys(Keys.TAB);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (xpath.getAction().equals("keyEnter")) {
				previousEle.sendKeys(Keys.ENTER);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (xpath.getAction().equals("pageLoad")) {
				// previousEle.sendKeys(Keys.TAB);
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (xpath.getAction().equals("waitTillLoad")) {
				// previousEle.sendKeys(Keys.TAB);
				int i = Integer.parseInt(xpath.getInputValue());
				try {
					Thread.sleep(i * 1000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (xpath.getAction().equals("Dropdown Values")) {
				try {
					List<String> dropDown = dropDownXpath(xpath, doc);
					String cssSelector = dropDown.get(0);

					if (cssSelector == "") {
						dropDown = dropDownXpath(xpath, doc);
						cssSelector = dropDown.get(0);
					}
					WebElement element = driver.findElement(By.cssSelector(cssSelector));
					synchronized (element) {
						while (element == null) {
							element.wait();
						}
						System.out.println("Found element1: " + element.getText());

					}
					try {
						element.click();
						Thread.sleep(3000);
						CompletableFuture<Object> updateDom = this.updateDOM(js);
						Object update = updateDom.get();
					} catch (InterruptedException | ExecutionException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}

					String inputString = dropDown.get(1);
					String copyInputString = inputString;
					String OutPut = inputString.replace("cntnrSpan", "dropdownPopup::popupsearch");
					String clickSearch = clickOnSearch(OutPut);
					// Thread.sleep(2000);
					if (clickSearch != "") {
						WebElement searchEle = driver.findElement(By.cssSelector(clickSearch));
						synchronized (searchEle) {
							while (searchEle == null) {
								searchEle.wait();
							}
							System.out.println("Found element1: " + element.getText());

						}
						try {
							searchEle.click();
							Thread.sleep(2000);
							CompletableFuture<Object> updateDom = this.updateDOM(js);
							Object update = updateDom.get();
						} catch (InterruptedException | ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						String searchByInput = searchByInput(xpath.getInputValue(), OutPut
								.replace("dropdownPopup::popupsearch", "_afrLovInternalQueryId:value00::content"));
						if (searchByInput != "") {
							WebElement searchByInputEle = driver.findElement(By.cssSelector(searchByInput));
							synchronized (searchByInputEle) {
								while (searchByInputEle == null) {
									searchByInputEle.wait();
								}
								System.out.println("Found element1: " + searchByInputEle.getText());

							}
							searchByInputEle.clear();
							searchByInputEle.sendKeys(xpath.getInputValue());

							WebElement SearchButton = driver.findElement(By.xpath("//button[text()='Search']"));
							synchronized (SearchButton) {
								while (SearchButton == null) {
									SearchButton.wait();
								}
								System.out.println("Found element1: " + SearchButton.getText());

							}
							try {
								SearchButton.click();
								Thread.sleep(5000);
								CompletableFuture<Object> updateDom = this.updateDOM(js);
								Object update = updateDom.get();
							} catch (InterruptedException | ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							String clickOnText = clickonDropDownText(
									copyInputString.replace("cntnrSpan", "lovDialogId"), xpath.getInputValue());
							WebElement searchvalueTD = driver.findElement(By.cssSelector(clickOnText));
							synchronized (searchvalueTD) {
								while (searchvalueTD == null) {
									searchvalueTD.wait();
								}
								System.out.println("Found element1: " + SearchButton.getText());

							}
							try {
								searchvalueTD.click();
								Thread.sleep(2000);
								CompletableFuture<Object> updateDom = this.updateDOM(js);
								Object update = updateDom.get();
							} catch (InterruptedException | ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}

							System.out.println(copyInputString);
							String clickOnOk = clickOnOKMethod(
									copyInputString.replace("cntnrSpan", "lovDialogId::ok"));
							WebElement clickOnOKEle = driver.findElement(By.cssSelector(clickOnOk));
							synchronized (clickOnOKEle) {
								while (clickOnOKEle == null) {
									clickOnOKEle.wait();
								}
								System.out.println("Found element1: " + clickOnOKEle.getText());

							}

							try {
								clickOnOKEle.click();
								Thread.sleep(5000);
								CompletableFuture<Object> updateDom = this.updateDOM(js);
								Object update = updateDom.get();
							} catch (InterruptedException | ExecutionException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}

					}
					// Element clickdropDown = doc.select(("*[id=" + OutPut + "]")).first();

				} catch (Exception e) {

				}

			} else if (xpath.getAction().equals("copyNumber")) {
				String[] copyNumberInputParameter = xpath.getInputParameter().split(">");
				if (copyNumberInputParameter[0].equals("Confirmation")) {

					// WebElement webElement =
					// driver.findElement(By.xpath("//td[normalize-space(text())=\"" +
					// copyNumberInputParameter[0] + "\"]/following::label[contains(text(),\"" +
					// copyNumberInputParameter[1] + "\")"));

					// Find the element containing the "Confirmation" text
					Element confirmationElement = doc.selectFirst(":containsOwn(Confirmation)");

					// Find the parent div element of the "Confirmation" text
					Element confirmationParent = confirmationElement.parent();
					while (confirmationParent != null
							&& confirmationParent.select(":contains(Process)").isEmpty()) {
						confirmationParent = confirmationParent.parent();
					}
					// Find the label element containing the "Process" text among its siblings
					Element labelElement = confirmationParent.selectFirst("label:contains(Process)");

					System.out.println("Label Text: " + labelElement);

					Pattern pattern = Pattern.compile("\\d+");

					// Create a matcher with the input text
					Matcher matcher = pattern.matcher(labelElement.text());

					// Find and print all matching numbers
					while (matcher.find()) {
						String number = matcher.group();
						System.out.println(number);
						copyNumber = number;
					}
				}

			} else if (xpath.getAction().equals("windowhandle")) {
				String parentWindowHandle = driver.getWindowHandle();
				Set<String> windowHandles = driver.getWindowHandles();

				for (String windowHandle : windowHandles) {
					if (!windowHandle.equals(parentWindowHandle)) {
						driver.switchTo().window(windowHandle);
						CompletableFuture<Object> updateDo = this.updateDOM(js);
						try {
							Object update = updateDo.get();
						} catch (InterruptedException | ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						break;
					}
				}

			}  else if (xpath.getAction().equals("switchToparent")) {
				String parentWindowHandle = driver.getWindowHandle();

				// Get the window handles of all open windows
				Set<String> allWindowHandles = driver.getWindowHandles();
				List<String> list = new ArrayList<>(allWindowHandles);
				// Switch to the child window
				for (String windowHandle : allWindowHandles) {
					if (list.get(list.size()-2).equals(windowHandle)) {
						driver.switchTo().window(windowHandle);
						break;
					}
				}
				CompletableFuture<Object> updateDo = this.updateDOM(js);
				try {
					Object update = updateDo.get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} 
			else if (xpath.getAction().equals("switchToMainparent")) {
				String parentWindowHandle = driver.getWindowHandle();

				// Get the window handles of all open windows
				Set<String> allWindowHandles = driver.getWindowHandles();
				List<String> list = new ArrayList<>(allWindowHandles);
				// Switch to the child window
				for (String windowHandle : allWindowHandles) {
					if (list.get(0).equals(windowHandle)) {
						driver.switchTo().window(windowHandle);
						break;
					}
				}
				CompletableFuture<Object> updateDo = this.updateDOM(js);
				try {
					Object update = updateDo.get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else {
				try {
					String cssSelector = getXpath(xpath, doc, false);
					if (cssSelector == "") {
						cssSelector = getXpath(xpath, doc, true);
					}
					WebElement element = driver.findElement(By.cssSelector(cssSelector));
					synchronized (element) {
						while (element == null) {
							element.wait();
						}
						System.out.println("Found element1: " + element.getText());

					}
					if (xpath.getAction().equals("sendKeys") || xpath.getAction().equals("textArea")
							|| xpath.getAction().equals("selectvaluesTable") || xpath.getAction().equals("login")
							|| xpath.getAction().equals("tableSendKeys")
							|| xpath.getAction().equals("Select Dropdown Values")) {
						System.out.println(element.getText());
						if (xpath.getAction().equals("Select Dropdown Values")) {
							try {
								System.out.println(element);
								// ((JavascriptExecutor) driver).executeScript("arguments[0].value =
								// arguments[1];", element, xpath.getInputValue());
								// element.click();
								// Select select = new Select(element);
								// select.selectByVisibleText(xpath.getInputValue());

								((JavascriptExecutor) driver).executeScript("arguments[0].selectedIndex = -1;",
										element);
								List<WebElement> options = element.findElements(By.tagName("option"));
								for (WebElement option : options) {
									if (option.getText().equals(xpath.getInputValue())) {
										option.click();
										Thread.sleep(3000);
										break;
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						} else {
							element.clear();
							element.sendKeys(xpath.getInputValue());
						}

					} else if (xpath.getAction().equals("clearText")) {
						System.out.println(element);
						element.clear();
					} else if (xpath.getAction().equals("paste")) {
						System.out.println(element);
						element.sendKeys(copyNumber);
					} else {
						element.click();
						Thread.sleep(5000);
						CompletableFuture<Object> updateDom = this.updateDOM(js);
						Object update = updateDom.get();
					}
					previousEle = element;
				} catch (Exception e) {

				}
			}
		});
		// driver.quit();
	}

	private String clickOnOKMethod(String replace) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		CompletableFuture<Object> updateDo = this.updateDOM(js);
		try {
			Object update = updateDo.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (doc.select(("*[id=" + replace + "]")).size() != 0) {
			Element parentele = doc.select("*[id=" + replace + "]").first();
			return parentele.cssSelector();

		} else {
			return "";
		}
	}

	private String clickonDropDownText(String replace, String input) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		CompletableFuture<Object> updateDo = this.updateDOM(js);
		try {
			Object update = updateDo.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (doc.select(("*[id=" + replace + "]")).size() != 0) {
			Element parentele = doc.select("*[id=" + replace + "]").first();
			Element tdEle = parentele.select("*:matchesOwn(^" + input + "$)").first();
			return tdEle.cssSelector();

		} else {
			return "";
		}
	}

	private String searchByInput(String inputValue, String replace) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		CompletableFuture<Object> updateDo = this.updateDOM(js);
		try {
			Object update = updateDo.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println(doc.select(("*[id=" + replace + "]")));
		if (doc.select(("*[id=" + replace + "]")).size() != 0) {

			return doc.select(("*[id=" + replace + "]")).first().cssSelector();
		} else {
			return "";
		}
	}

	private String clickOnSearch(String outPut) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		CompletableFuture<Object> updateDo = this.updateDOM(js);
		try {
			Object update = updateDo.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// doc.select(("*[id=" + outPut + "]"));
		System.out.println(doc.select(("*[id=" + outPut + "]")));
		if (doc.select(("*[id=" + outPut + "]")).size() != 0) {

			return doc.select(("*[id=" + outPut + "]")).first().cssSelector();
		} else {
			return "";
		}
	}

	private List<String> dropDownXpath(Xpaths xpath, Document doc2) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		CompletableFuture<Object> updateDo = this.updateDOM(js);
		try {
			Object update = updateDo.get();
		} catch (InterruptedException | ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		Elements headerElements = null;
		// List<String> storingIds = new ArrayList<String>();
		Element headerEle = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$):not(label)").last();
		Element parent = headerEle.parent();
		while (parent != null
				&& parent.select("label:matchesOwn(^" + splitInputParameter[1] + "$)").isEmpty()) {
			parent = parent.parent();
		}
		String forAtrr = parent.select("label:matchesOwn(^" + splitInputParameter[1] + "$)").attr("for")
				.replaceAll("content", "cntnrSpan");
		Element clickdropDown = parent.select(("*[id=" + forAtrr + "]")).first();
		List<String> storingIds = new ArrayList<String>();
		storingIds.add(clickdropDown.cssSelector());
		storingIds.add(forAtrr);

		return storingIds;
	}

	private void clickOnNavbar(Xpaths xpath) throws InterruptedException, ExecutionException {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		String param1 = "Navigator";
		this.currentUrl = driver.getCurrentUrl();
		WebElement webEle = driver.findElement(By.xpath("//a[@title='" + param1 + "']"));
		synchronized (webEle) {
			while (webEle == null) {
				webEle.wait();
			}
			System.out.println("Found element1: " + webEle.getText());

		}
		webEle.click();

		try {
			Thread.sleep(5000);
			CompletableFuture<Object> updateDom = this.updateDOM(js);
			Object update = updateDom.get();
			WebElement showMore = driver.findElement(By.xpath("//*[text()='Show More']"));
			synchronized (showMore) {
				while (showMore == null) {
					showMore.wait();
				}
				System.out.println("Found element2: " + showMore.getText());

			}
			showMore.click();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		try {
			Thread.sleep(5000);
			CompletableFuture<Object> updateDom = this.updateDOM(js);
			Object update = updateDom.get();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		String welcomeText = splitInputParameter[splitInputParameter.length - 1].trim();
		Element xpathNav = doc.select("*:contains(" + welcomeText + ")").last();
		synchronized (xpathNav) {
			while (xpathNav == null) {
				xpathNav.wait();
			}
			System.out.println("Found element: " + xpathNav.text());

		}

		WebElement element = driver.findElement(By.cssSelector(xpathNav.cssSelector()));
		synchronized (element) {
			while (element == null) {
				element.wait();
			}
			System.out.println("Found element1: " + element.getText());

		}
		element.click();

	}

	@Async
	private CompletableFuture<Object> updateDOM(JavascriptExecutor js) {

		try {
			Object obj = js.executeScript("return document.readyState");
			if (!obj.toString().equals("complete")) {
				return updateDOM(js);
			} else {
				CompletableFuture<String> pageSourceFuture = this.getPageSource(driver);
				String pageSource = pageSourceFuture.get();
				CompletableFuture<Document> parsedHtmlFuture = this.parseHtml(pageSource);
				this.doc = parsedHtmlFuture.get();
				// System.out.println(obj.toString()+this.doc.html()+"hello");
				return CompletableFuture.completedFuture(obj);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return CompletableFuture.completedFuture(js);
	}

	private String getXpath(Xpaths xpath, Document doc, boolean CheckAll) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		CompletableFuture<Object> updateDom = this.updateDOM(js);
		try {
			Object update = updateDom.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// checkingForPageLoaded(js);
		Elements elements;
		if (!CheckAll) {
			switch (xpath.getAction()) {
				case "clickLink":
					elements = doc.select("a");
					break;
				case "clickButton":
					elements = doc.select("button, input[type=button],input[type=submit],a[role=button]");
					break;
				case "Click Expand or Collapse":
					elements = doc.select("a, button, input[type=button],input[type=submit]");
					break;
				case "Select Dropdown Values":
					elements = doc.select("select");
					break;
				case "clickImage":
					elements = doc.select("img, svg");
					break;
				case "sendKeys":
					elements = doc.select("input");
					break;
				case "clearText":
					elements = doc.select("input");
					break;
				case "paste":
					elements = doc.select("input");
					break;
				case "login":
					elements = doc.select("input");
					break;
				case "textArea":
					elements = doc.select("textarea");
					break;
				default:
					elements = doc.select("*");
					break;
			}

		} else {
			elements = doc.select("*");
		}
		boolean flag = false;
		// Elements elements = doc.select("a");
		for (Element element : elements) {

			if (xpath.getAction().equals("login")) {
				String[] splitInputParameter = xpath.getInputParameter().split(">");
				Elements headerElements = null;
				for (int i = 0; i < splitInputParameter.length; i++) {

					String welcomeText = splitInputParameter[i].trim();
					if (headerElements == null) {
						headerElements = doc.select("*:contains(" + welcomeText + ")");
					} else if (headerElements != null && i < splitInputParameter.length) {
						headerElements = headerElements.nextAll().select("*:contains(" + welcomeText + ")");
					}
					if (i == splitInputParameter.length - 1) {
						headerElements = headerElements.nextAll().select("input");
						if (headerElements.first() == element) {
							flag = true;
							return element.cssSelector();
						}

					}
				}

			} else if ((xpath.getAction().equals("clickButton") || xpath.getAction().equals("clickLink"))) {
				String[] splitInputParameter = xpath.getInputParameter().split(">");

				if (splitInputParameter.length == 1) {
					if ((element.text().trim().equalsIgnoreCase(xpath.getInputParameter())
							|| element.attr("alt").trim().equals(xpath.getInputParameter()))) {
						flag = true;
						return element.cssSelector();
					}
				} else {
					// Elements headerElements = null;
					// List<String> storingIds = new ArrayList<String>();
					Element headerEle = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$):not(label,button,a)")
							.last();
					Element parent = headerEle.parent();
					while (parent != null
							&& parent.select("*:matchesOwn(^" + splitInputParameter[1] + "$)").isEmpty()) {
						parent = parent.parent();
					}
					Element clickbutton = parent.select("*:matchesOwn(^" + splitInputParameter[1] + "$)").first();
					return clickbutton.cssSelector();

				}

			} else if (xpath.getAction().equals("clickImage")) {
				if (element.tagName() == "svg") {
					if (element.select("title").text().equals(xpath.getInputParameter())) {
						return element.cssSelector();
					}

				} else {
					if (element.attr("title").equals(xpath.getInputParameter())) {
						flag = true;
						return element.cssSelector();
					}
				}
			} else if (xpath.getAction().equals("tableSendKeys")) {
				CompletableFuture<Object> updateDo = this.updateDOM(js);
				try {
					Object update = updateDo.get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String[] splitInputParameter = xpath.getInputParameter().split(">");
				Elements headerElements = null;
				// List<String> storingIds = new ArrayList<String>();
				Element headerEle = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$):not(label)").last();
				Element parent = headerEle.parent();
				while (parent != null
						&& parent.select("th:matchesOwn(^" + splitInputParameter[1] + "$)").isEmpty()) {
					parent = parent.parent();
				}
				String strele = "_afrFilter_FOpt1_afr__FOr1_afr_0_afr__FONSr2_afr_0_afr_MAnt2_afr_1_afr_pm1_afr_r1_afr_0_afr_ap1_afr_r12_afr_1_afr_at1_afr__ATp_afr_ta1_afr_c4::content";
				Element ele = doc.select("input[id=" + strele + "]").first();
				return ele.cssSelector();

			} else if (xpath.getAction().equals("textArea") || xpath.getAction().equals("selectvaluesTable")
					|| xpath.getAction().equals("sendKeys") || xpath.getAction().equals("Select Dropdown Values") ||
					xpath.getAction().equals("clearText") || xpath.getAction().equals("paste")) {
				CompletableFuture<Object> updateDo = this.updateDOM(js);
				try {
					Object update = updateDo.get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String[] splitInputParameter = xpath.getInputParameter().split(">");
				Elements headerElements = null;
				// List<String> storingIds = new ArrayList<String>();
				Element headerEle = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$):not(label)").last();
				Element parent = headerEle.parent();
				while (parent != null
						&& parent.select("label:matchesOwn(^" + splitInputParameter[1] + "$)").isEmpty()) {
					parent = parent.parent();
				}
				String forAtrr = parent.select("label:matchesOwn(^" + splitInputParameter[1] + "$)").attr("for");
				if (xpath.getAction().equals("selectvaluesTable") || xpath.getAction().equals("Dropdown Values")
						|| xpath.getAction().equals("sendKeys") || xpath.getAction().equals("paste")) {
					Element textArea = parent.select(("input[id=" + forAtrr + "]")).first();
					if (xpath.getAction().equals("paste")) {
						textArea = parent
								.select(("input[id="
										+ "_FOpt1:_FOr1:0:_FONSr2:0:_FOTsr1:0:pt1:srRssdfl:value10::content" + "]"))
								.first();
					}
					return textArea.cssSelector();
				} else if (xpath.getAction().equals("Select Dropdown Values")) {
					Element textArea = parent.select(("select[id=" + forAtrr + "]")).first();
					return textArea.cssSelector();
				} else {

					Element textArea = parent.select(("textarea[id=" + forAtrr + "]")).first();
					return textArea.cssSelector();
				}

			} else if (xpath.getAction().equals("selectAvalue")) {
				Element textArea = doc.select(":matchesOwn(^" + xpath.getInputValue() + "$)").last();
				return textArea.cssSelector();

			} else if (xpath.getAction().equals("Click Expand or Collapse")) {
				CompletableFuture<Object> updateDo = this.updateDOM(js);
				try {
					Object update = updateDo.get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				String[] splitInputParameter = xpath.getInputParameter().split(">");
				Elements headerElements = null;
				// List<String> storingIds = new ArrayList<String>();
				Element headerEle = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$):not(label)").last();
				Element parent = headerEle.parent();
				while (parent != null
						&& parent.select(":matchesOwn(^" + splitInputParameter[1] + "$)").isEmpty()) {
					parent = parent.parent();
				}
				Element ele = parent.select(":matchesOwn(^" + splitInputParameter[1] + "$)").first().parent().parent()
						.parent().select("a").first();
				System.out.println(ele.cssSelector());
				if (ele == element) {
					return ele.cssSelector();
				}

			}
		}

		return "";
	}

	@Async
	public CompletableFuture<String> getPageSource(WebDriver driver) {
		String pageSource = driver.getPageSource();
		return CompletableFuture.completedFuture(pageSource);
	}

	@Async
	public CompletableFuture<Document> parseHtml(String htmlString) throws IOException {
		Document parsedHtml = Jsoup.parse(htmlString);
		return CompletableFuture.completedFuture(parsedHtml);
	}

	// ___ Methods for every Actions

	// Method for Naviagte
	private void Navigate(Xpaths xpath) {
		String param1 = "Navigator";
		WebElement clickOnNaviagtorIcon = driver.findElement(By.xpath("//a[@title='" + param1 + "']"));
		synchronized (clickOnNaviagtorIcon) {
			while (clickOnNaviagtorIcon == null) {
				try {
					clickOnNaviagtorIcon.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Found element1: " + clickOnNaviagtorIcon.getText());

		}
		clickOnNaviagtorIcon.click();
		try {
			Thread.sleep(5000);
			CompletableFuture<Object> updateDom = this.updateDOM((JavascriptExecutor) driver);
			try {
				Object update = updateDom.get();
			} catch (ExecutionException e) {
				e.printStackTrace();
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		WebElement showMoreElement = driver.findElement(By.xpath("//*[text()='Show More']"));
		synchronized (showMoreElement) {
			while (showMoreElement == null) {
				try {
					showMoreElement.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			System.out.println("Found element2: " + showMoreElement.getText());

		}
		showMoreElement.click();
		String[] NavigateInputParameter = xpath.getInputParameter().split(">");
		for (int i = NavigateInputParameter.length - 1; i >= 0; i--) {
			Elements inputParameterEle = this.doc.select(":matchesOwn(^" + NavigateInputParameter[i] + "$)");
			String parentEle = NavigateInputParameter[i - 1];
			inputParameterEle.forEach(ele -> {
				Element parent = ele.parent();
				while (parent != null && parent.select(":matchesOwn(^" + parentEle + "$)").isEmpty()) {
					parent = parent.parent();
				}
				if (parent != null) {

				}
			});
		}

	}

	// Method for DropdownValues
	private void DropdownValues(Xpaths xpath) {

	}

	// Method for logout
	private void Logout(Xpaths xpath) {

	}

	// Method for login
	private void Login(Xpaths xpath) {

	}

	// Method for sendKeys
	private void SendKeys(Xpaths xpath) {

	}

	// Method for Table Dropdownvalues
	private void TableDropdownValues(Xpaths xpath) {

	}

	// Method for Table sendKeys
	private void TableSendKeys(Xpaths xpath) {

	}

	// Method for vertical Scroll
	private void VerticalScroll(Xpaths xpath) {

	}

	// Method for Clear
	private void Clear(Xpaths xpath) {

	}

	// Method for click Link
	private void ClickLink(Xpaths xpath) {

	}

	// Method for click button
	private void ClickButton(Xpaths xpath) {

	}

	// Method for clickCheckBox
	private void clickCheckBox(Xpaths xpath) {

	}

	// Method for SwitchToParentWindow
	private void SwitchToParentWindow(Xpaths xpath) {

	}

	// Method for SwitchToDefaultFrame
	private void SwitchToDefaultFrame(Xpaths xpath) {

	}

	// Method for SwitchToFrame
	private void SwitchToFrame(Xpaths xpath) {

	}

	// Method for textArea
	private void TextArea(Xpaths xpath) {

	}

	// Method for Windowhandle
	private void Windowhandle(Xpaths xpath) {

	}

	// Method for clickExpandorcollapse

	private void clickExpandorcollapse(Xpaths xpath) {

	}

	private Elements GettingAllElements(String actionElements) {
		Elements elements;
		switch (actionElements) {
			case "clickLink":
				elements = doc.select("a");
				break;
			case "clickButton":
				elements = doc.select("button, input[type=button],input[type=submit],a[role=button]");
				break;
			case "Click Expand or Collapse":
				elements = doc.select("a, button, input[type=button],input[type=submit]");
				break;
			case "Dropdown Values":
				elements = doc.select("select");
				break;
			case "clickImage":
				elements = doc.select("img, svg");
				break;
			case "sendKeys":
				elements = doc.select("input");
				break;
			case "login":
				elements = doc.select("input");
				break;
			case "textArea":
				elements = doc.select("textarea");
				break;
			case "getAllElements":
				elements = doc.select("*");
				break;
			default:
				elements = doc.select("*");
				break;
		}
		return elements;
	}
}
