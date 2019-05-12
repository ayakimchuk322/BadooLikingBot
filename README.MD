# Badoo liking bot

### Prerequisites

Create next environment variables:

* BADOO_USER
* BADOO_PASSWORD
* BADOO_CITY
* BADOO_BLACK_LISTED_WORDS  
(this one is not required; the values should be comma separated)
* SELENIUM_CHROME_DRIVER_LOCATION  
(this one should point to a executable chrome driver)

Download chrome driver from http://chromedriver.chromium.org/downloads

### Build

```./gradlew clean build```

### Usage

```java -jar badoo_liking_bot.jar```

The bot will like only verified, online profiles with more than one photo, located exactly at your city, without black listed words in profile info section.

Additional parameters:

* ```-DnoOnline``` - skips check for online status; default is false
* ```-DonePhoto``` - skips check for number of photos; default is false
* ```-DlikesLimit``` - sets the limit for the likes; default is 50 