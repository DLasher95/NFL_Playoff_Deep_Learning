import requests
from webscraper import scraper
from selenium import webdriver
from webscraper.arffconverter import ARFFConverter

# response = requests.get(base_url + "/years/2017")
# options = webdriver.FirefoxOptions()
# driver = webdriver.Firefox()
arffConverter = ARFFConverter()
arffConverter.write_arff_file()
# scraper.retrieve_data(driver)
