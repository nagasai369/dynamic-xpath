package com.example.demo.service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
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
				CompletableFuture<Object> updateDo = this.updateDOM(js);
				try {
					Object update = updateDo.get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (xpath.getAction().equals("Dropdown Values")) {
				dropDownValues(xpath);
			} else if (xpath.getAction().equals("copyNumber")) {
				String[] copyNumberInputParameter = xpath.getInputParameter().split(">");
				if (copyNumberInputParameter[0].equals("Confirmation")) {
					Element confirmationElement = doc
							.selectFirst(":containsOwn('" + copyNumberInputParameter[0] + "')");

					// Find the parent div element of the "Confirmation" text
					Element confirmationParent = confirmationElement.parent();
					while (confirmationParent != null
							&& confirmationParent.select(":contains('" + copyNumberInputParameter[1] + "')")
									.isEmpty()) {
						confirmationParent = confirmationParent.parent();
					}
					// Find the label element containing the "Process" text among its siblings
					Element labelElement = confirmationParent
							.selectFirst("label:contains('" + copyNumberInputParameter[1] + "')");
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

			} else if (xpath.getAction().equals("switchToparent")) {
				String parentWindowHandle = driver.getWindowHandle();

				// Get the window handles of all open windows
				Set<String> allWindowHandles = driver.getWindowHandles();
				List<String> list = new ArrayList<>(allWindowHandles);
				// Switch to the child window
				for (String windowHandle : allWindowHandles) {
					if (list.get(list.size() - 2).equals(windowHandle)) {
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

			} else if (xpath.getAction().equals("switchToMainparent")) {
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

			} else if (xpath.getAction().equals("switchToFrame")) {
				String cssSelector = getXpath(xpath, doc, false);
				WebElement frameElement = driver.findElement(By.cssSelector(cssSelector));
				synchronized (frameElement) {
					while (frameElement == null) {
						try {
							frameElement.wait();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					System.out.println("Found element1: " + frameElement.getText());

				}

				driver.switchTo().frame(frameElement);
				CompletableFuture<Object> updateDo = this.updateDOM(js);
				try {
					Object update = updateDo.get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			} else if (xpath.getAction().equals("switchToDefaultFrame")) {
				driver.switchTo().defaultContent();
				CompletableFuture<Object> updateDo = this.updateDOM(js);
				try {
					Object update = updateDo.get();
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			} else if (xpath.getAction().equals("Click Expand or Collapse")) {
				clickExpandorcollapse(xpath);
			} else if (xpath.getAction().equals("login")) {
				login(xpath);
			} else if (xpath.getAction().equals("sendKeys")) {
				sendKeys(xpath);
			} else if (xpath.getAction().equals("textArea")) {
				textArea(xpath);
			} else if (xpath.getAction().equals("clickButton")) {
				clickButton(xpath);
			} else if (xpath.getAction().equals("clickLink")) {
				clickLink(xpath);
			} else if (xpath.getAction().equals("clickImage")) {
				clickImage(xpath);
			} else if (xpath.getAction().equals("paste")) {
				paste(xpath);
			} else if (xpath.getAction().equals("clearText")) {
				clearText(xpath);
			} else if (xpath.getAction().equals("Select Dropdown Values")) {
				selectDropdownValues(xpath);
			} else if (xpath.getAction().equals("tableRowSelect")) {
				tableRowSelect(xpath);
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
						Thread.sleep(5000);
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
		
			if(xpath.getAction().equals("sendKeys") || xpath.getAction().equals("paste") || xpath.getAction().equals("Select Dropdown Values")){


			}
			else{
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
						CompletableFuture<Object> updateDom = this.updateDOM(js);
						try {
							updateDom.get();
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						} catch (ExecutionException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
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
				case "switchToFrame":
					elements = doc.select("iframe");
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
				case "tableRowSelect":
					elements = doc.select("table");
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

			if ((xpath.getAction().equals("clickButton") || xpath.getAction().equals("clickLink"))) {
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

				String[] splitInputParameter = xpath.getInputParameter().split(">");
				if (splitInputParameter.length == 1) {
					if (element.tagName() == "svg") {
						if (element.select("title").text().equals(xpath.getInputParameter()) || element.select("title")
								.text().replaceAll(String.valueOf((char) 160), " ").equals(xpath.getInputParameter()) ||
								element.parent().attr("title").equals(xpath.getInputParameter())
								|| element.parent().attr("title").replaceAll(String.valueOf((char) 160), " ")
										.equals(xpath.getInputParameter())) {
							return element.cssSelector();
						}

					} else {
						if (element.attr("title").equals(xpath.getInputParameter()) || element.attr("title")
								.replaceAll(String.valueOf((char) 160), " ").equals(xpath.getInputParameter())
								|| element.parent().attr("title").equals(xpath.getInputParameter())
								|| element.parent().attr("title").replaceAll(String.valueOf((char) 160), " ")
										.equals(xpath.getInputParameter())) {
							flag = true;
							return element.cssSelector();
						}
					}
				} else {

					Element headerEle = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$)")
							.last();
					Element parent = headerEle.parent();
					while (parent != null
							&& parent.select(String.format("img[title=%s]", splitInputParameter[1])).isEmpty()) {
						parent = parent.parent();
					}
					Element clickbutton = parent.select(String.format("img[title=%s]", splitInputParameter[1])).first();
					return clickbutton.cssSelector();

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

			} else if (xpath.getAction().equals("tableRowSelect")) {

				if (element.attr("summary").equals(xpath.getInputParameter())) {
					return element.cssSelector();
				}
			} else if (xpath.getAction().equals("switchToFrame")) {
				if (element.attr("id").equals(xpath.getInputParameter())) {
					return element.cssSelector();

				} else if (element.attr("title").equals(xpath.getInputParameter())) {

				}
			}
		}

		return "";
	}

	private WebElement findTheElement(Element ele, String[] copyOfRange) {
		Element parent = ele.parent();
		while (parent != null && parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
			parent = parent.parent();
		}
		if (parent != null) {
			Elements secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
			for (Element element : secondElements) {
				if (copyOfRange.length > 1) {
					findTheElement(element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
				} else {
					WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					Selelement = Selelement.findElement(By.xpath("./following::input"));
					if (!Selelement.isEnabled()) {
						continue;
					} else {
						return Selelement;
					}
				}
			}
		} else {
			return null;
		}

		return null;
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

	// Method for logout
	private void Logout(Xpaths xpath) {

	}

	// Method for login
	private void login(Xpaths xpath) {
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		Elements headerElements = null;
		headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
		for (Element ele : headerElements) {
			if (splitInputParameter.length == 1) {
				WebElement selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
				if (!selElement.isEnabled()) {
					continue;
				} else {
					selElement =selElement.findElement(By.xpath("./following::input"));
					selElement.clear();
					JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
					String script = "arguments[0].value = arguments[1]";
					jsExecutor.executeScript(script, selElement, xpath.getInputValue());
					break;
				}
			}
			WebElement selElement = findTheElement(ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
			if (selElement == null) {
				continue;
			} else {
				selElement.clear();
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				String script = "arguments[0].value = arguments[1]";
				jsExecutor.executeScript(script, selElement, xpath.getInputValue());
				break;
			}
		}

	}

	// Method for Table Dropdownvalues
	private void TableDropdownValues(Xpaths xpath) {

	}

	// Method for paste
	private void paste(Xpaths xpath) {
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		Elements headerElements = null;
		headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
		for (Element ele : headerElements) {
			if (splitInputParameter.length == 1) {
				WebElement selElement = driver.findElement(By.xpath(ele.cssSelector()));
				if (!selElement.isEnabled()) {
					continue;
				} else {
					selElement.clear();
					JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
					String script = "arguments[0].value = arguments[1]";
					jsExecutor.executeScript(script, selElement, xpath.getInputValue());
					break;
				}
			}
			WebElement selElement = findTheElement(ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
			if (selElement == null) {
				continue;
			} else {
				selElement.clear();
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				String script = "arguments[0].value = arguments[1]";
				jsExecutor.executeScript(script, selElement, xpath.getInputValue());
				break;
			}
		}

	}

	// Method for Table sendKeys
	private void tableSendKeys(Xpaths xpath) {

	}

	// Method for selectDropdownValues
	private void selectDropdownValues(Xpaths xpath) {
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		Elements headerElements = null;
		headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
		for (Element ele : headerElements) {
			if (splitInputParameter.length == 1) {
				WebElement selElement = driver.findElement(By.xpath(ele.cssSelector()));
				if (!selElement.isEnabled()) {
					continue;
				} else {
					((JavascriptExecutor) driver).executeScript("arguments[0].selectedIndex = -1;",
							selElement);
					List<WebElement> options = selElement.findElements(By.tagName("option"));
					for (WebElement option : options) {
						if (option.getText().equals(xpath.getInputValue())) {
							option.click();
							break;
						}
					}
					break;
				}
			}
			WebElement selElement = findselectDropdownElement(ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
			if (selElement == null) {
				continue;
			} else {
				((JavascriptExecutor) driver).executeScript("arguments[0].selectedIndex = -1;",
						selElement);
				List<WebElement> options = selElement.findElements(By.tagName("option"));
				for (WebElement option : options) {
					if (option.getText().equals(xpath.getInputValue())) {
						option.click();
						break;
					}
				}
				break;
			}
		}

	}

	private WebElement findselectDropdownElement(Element ele, String[] copyOfRange) {
		Element parent = ele.parent();
		while (parent != null && parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
			parent = parent.parent();
		}
		if (parent != null) {
			Elements secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
			for (Element element : secondElements) {
				if (copyOfRange.length > 1) {
					findselectDropdownElement(element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
				} else {
					WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					Selelement = Selelement.findElement(By.xpath("./following::select"));
					if (!Selelement.isEnabled()) {
						continue;
					} else {
						return Selelement;
					}
				}
			}
		} else {
			return null;
		}

		return null;
	}

	// Method for sendKeys
	private void sendKeys(Xpaths xpath) {
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		Elements headerElements = null;
		headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
		for (Element ele : headerElements) {
			if (splitInputParameter.length == 1) {
				WebElement selElement = driver.findElement(By.xpath(ele.cssSelector()));
				if (!selElement.isEnabled()) {
					continue;
				} else {
					selElement =selElement.findElement(By.xpath("./following::select"));
					selElement.clear();
					JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
					String script = "arguments[0].value = arguments[1]";
					jsExecutor.executeScript(script, selElement, xpath.getInputValue());
					break;
				}
			}
			WebElement selElement = findTheElement(ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
			if (selElement == null) {
				continue;
			} else {
				selElement.clear();
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				String script = "arguments[0].value = arguments[1]";
				jsExecutor.executeScript(script, selElement, xpath.getInputValue());
				break;
			}
		}

	}

	// Method for vertical Scroll
	private void VerticalScroll(Xpaths xpath) {

	}

	// Method for Clear
	private void clearText(Xpaths xpath) {
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		Elements headerElements = null;
		headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
		for (Element ele : headerElements) {
			if (splitInputParameter.length == 1) {
				WebElement selElement = driver.findElement(By.xpath(ele.cssSelector()));
				if (!selElement.isEnabled()) {
					continue;
				} else {
					selElement.clear();

				}
			}
			WebElement selElement = findTheElement(ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
			if (selElement == null) {
				continue;
			} else {
				selElement.clear();
			}
		}

	}

	// Method for click Link
	private void dropDownValues(Xpaths xpath) {
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		Elements headerElements = null;
		headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
		for (Element ele : headerElements) {
			if (splitInputParameter.length == 1) {
				WebElement selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
				if (!selElement.isEnabled() && !selElement.isDisplayed()
				// || !selElement.isClickable()
				) {
					continue;
				} else {
					selElement.clear();
					JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
					String script = "arguments[0].value = arguments[1]";
					jsExecutor.executeScript(script, selElement, xpath.getInputValue());
					break;
				}
			} else {
				WebElement selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
				if (!selElement.isEnabled() || !selElement.isDisplayed()

				// ___uncomment it
				// || !selElement.isClickable()

				// --comment this
						|| selElement.getTagName().equals("span")) {
					continue;
				}
				selElement = findDropDownEle(ele,
						Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), xpath.getInputValue());
				if (selElement == null) {
					continue;
				} else {
					selElement.click();
					break;
				}
			}
		}
	}

	private WebElement findDropDownEle(Element ele, String[] copyOfRange, String inputValue) {
		Element parent = ele.parent();
		while (parent != null && parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
			parent = parent.parent();
		}
		if (parent != null) {
			Elements secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");

			for (Element element : secondElements) {
				if (copyOfRange.length > 1) {
					WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					if (!Selelement.isEnabled() || !Selelement.isDisplayed()
					// || !Selelement.isClickable()
					) {
						continue;
					}
					findTheElement(element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
				} else {
					WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					if (!Selelement.isEnabled() || !Selelement.isDisplayed()
					// || !Selelement.isClickable()
							|| element.tagName().equals("td")) {
						continue;
					}
					Selelement = Selelement.findElement(By.xpath("./following::input"));
					Selelement = Selelement.findElement(By.xpath("following-sibling::*[1]"));
					if (!Selelement.isEnabled() || !Selelement.isDisplayed()
					// || !Selelement.isClickable()
					) {
						continue;
					} else {
						Selelement.click();
						try {
							Thread.sleep(2000);
						} catch (InterruptedException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						WebElement SearchSelelement = driver.findElement(By.xpath("//*[text()='Search...']"));
						SearchSelelement.click();
						String searchByInput = element.attr("for")
								.replace("content", "_afrLovInternalQueryId:value00::content");

						WebElement searchByValue = driver.findElement(By.id(searchByInput));
						searchByValue.clear();
						JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
						String script = "arguments[0].value = arguments[1]";
						jsExecutor.executeScript(script, searchByValue, inputValue);
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
						Element searchByValueJsoup = doc.select(("*[id=" + searchByInput + "]")).first();
						parent = searchByValueJsoup.parent();
						while (parent != null && parent.select(":matchesOwn(^" + "Search" + "$)").isEmpty()) {
							parent = parent.parent();
						}
						if (parent != null) {
							Elements searchButtonElement = parent.select(":matchesOwn(^" + "Search" + "$)");
							for (Element searchButtonele : searchButtonElement) {
								WebElement searchButtoneleSelinium = driver
										.findElement(By.cssSelector(searchButtonele.cssSelector()));
								if (!searchButtoneleSelinium.isEnabled() || !searchButtoneleSelinium.isDisplayed()
								// || !searchButtoneleSelinium.isClickable()
								) {
									continue;
								} else {
									searchButtoneleSelinium.click();
									driver.findElement(By.xpath("//td/following::span[text()='" + inputValue + "']"))
											.click();
									String okButtonEleId = searchButtoneleSelinium.getAttribute("id");
									return driver.findElement(By.id(okButtonEleId));
								}
							}
						}

					}
				}
			}
		} else {
			return null;
		}

		return null;
	}

	// method for click Link
	private void clickLink(Xpaths xpath) {

		Elements elements = GettingAllElements(xpath.getAction());
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		String cssSelectorEle = "";
		if (splitInputParameter.length == 1) {
			for (Element element : elements) {
				if ((element.text().trim().equalsIgnoreCase(xpath.getInputParameter())
						|| element.attr("alt").trim().equals(xpath.getInputParameter()))) {
					cssSelectorEle = element.cssSelector();

				}
			}
		} else {
			// Element headerEle = doc.select(":matchesOwn(^" + splitInputParameter[0] +
			// "$):not(label,button,a)")
			// .last();
			// Element parent = headerEle.parent();
			// while (parent != null
			// && parent.select("*:matchesOwn(^" + splitInputParameter[1] + "$)").isEmpty())
			// {
			// parent = parent.parent();
			// }
			// Element clickbutton = parent.select("*:matchesOwn(^" + splitInputParameter[1]
			// + "$)").first();
			// cssSelectorEle = clickbutton.cssSelector();
			Elements headerElements = null;
			headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
			for (Element ele : headerElements) {
				WebElement selElement = findLinkElement(ele,
						Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
				if (selElement == null) {
					continue;
				} else {
					selElement.click();
				}
			}

		}
		if (cssSelectorEle != "") {

			WebElement buttonElement = driver.findElement(By.cssSelector(cssSelectorEle));
			synchronized (buttonElement) {
				while (buttonElement == null) {
					try {
						buttonElement.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			buttonElement.click();
		}
	}

	private WebElement findLinkElement(Element ele, String[] copyOfRange) {
		Element parent = ele.parent();
		while (parent != null && (parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()
				|| parent.select("[alt='" + copyOfRange[0] + "']").isEmpty()
				|| parent.select("[title='" + copyOfRange[0] + "']").isEmpty())) {
			parent = parent.parent();
		}
		if (parent != null) {
			Elements secondElements = null;
			if (parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
				secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
			}
			if (parent.select("[alt='" + copyOfRange[0] + "']").isEmpty()) {
				secondElements = parent.select("[alt='" + copyOfRange[0] + "']");
			}
			if (parent.select("[title='" + copyOfRange[0] + "']").isEmpty()) {
				secondElements = parent.select("[title='" + copyOfRange[0] + "']");
			}
			for (Element element : secondElements) {
				if (copyOfRange.length > 1) {
					findLinkElement(element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
				} else {
					WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
						continue;
					} else {
						return Selelement;
					}
				}
			}
		} else {
			return null;
		}

		return null;
	}

	// method for click Link
	private void clickImage(Xpaths xpath) {

		Elements elements = GettingAllElements(xpath.getAction());
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		String cssSelectorEle = "";
		if (splitInputParameter.length == 1) {
			for (Element element : elements) {
				if (element.tagName() == "svg") {
					if (element.select("title").text().equals(xpath.getInputParameter()) || element.select("title")
							.text().replaceAll(String.valueOf((char) 160), " ").equals(xpath.getInputParameter()) ||
							element.parent().attr("title").equals(xpath.getInputParameter())
							|| element.parent().attr("title").replaceAll(String.valueOf((char) 160), " ")
									.equals(xpath.getInputParameter())) {
						cssSelectorEle = element.cssSelector();
					}

				} else {
					if (element.attr("title").equals(xpath.getInputParameter()) || element.attr("title")
							.replaceAll(String.valueOf((char) 160), " ").equals(xpath.getInputParameter())
							|| element.parent().attr("title").equals(xpath.getInputParameter())
							|| element.parent().attr("title").replaceAll(String.valueOf((char) 160), " ")
									.equals(xpath.getInputParameter())) {

						cssSelectorEle = element.cssSelector();
					}
				}
			}
		} else {
			Elements headerElements = null;
			headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
			for (Element ele : headerElements) {
				WebElement selElement = findImageElement(ele,
						Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
				if (selElement == null) {
					continue;
				} else {
					selElement.click();
				}
			}

		}
		if (cssSelectorEle != "") {

			WebElement buttonElement = driver.findElement(By.cssSelector(cssSelectorEle));
			synchronized (buttonElement) {
				while (buttonElement == null) {
					try {
						buttonElement.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			buttonElement.click();
		}
	}

	private WebElement findImageElement(Element ele, String[] copyOfRange) {
		Element parent = ele.parent();
		while (parent != null && parent
				.select(String.format("img[title='%s'], svg[title='%s']", copyOfRange[0], copyOfRange[0])).isEmpty()) {
			parent = parent.parent();
		}
		if (parent != null) {
			Elements secondElements = parent
					.select(String.format("img[title='%s'], svg[title='%s']", copyOfRange[0], copyOfRange[0]));
			for (Element element : secondElements) {
				if (copyOfRange.length > 1) {
					findImageElement(element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
				} else {
					WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
						continue;
					} else {
						return Selelement;
					}
				}
			}
		} else {
			return null;
		}

		return null;
	}

	// Method for click button
	// Method for click button
	private void tableRowSelect(Xpaths xpath) {
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		String cssSelectorEle = "";
		if (splitInputParameter.length == 1) {
			Elements elements = doc.select(String.format("table[summary='%s']", splitInputParameter[0]));
			for (Element element : elements) {
				WebElement tableElement = driver.findElement(By.cssSelector(element.cssSelector()));
				if (!tableElement.isDisplayed() || !tableElement.isEnabled()) {
					continue;
				} else {
					cssSelectorEle = element.cssSelector();
				}
			}

		} else {
			// Element headerEle = doc.select(":matchesOwn(^" + splitInputParameter[0] +
			// "$):not(label,button,a)")
			// .last();
			// Element parent = headerEle.parent();
			// while (parent != null
			// && parent.select("*:matchesOwn(^" + splitInputParameter[1] + "$)").isEmpty())
			// {
			// parent = parent.parent();
			// }
			// Element clickbutton = parent.select("*:matchesOwn(^" + splitInputParameter[1]
			// + "$)").first();
			// cssSelectorEle = clickbutton.cssSelector();
			Elements headerElements = null;
			headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
			for (Element ele : headerElements) {
				WebElement selElement = findtableRowSelectElement(ele,
						Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
				if (selElement == null) {
					continue;
				} else {
					selElement.click();
				}
			}

		}
		if (cssSelectorEle != "") {

			WebElement buttonElement = driver.findElement(By.cssSelector(cssSelectorEle));
			synchronized (buttonElement) {
				while (buttonElement == null) {
					try {
						buttonElement.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			buttonElement.click();
		}
	}

	private WebElement findtableRowSelectElement(Element ele, String[] copyOfRange) {
		Element parent = ele.parent();
		if (copyOfRange.length == 1) {
			while (parent != null && parent.select(String.format("table[summary='%s']", copyOfRange[0])).isEmpty()) {
				parent = parent.parent();
			}
		} else {
			while (parent != null && (parent.select("*:matchesOwn(^" + copyOfRange[0] + "$)").isEmpty())) {
				parent = parent.parent();
			}
		}
		if (parent != null) {
			Elements secondElements = null;
			if (copyOfRange.length == 1) {
				secondElements = parent.select(String.format("table[summary='%s']", copyOfRange[0]));
			} else {
				secondElements = parent.select("*:matchesOwn(^" + copyOfRange[0] + "$)");
			}
			for (Element element : secondElements) {
				if (copyOfRange.length > 1) {
					findtableRowSelectElement(element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
				} else {
					WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
						continue;
					} else {
						return Selelement;
					}
				}
			}
		} else {
			return null;
		}

		return null;
	}

	private void clickButton(Xpaths xpath) {
		Elements elements = GettingAllElements(xpath.getAction());
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		String cssSelectorEle = "";
		if (splitInputParameter.length == 1) {
			for (Element element : elements) {
				if ((element.text().trim().equalsIgnoreCase(xpath.getInputParameter())
						|| element.attr("alt").trim().equals(xpath.getInputParameter()) || element.attr("value").equals(xpath.getInputParameter()))) {
					cssSelectorEle = element.cssSelector();
					break;

				}
			}
		} else {
				Elements headerElements = null;
			headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
			for (Element ele : headerElements) {
				WebElement selElement = findButtonElement(ele,
						Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
				if (selElement == null) {
					continue;
				} else {
					selElement.click();
				}
			}

		}
		if (cssSelectorEle != "") {

			WebElement buttonElement = driver.findElement(By.cssSelector(cssSelectorEle));
			synchronized (buttonElement) {
				while (buttonElement == null) {
					try {
						buttonElement.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			buttonElement.click();
		}
	}

	private WebElement findButtonElement(Element ele, String[] copyOfRange) {
		Element parent = ele.parent();
		while (parent != null && (parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()
				|| parent.select("[alt='" + copyOfRange[0] + "']").isEmpty()
				|| parent.select("[title='" + copyOfRange[0] + "']").isEmpty())) {
			parent = parent.parent();
		}
		if (parent != null) {
			Elements secondElements = null;
			if (parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
				secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
			}
			if (parent.select("[alt='" + copyOfRange[0] + "']").isEmpty()) {
				secondElements = parent.select("[alt='" + copyOfRange[0] + "']");
			}
			if (parent.select("[title='" + copyOfRange[0] + "']").isEmpty()) {
				secondElements = parent.select("[title='" + copyOfRange[0] + "']");
			}
			for (Element element : secondElements) {
				if (copyOfRange.length > 1) {
					findButtonElement(element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
				} else {
					WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
						continue;
					} else {
						return Selelement;
					}
				}
			}
		} else {
			return null;
		}

		return null;
	}

	// Method for clickCheckBox
	// Method for clickCheckBox
	private void clickCheckBox(Xpaths xpath) {

	}

	// Method for SwitchToParentWindow
	private void SwitchToParentWindow() {
		String parentWindowHandle = driver.getWindowHandle();

		// Get the window handles of all open windows
		Set<String> allWindowHandles = driver.getWindowHandles();
		List<String> list = new ArrayList<>(allWindowHandles);
		// Switch to the child window
		for (String windowHandle : allWindowHandles) {
			if (list.get(list.size() - 2).equals(windowHandle)) {
				driver.switchTo().window(windowHandle);
				break;
			}
		}

	}

	// Method for SwitchToDefaultFrame
	private void SwitchToDefaultFrame() {
		driver.switchTo().defaultContent();
	}

	// Method for SwitchToFrame
	private void SwitchToFrame(Xpaths xpath) {
		// Elements elements = doc.select("iframe");
		Elements elements = GettingAllElements(xpath.getAction());
		String CssSelector = "";
		for (Element element : elements) {
			if (element.attr("id").equals(xpath.getInputParameter())) {
				CssSelector = element.cssSelector();
				break;

			} else if (element.attr("title").equals(xpath.getInputParameter())) {

			}
		}
		WebElement frameElement = driver.findElement(By.cssSelector(CssSelector));
		synchronized (frameElement) {
			while (frameElement == null) {
				try {
					frameElement.wait();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		driver.switchTo().frame(frameElement);

	}

	// Method for textArea
	private void textArea(Xpaths xpath) {
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		Elements headerElements = null;
		headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
		for (Element ele : headerElements) {
			if (splitInputParameter.length == 1) {
				WebElement selElement = driver.findElement(By.xpath(ele.cssSelector()));
				if (!selElement.isEnabled()) {
					continue;
				} else {
					selElement.clear();
					JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
					String script = "arguments[0].value = arguments[1]";
					jsExecutor.executeScript(script, selElement, xpath.getInputValue());
					break;
				}
			}
			WebElement selElement = findTheTextAreaElement(ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
			if (selElement == null) {
				continue;
			} else {
				selElement.clear();
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				String script = "arguments[0].value = arguments[1]";
				jsExecutor.executeScript(script, selElement, xpath.getInputValue());
				break;
			}
		}

	}

	private WebElement findTheTextAreaElement(Element ele, String[] copyOfRange) {
		Element parent = ele.parent();
		while (parent != null && parent.select(":matchesOwn(^" + copyOfRange[0] + "$)").isEmpty()) {
			parent = parent.parent();
		}
		if (parent != null) {
			Elements secondElements = parent.select(":matchesOwn(^" + copyOfRange[0] + "$)");
			for (Element element : secondElements) {
				if (copyOfRange.length > 1) {
					findTheTextAreaElement(element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
				} else {
					WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					Selelement = Selelement.findElement(By.xpath("./following::textarea"));
					if (!Selelement.isEnabled()) {
						continue;
					} else {
						return Selelement;
					}
				}
			}
		} else {
			return null;
		}

		return null;
	}

	// Method for Windowhandle
	private void windowhandle(Xpaths xpath) {
		String parentWindowHandle = driver.getWindowHandle();
		Set<String> windowHandles = driver.getWindowHandles();

		for (String windowHandle : windowHandles) {
			if (!windowHandle.equals(parentWindowHandle)) {
				driver.switchTo().window(windowHandle);
				break;
			}
		}

	}

	// Method for clickExpandorcollapse

	private void clickExpandorcollapse(Xpaths xpath) {
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		Elements headerElements = null;
		headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
		for (Element ele : headerElements) {
			if (splitInputParameter.length == 1) {

				WebElement selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
				if (!selElement.isEnabled()) {
					continue;
				} else {
					selElement.clear();
					JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
					String script = "arguments[0].value = arguments[1]";
					jsExecutor.executeScript(script, selElement, xpath.getInputValue());
					break;
				}

			}
			WebElement selElement = findThecllopseExpandElement(ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
			if (selElement == null) {
				continue;
			} else {
				// selElement.clear();
				selElement.click();
				break;
			}
		}

	}

	private WebElement findThecllopseExpandElement(Element ele, String[] copyOfRange) {
		Element parent = ele.parent();
		while (parent != null && parent.select("*[title=" + copyOfRange[0] + "]").isEmpty()) {
			parent = parent.parent();
		}
		if (parent != null) {
			Elements secondElements = parent.select("*[title=" + copyOfRange[0] + "]");
			for (Element element : secondElements) {
				if (copyOfRange.length > 1) {
					findThecllopseExpandElement(element, Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
				} else {
					WebElement Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					if (!Selelement.isEnabled()) {
						continue;
					} else {
						return Selelement;
					}
				}
			}
		} else {
			return null;
		}

		return null;
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
