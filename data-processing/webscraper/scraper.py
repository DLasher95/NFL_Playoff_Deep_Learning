from selenium import webdriver
from lxml import etree
from bs4 import BeautifulSoup
from itertools import repeat
from webscraper.tableinfo import TableInfo
from webscraper.arffconverter import ARFFConverter
from pandas import DataFrame
base_url = "https://www.pro-football-reference.com"

data_dict_headers = {
  "games" : ["SeasonID","Team1Name","Team2Name","Team1Score","Team2Score","VenueName"],
  "team_stats" : ["SeasonID","TeamName","Wins","Losses","StrengthOfSchedule"],
  "team_off" : ["SeasonID","TeamName","Rank","Turnovers","FumblesLost","FirstDowns","Penalties","PenaltyYards","ScorePct","TurnoverPct"],
  "team_passing_off" : ["SeasonID","TeamName","Rank","Completions","Attempts","Yards","Interceptions","QBPasserRating","Sacks"],
  "team_rushing_off" : ["SeasonID","TeamName","Rank","Attempts","Yards","Fumbles"],
  "team_kick_punt_returns_off" : ["SeasonID","TeamName","Rank","PuntReturns","PuntReturnYards","KickReturns","KickReturnYards","AllPurposeYardage"],
  "team_kick_punt_off" : ["SeasonID","TeamName","Rank","FGA","FGM","XPA","XPM","Punts","PuntYards","PuntLong","PuntBlock"],
  "team_conversions_off" : ["SeasonID","TeamName","Rank","3DPCT","4DPCT","RZTDPCT"],
  "team_scoring_off" : ["SeasonID","TeamName","Rank","RushTD","RecTD","PuntReturnTD","KickReturnTD","FumbleTD","IntTD","OthTD","2PM","2PA","XPM","XPA","FGM","FGA","Safety","Pts"],
  "team_def" : ["SeasonID","TeamName","Rank","Takeaways","FumblesLost","FirstDowns","Penalties","PenaltyYards","ScorePct","TurnoverPct"],
  "team_passing_def" : ["SeasonID","TeamName","Rank","Completions","Attempts","Yards","Interceptions","QBPasserRating","Sacks"],
  "team_rushing_def" : ["SeasonID","TeamName","Rank","Attempts","Yards"],
  "team_kick_punt_returns_def" : ["SeasonID","TeamName","Rank","PuntReturns","PuntReturnYards","KickReturns","KickReturnYards"],
  "team_kick_punt_def" : ["SeasonID","TeamName","Rank","FGA","FGM","XPA","XPM","Punts","PuntYards","PuntBlock"],
  "team_conversions_def" : ["SeasonID","TeamName","Rank","3DPCT","4DPCT","RZTDPCT"],
  "team_scoring_def" : ["SeasonID","TeamName","Rank","RushTD","RecTD","PuntReturnTD","KickReturnTD","FumbleTD","IntTD","OthTD","2PM","2PA","XPM","XPA","FGM","FGA","Safety","Pts"],  
}
def convert_num_to_string(num):
  return "{0}".format(num)

def parse_value(input):
  try:
    val = float(input)        
    return round(val,3)
  except:
    val = input
  try:
    remove_percent = input.strip('%')
    val = float(remove_percent)/100
    return round(val,3)
  except:
    return input

def retrieve_data(driver):
  data_dict = {
    "games" : [],
    "team_stats": [],
    "team_off" : [],
    "team_passing_off" : [],
    "team_rushing_off" : [],
    "team_kick_punt_returns_off" : [],
    "team_kick_punt_off" : [],
    "team_conversions_off" : [],
    "team_scoring_off" : [],
    "team_def" : [],
    "team_passing_def" : [],
    "team_rushing_def" : [],
    "team_kick_punt_returns_def" : [],
    "team_kick_punt_def" : [],
    "team_conversions_def" : [],
    "team_scoring_def" : [],
  }

  conference_table_info = {
    TableInfo("AFC",[1,2,8],"team_stats"),
    TableInfo("NFC",[1,2,8],"team_stats")
  }

  off_table_data = {
    TableInfo("team_stats",[0,7,8,9,22,23,25,26],"team_off"),
    TableInfo("passing",[0,3,4,6,9,16,17],"team_passing_off"),
    TableInfo("rushing",[0,3,4,9],"team_rushing_off"),
    TableInfo("returns",[0,3,4,8,9,13],"team_kick_punt_returns_off"),
    TableInfo("kicking",[0,13,14,16,17,19,20,21,22],"team_kick_punt_off"),
    TableInfo("team_conversions",[0,5,8,11],"team_conversions_off"),
    TableInfo("team_scoring",[0,3,4,5,6,7,8,9,11,12,14,15,16,17,18,19],"team_scoring_off")
  }
  
  def_table_data = {
    TableInfo("team_stats",[0,7,8,9,22,23,25,26],"team_def"),
    TableInfo("passing",[0,3,4,6,9,16,17],"team_passing_def"),
    TableInfo("rushing",[0,3,4],"team_rushing_def"),
    TableInfo("returns",[0,3,4,8,9],"team_kick_punt_returns_def"),
    TableInfo("kicking",[0,3,4,6,7,9,10,11],"team_kick_punt_def"),
    TableInfo("team_conversions",[0,5,8,11],"team_conversions_def"),
    TableInfo("team_scoring",[0,3,4,5,6,7,8,9,11,12,14,15,16,17,18,19],"team_scoring_def")
  }

  for i in range(2009,2019,1):
    # Load the overall stats webpage for the given year
    driver.get(base_url + "/years/" + str(i))
    print(driver.title)
    # Determine which teams made the playoffs and grab game data
    playoff_teams = []
    playoff_results_table = driver.find_element_by_id("playoff_results").get_attribute('outerHTML')
    playoff_results_table_body = etree.HTML(playoff_results_table).find("body/table/tbody")
    playoff_results_table_iter = iter(playoff_results_table_body)
    for row in playoff_results_table_iter:
      # Gets just the team name
      winner = row[3].find("strong/a").text.split(" ")[-1]
      loser = row[5].find("a").text.split(" ")[-1]
      if winner not in playoff_teams:
        playoff_teams.append(winner)
      if loser not in playoff_teams:
        playoff_teams.append(loser)
      # Append game result to games data
      winner_score = int(row[7].find("strong").text)
      loser_score = int(row[8].text)
      game_link_elem = row[6].find("a")
      driver.get(base_url + game_link_elem.attrib["href"])
      scorebox_meta = driver.find_element_by_class_name("scorebox_meta").get_attribute("outerHTML")
      stadium_name = etree.HTML(scorebox_meta).find("body/div/div/a").text
      data_dict["games"].append([int(i),winner,loser,winner_score,loser_score,stadium_name])
      data_dict["games"].append([int(i),loser,winner,loser_score,winner_score,stadium_name])
    # Navigate back to get table data
    driver.get(base_url + "/years/" + str(i))
    # Iterate through playoff teams and fill general stats
    for conf in conference_table_info:
      data_table = driver.find_element_by_id(conf.tableID).get_attribute('outerHTML')
      data_table_body = etree.HTML(data_table).find("body/table/tbody")
      data = retrieve_team_data_from_table(data_table_body,str(i),playoff_teams,0,conf.datacols)
      append_data(data,data_dict[conf.mapkey])




    for tableinfo in off_table_data:
      data_table = driver.find_element_by_id(tableinfo.tableID).get_attribute('outerHTML')
      data_table_body = etree.HTML(data_table).find("body/table/tbody")
      data = retrieve_team_data_from_table(data_table_body,str(i),playoff_teams,1,tableinfo.datacols)
      append_data(data,data_dict[tableinfo.mapkey])

    # Laod the team defense stats webpage for the given year
    driver.get(base_url + "/years/" + str(i) + "/opp.htm")
    print(driver.title)
    for tableinfo in def_table_data:
      data_table = driver.find_element_by_id(tableinfo.tableID).get_attribute('outerHTML')
      data_table_body = etree.HTML(data_table).find("body/table/tbody")
      data = retrieve_team_data_from_table(data_table_body,i,playoff_teams,1,tableinfo.datacols)
      append_data(data,data_dict[tableinfo.mapkey])
  
  driver.quit()
  # Convert data dictionaries to dataframes
  pdDataFrames = dict()
  for key, val in data_dict.items():
    pdDataFrames[key] = DataFrame(val,columns = data_dict_headers[key])

  arffConverter = ARFFConverter()
  arffConverter.create_arff_db(pdDataFrames)
  arffConverter.write_arff_file()
  # Save to files
  # for key, val in data_dict.items():
  #   header = convert_row_to_csv(data_dict_headers[key]) + "\n"
  #   body = convert_table_to_csv(val)
  #   file_name = key + ".csv"
  #   with open(file_name,'w') as file:
  #     file.write(header)
  #     file.write(body)
  print("end")

def retrieve_team_data_from_table(table,year,teams,team_column_ind,data_columns):
  ret_table = []
  rows = iter(table)
  for row in rows:
    ret_row = [year]
    # Skip teams that are not in playoffs
    team_name_obj = row[team_column_ind]
    if team_name_obj.find("a") is None:
      continue
    team_name = team_name_obj.find("a").text.split(" ")[-1]
    if team_name not in teams:
      continue
    ret_row.append(team_name)
    for i in data_columns:
      val = row[i].text
      if (not (val and val.strip())):
        ret_row.append(0)
      else:
        ret_row.append(parse_value(val))
    ret_table.append(ret_row)
  return ret_table

def append_data(src_table,dest_table):
  rows = iter(src_table)
  for row in rows:
    dest_table.append(row)

def convert_table_to_csv(data):
  rows = iter(data)
  ret_string = ""
  for row in rows:
    ret_string += convert_row_to_csv(row)
    ret_string += "\n"
  return ret_string

def convert_row_to_csv(row):
  ret_string = ""
  cols = iter(row)
  for col in cols:
    if type(col) is str:
      ret_string += "\"" + col + "\""
    else:
      ret_string += convert_num_to_string(col)
    if col is not row[-1]:
      ret_string += ","
  return ret_string