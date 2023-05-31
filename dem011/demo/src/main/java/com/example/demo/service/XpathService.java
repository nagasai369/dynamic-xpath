package com.example.demo.service;

import org.joox.selector.CSS2XPath;
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
				try {
					previousEle.sendKeys(Keys.TAB);
				} catch (Exception e) {
					JavascriptExecutor jsTab = (JavascriptExecutor) driver;
					jsTab.executeScript(
							"document.activeElement.dispatchEvent(new KeyboardEvent('keydown',{'key':'Tab'}));");
				}
				try {
					Thread.sleep(1000);
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
			} else if (xpath.getAction().equals("datePicker")) {
				datePicker(xpath);
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
			} else if (xpath.getAction().equals("selectAvalue")) {
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
				selectAvalue(xpath);
			}

			if (xpath.getAction().equals("sendKeys") || xpath.getAction().equals("paste")
					|| xpath.getAction().equals("Select Dropdown Values") || xpath.getAction().equals("login")) {

			} else {
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
					WebElement Selelement = null;
					try {
						Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					} catch (Exception e) {
						Selelement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true)));
					}
					if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
						continue;
					} else {
						Selelement = Selelement.findElement(By.xpath("./following::input"));
						return Selelement;
					}
				}
			}
		} else {
			return null;
		}

		return null;
	}


	private List<WebElement> findDatePicker(Element ele, String[] copyOfRange) {
		List<WebElement> elemets= new ArrayList<>();
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
					WebElement Selelement = null;
					try {
						Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					} catch (Exception e) {
						Selelement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true)));
					}
					if (!Selelement.isEnabled() || !Selelement.isDisplayed()) {
						continue;
					} else {
						WebElement Selelement1 = Selelement.findElement(By.xpath("./following::input"));
						elemets.add(Selelement1);
						elemets.add(Selelement);

						return elemets ;
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
					selElement = selElement.findElement(By.xpath("./following::input"));
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
				WebElement selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
				selElement = selElement.findElement(By.xpath("./following::select"));
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
		if (headerElements.size() == 0) {
			headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
		}
		for (Element ele : headerElements) {
			if (splitInputParameter.length == 1) {
				WebElement selElement = null;
				try {
					selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
				} catch (Exception e) {
					selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				}
				if (!selElement.isEnabled() || !selElement.isDisplayed()) {
					continue;
				} else {
					selElement = selElement.findElement(By.xpath("./following::input"));
					previousEle = selElement;
					selElement.clear();
					JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
					jsExecutor.executeScript("arguments[0].click();", selElement);
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
				previousEle = selElement;
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				jsExecutor.executeScript("arguments[0].click();", selElement);
				String script = "arguments[0].value = arguments[1]";
				jsExecutor.executeScript(script, selElement, xpath.getInputValue());
				jsExecutor.executeScript("arguments[0].click();", selElement);
				break;
			}
		}

	}


	private void datePicker(Xpaths xpath) {
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		Elements headerElements = null;
		headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
		if (headerElements.size() == 0) {
			headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
		}
		for (Element ele : headerElements) {
			if (splitInputParameter.length == 1) {
				WebElement selElement = null;
				try {
					selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
				} catch (Exception e) {
					selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				}
				if (!selElement.isEnabled() || !selElement.isDisplayed()) {
					continue;
				} else {
					selElement = selElement.findElement(By.xpath("./following::input"));
					previousEle = selElement;
					selElement.clear();
					JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
					jsExecutor.executeScript("arguments[0].click();", selElement);
					String script = "arguments[0].value = arguments[1]";
					jsExecutor.executeScript(script, selElement, xpath.getInputValue()+ " ");
					jsExecutor.executeScript("var backspaceKeyEvent = new KeyboardEvent('keydown', { 'key': 'Backspace', 'code': 'Backspace', 'bubbles': true, 'cancelable': true }); document.dispatchEvent(backspaceKeyEvent);");
					break;
				}
			}
			List<WebElement> selElement = findDatePicker(ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
			if (selElement == null) {
				continue;
			} else {
				selElement.get(0).clear();
				previousEle = selElement.get(0);
				JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
				jsExecutor.executeScript("arguments[0].click();", selElement.get(0));
				String script = "arguments[0].value = arguments[1]";
				jsExecutor.executeScript(script, selElement.get(0), xpath.getInputValue());
				jsExecutor.executeScript("arguments[0].click();", selElement.get(0));
				jsExecutor.executeScript("arguments[0].click();", selElement.get(1));
				
				break;
			}
		}

	}

	// method for selectAValue
	// method for selectAValue

	private void selectAvalue(Xpaths xpath) {
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		Elements headerElements = null;
		headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
		if (headerElements.size() == 0) {
			headerElements = doc.select("*[data-value=" + splitInputParameter[0] + "]");
		}
		for (Element ele : headerElements) {
			if (splitInputParameter.length == 1) {
				boolean flag = false;
				WebElement selElement = null;
				try {
					selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
				} catch (Exception e) {
					selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				}
				if (!selElement.isEnabled() || !selElement.isDisplayed()) {
					continue;
				} else {
					// selElement = selElement.findElement(By.xpath("./following::input"));

					Element parent = ele.parent();
					while (parent != null && parent.select(":matchesOwn(^" + xpath.getInputValue() + "$)").isEmpty()) {
						parent = parent.parent();
					}
					if (parent != null) {
						Elements selectAValueElement = parent.select(":matchesOwn(^" + xpath.getInputValue() + "$)");
						for (Element element : selectAValueElement) {
							if (!element.parent().text().equals(element.text())) {
								continue;
							}
							WebElement selele = null;
							try {
								selele = driver.findElement(By.cssSelector(element.cssSelector()));
							} catch (Exception e) {
								selele = driver
										.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
												: CSS2XPath.css2xpath(element.cssSelector(), true)));
							}
							if (!selele.isEnabled() || !selele.isDisplayed()) {
								continue;
							} else {
								try {
									selele.click();
									flag = true;
									break;
								} catch (Exception e) {
									JavascriptExecutor executor = (JavascriptExecutor) driver;
									executor.executeScript("arguments[0].click();", selele);
									flag = true;
									break;

								}
							}
						}
						if (flag) {
							break;
						}
					}
				}
			}
			WebElement selElement = findTheElement(ele,
					Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
			if (selElement == null) {
				continue;
			} else {
				selElement.click();
				try {
					selElement.click();
					break;
				} catch (Exception e) {
					JavascriptExecutor executor = (JavascriptExecutor) driver;
					executor.executeScript("arguments[0].click();", selElement);
					break;

				}
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
				WebElement selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
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
				WebElement selElement = null;
				try {
					selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
				} catch (Exception ex) {
					selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
							: CSS2XPath.css2xpath(ele.cssSelector(), true)));
				}
				if (!selElement.isEnabled() || !selElement.isDisplayed()) {
					continue;
				}
				selElement = findDropDownEle(ele,
						Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length), xpath.getInputValue());
				if (selElement == null) {
					continue;
				} else {
					try {
						selElement.click();
					} catch (Exception e) {
						JavascriptExecutor executor = (JavascriptExecutor) driver;
						executor.executeScript("arguments[0].click();", selElement);
					}

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
					WebElement Selelement = null;
					try {
						Selelement = driver.findElement(By.cssSelector(element.cssSelector()));
					} catch (Exception ex) {
						Selelement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
										: CSS2XPath.css2xpath(element.cssSelector(), true)));
					}
					if (!Selelement.isEnabled() || !Selelement.isDisplayed()
					// || !Selelement.isClickable()
					) {
						continue;
					}
					// Selelement = Selelement.findElement(By.xpath("./following::input"));
					WebElement Selelement1 = Selelement.findElement(By.xpath("./following::button"));
					if (!Selelement1.isEnabled() || !Selelement1.isDisplayed()) {
						continue;
					} else {
						try {
							Selelement1.click();
						} catch (Exception ex) {
							try {
								Selelement1.click();
							} catch (Exception e) {
								JavascriptExecutor executor = (JavascriptExecutor) driver;
								executor.executeScript("arguments[0].click();", Selelement1);
							}
						}
						// JavascriptExecutor jsExecutor = (JavascriptExecutor) driver;
						// CompletableFuture<Object> updateDo = this.updateDOM(jsExecutor);
						// try {
						// 	Object update = updateDo.get();
						// } catch (InterruptedException | ExecutionException e) {
						// 	// TODO Auto-generated catch block
						// 	e.printStackTrace();
						// }
						// findDropdownOption(element, inputValue);
						WebElement SearchSelelement = Selelement
						.findElement(By.xpath("./following::*[text()='" + inputValue + "']"));
						return SearchSelelement;
					}
				}
			}
		} else {
			return null;
		}

		return null;
	}

	private WebElement findDropdownOption(Element element, String inputValue) {
		Element parent = element.parent();
		while (parent != null) {
			boolean gotElement = false;
			if (!parent.select(":matchesOwn(^" + inputValue + "$)").isEmpty()) {
				Elements eles = parent.select(":matchesOwn(^" + inputValue + "$)");
				for (Element elem : eles) {
					WebElement selEle = null;
					try {
						selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
					} catch (Exception ex) {
						try {
							selEle = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
									: CSS2XPath.css2xpath(elem.cssSelector(), true)));
						} catch (Exception exe) {
							try {
								selEle = driver.findElement(By.xpath("//*[@class='" + elem.parent().attr("class")
										+ "']/child::*[@class='" + elem.attr("class") + "']"));
							} catch (Exception exep) {
								continue;
							}
							// continue;
						}
					}
					if (selEle.isDisplayed() && selEle.isEnabled()) {
						gotElement = true;
						return selEle;

					}
				}
			}
			if (!gotElement) {
				parent = parent.parent();
			}
		}
		return null;
	}

	// method for click Link
	private void clickLink(Xpaths xpath) {

		// Elements elements = GettingAllElements(xpath.getAction());
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		WebElement cssSelectorEle = null;
		if (splitInputParameter.length == 1) {
			Elements elements = doc.select(":matchesOwn(^" + splitInputParameter[0] + "$)");
			for (Element element : elements) {
				WebElement ele = null;
				try {
					ele = driver.findElement(By.cssSelector(element.cssSelector()));
				} catch (Exception e) {
					ele = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
							: CSS2XPath.css2xpath(element.cssSelector(), true)));
				}
				if (!ele.isDisplayed() || !ele.isEnabled()) {
					continue;

				} else {
					cssSelectorEle = ele;
					break;
				}
			}
		} else {
			Elements headerElements = null;
			headerElements = doc.select("*:matchesOwn(^" + splitInputParameter[0] + "$)");
			for (Element elem : headerElements) {
				WebElement headerSelElement = null;
				try {
					headerSelElement = driver.findElement(By.cssSelector(elem.cssSelector()));
				} catch (Exception ex) {
					try {
						headerSelElement = driver
								.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
										: CSS2XPath.css2xpath(elem.cssSelector(), true)));
					} catch (Exception exe) {
						if (elem.attr("id") != "") {
							headerSelElement = driver.findElement(By.id(elem.attr("id")));
						}

					}

				}
				if (!headerSelElement.isDisplayed() || !headerSelElement.isEnabled()) {
					continue;
				} else {
					WebElement selElement = findLinkElement(elem,
							Arrays.copyOfRange(splitInputParameter, 1, splitInputParameter.length));
					if (selElement == null) {
						continue;
					} else {
						cssSelectorEle = selElement;
						break;
					}
				}
			}

		}
		if (cssSelectorEle != null) {

			// WebElement buttonElement =
			// driver.findElement(By.cssSelector(cssSelectorEle));
			WebElement buttonElement = cssSelectorEle;

			synchronized (buttonElement) {
				while (buttonElement == null) {
					try {
						buttonElement.wait();
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
			}
			try {
				buttonElement.click();
			} catch (Exception e) {
				JavascriptExecutor executor = (JavascriptExecutor) driver;
				executor.executeScript("arguments[0].click();", buttonElement);
			}
		}
	}

	private WebElement findLinkElement(Element ele, String[] copyOfRange) {
		Element parent = ele.parent();
		ELements seconElement = findSecondELement(parent, copyOfRange[0]);
		if (copyOfRange.length > 1) {
			findLinkElement(seconElement.getElement(), Arrays.copyOfRange(copyOfRange, 1, copyOfRange.length));
		} else {
			WebElement Selelement = seconElement.getWebElement();
			return Selelement;
		}
		return null;
	}

	private ELements findSecondELement(Element parent, String string) {
		ELements elements = new ELements();
		while (parent != null) {
			boolean gotElement = false;
			if (!parent.select(":matchesOwn(^" + string + "$)").isEmpty()) {
				Elements eles = parent.select(":matchesOwn(^" + string + "$)");
				for (Element elem : eles) {
					WebElement selEle = null;
					try {
						selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
					} catch (Exception ex) {
						try {
							selEle = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
									: CSS2XPath.css2xpath(elem.cssSelector(), true)));
						} catch (Exception exe) {
							try {
								selEle = driver.findElement(By.xpath("//*[@class='" + elem.parent().attr("class")
										+ "']/child::*[@class='" + elem.attr("class") + "']"));
							} catch (Exception exep) {
								continue;
							}
							// continue;
						}
					}
					if (selEle.isDisplayed() && selEle.isEnabled()) {
						gotElement = true;
						elements.setWebElement(selEle);
						elements.setElement(elem);
						return elements;

					}
				}
			} else if (!parent.select("[alt='" + string + "']").isEmpty()) {
				Elements eles = parent.select("[alt='" + string + "']");
				for (Element elem : eles) {
					WebElement selEle = null;
					try {
						selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
					} catch (Exception ex) {
						selEle = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
								: CSS2XPath.css2xpath(elem.cssSelector(), true)));
					}
					if (selEle.isDisplayed() && selEle.isEnabled()) {
						gotElement = true;
						elements.setWebElement(selEle);
						elements.setElement(elem);
						return elements;

					}
				}
			} else if (!parent.select("[title='" + string + "']").isEmpty()) {
				Elements eles = parent.select("[title='" + string + "']");
				for (Element elem : eles) {
					WebElement selEle = null;
					try {
						selEle = driver.findElement(By.cssSelector(elem.cssSelector()));
					} catch (Exception ex) {
						selEle = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(elem.cssSelector())) ? null
								: CSS2XPath.css2xpath(elem.cssSelector(), true)));
					}
					if (selEle.isDisplayed() && selEle.isEnabled()) {
						gotElement = true;
						elements.setWebElement(selEle);
						elements.setElement(elem);
						return elements;
					}
				}
			}
			if (!gotElement) {
				parent = parent.parent();
			}
		}

		return null;
	}

	// method for click Link
	private void clickImage(Xpaths xpath) {

		Elements elements = GettingAllElements(xpath.getAction());
		String[] splitInputParameter = xpath.getInputParameter().split(">");
		String cssSelectorEle = "";
		if (splitInputParameter.length == 1) {

			Elements imagElements = doc.select("*[title='" + splitInputParameter[0] + "']");
			for (Element element : imagElements) {
				WebElement imgElement = null;
				try {
					imgElement = driver.findElement(By.cssSelector(element.cssSelector()));
				} catch (Exception e) {
					imgElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(element.cssSelector())) ? null
							: CSS2XPath.css2xpath(element.cssSelector(), true)));
				}
				if (!imgElement.isDisplayed() || !imgElement.isEnabled()) {
					continue;
				} else {
					try {
						imgElement.click();
					} catch (Exception e) {
						JavascriptExecutor executor = (JavascriptExecutor) driver;
						executor.executeScript("arguments[0].click();", imgElement);
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
						|| element.attr("alt").trim().equals(xpath.getInputParameter())
						|| element.attr("value").equals(xpath.getInputParameter()))) {
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

				WebElement selElement = null;
				try {
					selElement = driver.findElement(By.cssSelector(ele.cssSelector()));
				} catch (Exception e) {
					try {
						selElement = driver.findElement(By.xpath(("\\#root".equalsIgnoreCase(ele.cssSelector())) ? null
								: CSS2XPath.css2xpath(ele.cssSelector(), true)));
					} catch (Exception ex) {
						selElement = driver.findElement(By.xpath("//*[text()='" + splitInputParameter[0] + "']"));
					}
				}
				if (!selElement.isEnabled() || !selElement.isDisplayed()) {
					continue;
				} else {
					selElement = selElement.findElement(By.xpath("./following::textarea"));
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
