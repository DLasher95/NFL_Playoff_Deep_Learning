import pandas as pd
import sqlite3
from io import StringIO 

class ARFFConverter:
  ARFF_HEADER = "@RELATION NFLPlayoffs\n\n"\
    "@ATTRIBUTE t1-team-type {home, away}\n"\
    "@ATTRIBUTE t1-playoff-seed continuous\n"\
    "@ATTRIBUTE t1-win-loss-ratio continuous\n"\
    "@ATTRIBUTE t1-points-per-game continuous\n"\
    "@ATTRIBUTE t1-points-allowed-per-game continuous\n"\
    "@ATTRIBUTE t1-turnover-ratio continuous\n"\
    "@ATTRIBUTE t1-offensive-rank continuous\n"\
    "@ATTRIBUTE t1-defensive-rank continuous\n"\
    "@ATTRIBUTE t1-kicking-and-punting-rank continuous\n"\
    "@ATTRIBUTE t1-kicking-and-punting-return-rank continuous\n"\
    "@ATTRIBUTE t1-relative-strength-of-schedule continuous\n"\
    "@ATTRIBUTE t1-penalties-per-game continuous\n"\
    "@ATTRIBUTE t1-penalty-yards-per-game continuous\n"\
    "@ATTRIBUTE t1-rush-yards-per-game continuous\n"\
    "@ATTRIBUTE t1-rush-yards-per-attempt continuous\n"\
    "@ATTRIBUTE t1-rush-touchdowns-per-game continuous\n"\
    "@ATTRIBUTE t1-pass-yards-per-game continuous\n"\
    "@ATTRIBUTE t1-pass-yards-per-attempt continuous\n"\
    "@ATTRIBUTE t1-receiving-touchdowns-per-game continuous\n"\
    "@ATTRIBUTE t1-1st-downs-per-game continuous\n"\
    "@ATTRIBUTE t1-3rd-down-conversions-pct continuous\n"\
    "@ATTRIBUTE t1-4th-down-conversions-pct continuous\n"\
    "@ATTRIBUTE t1-sacks-allowed-per-game continuous\n"\
    "@ATTRIBUTE t1-fumbles-per-game continuous\n"\
    "@ATTRIBUTE t1-fumbles-lost-per-game continuous\n"\
    "@ATTRIBUTE t1-interceptions-thrown-per-game continuous\n"\
    "@ATTRIBUTE t1-starting-qb-passer-rating continuous\n"\
    "@ATTRIBUTE t1-rush-yards-allowed-per-game continuous\n"\
    "@ATTRIBUTE t1-rush-yards-per-attempt-allowed continuous\n"\
    "@ATTRIBUTE t1-rush-touchdowns-allowed-per-game continuous\n"\
    "@ATTRIBUTE t1-pass-yards-allowed-per-game continuous\n"\
    "@ATTRIBUTE t1-pass-yards-allowed-per-attempt-allowed continuous\n"\
    "@ATTRIBUTE t1-receiving-touchdowns-allowed-per-game continuous\n"\
    "@ATTRIBUTE t1-1st-downs-allowed-per-game continuous\n"\
    "@ATTRIBUTE t1-3rd-down-conversions-allowed-pct continuous\n"\
    "@ATTRIBUTE t1-4th-down-conversions-allowed-pct continuous\n"\
    "@ATTRIBUTE t1-sacks-per-game continuous\n"\
    "@ATTRIBUTE t1-forced-fumble-recoveries-per-game continuous\n"\
    "@ATTRIBUTE t1-interceptions-per-game continuous\n"\
    "@ATTRIBUTE t1-defensive-touchdowns-per-game continuous\n"\
    "@ATTRIBUTE t1-kicker-field-goal-pct continuous\n"\
    "@ATTRIBUTE t1-kicker-pat-pct continuous\n"\
    "@ATTRIBUTE t1-avg-yardage-per-punt continuous\n"\
    "@ATTRIBUTE t1-percent-blocked-punts continuous\n"\
    "@ATTRIBUTE t1-opponent-avg-yardage-per-punt continuous\n"\
    "@ATTRIBUTE t1-opponent-percent-blocked-punts continuous\n"\
    "@ATTRIBUTE t1-kick-return-yards-per-return continuous\n"\
    "@ATTRIBUTE t1-kick-return-touchdowns-per-return continuous\n"\
    "@ATTRIBUTE t1-opponent-kick-return-yards-per-return continuous\n"\
    "@ATTRIBUTE t1-opponent-kick-return-touchdowns-per-return continuous\n"\
    "@ATTRIBUTE t1-punt-return-yards-per-return continuous\n"\
    "@ATTRIBUTE t1-punt-return-touchdowns-per-return continuous\n"\
    "@ATTRIBUTE t1-opponent-punt-return-yards-per-return continuous\n"\
    "@ATTRIBUTE t1-opponent-punt-return-touchdowns-per-return continuous\n"\
    "@ATTRIBUTE t1-stadium-altitude continuous\n"\
    "@ATTRIBUTE t1-stadium-type {open,dome}\n"\
    "@ATTRIBUTE t1-stadium-field-type {grass, turf}\n"\
    "@ATTRIBUTE t2-team-type {home, away}\n"\
    "@ATTRIBUTE t2-playoff-seed continuous\n"\
    "@ATTRIBUTE t2-win-loss-ratio continuous\n"\
    "@ATTRIBUTE t2-points-per-game continuous\n"\
    "@ATTRIBUTE t2-points-allowed-per-game continuous\n"\
    "@ATTRIBUTE t2-turnover-ratio continuous\n"\
    "@ATTRIBUTE t2-offensive-rank continuous\n"\
    "@ATTRIBUTE t2-defensive-rank continuous\n"\
    "@ATTRIBUTE t2-kicking-and-punting-rank continuous\n"\
    "@ATTRIBUTE t2-kicking-and-punting-return-rank continuous\n"\
    "@ATTRIBUTE t2-relative-strength-of-schedule continuous\n"\
    "@ATTRIBUTE t2-penalties-per-game continuous\n"\
    "@ATTRIBUTE t2-penalty-yards-per-game continuous\n"\
    "@ATTRIBUTE t2-rush-yards-per-game continuous\n"\
    "@ATTRIBUTE t2-rush-yards-per-attempt continuous\n"\
    "@ATTRIBUTE t2-rush-touchdowns-per-game continuous\n"\
    "@ATTRIBUTE t2-pass-yards-per-game continuous\n"\
    "@ATTRIBUTE t2-pass-yards-per-attempt continuous\n"\
    "@ATTRIBUTE t2-receiving-touchdowns-per-game continuous\n"\
    "@ATTRIBUTE t2-1st-downs-per-game continuous\n"\
    "@ATTRIBUTE t2-3rd-down-conversions-pct continuous\n"\
    "@ATTRIBUTE t2-4th-down-conversions-pct continuous\n"\
    "@ATTRIBUTE t2-sacks-allowed-per-game continuous\n"\
    "@ATTRIBUTE t2-fumbles-per-game continuous\n"\
    "@ATTRIBUTE t2-fumbles-lost-per-game continuous\n"\
    "@ATTRIBUTE t2-interceptions-thrown-per-game continuous\n"\
    "@ATTRIBUTE t2-starting-qb-passer-rating continuous\n"\
    "@ATTRIBUTE t2-rush-yards-allowed-per-game continuous\n"\
    "@ATTRIBUTE t2-rush-yards-per-attempt-allowed continuous\n"\
    "@ATTRIBUTE t2-rush-touchdowns-allowed-per-game continuous\n"\
    "@ATTRIBUTE t2-pass-yards-allowed-per-game continuous\n"\
    "@ATTRIBUTE t2-pass-yards-allowed-per-attempt-allowed continuous\n"\
    "@ATTRIBUTE t2-receiving-touchdowns-allowed-per-game continuous\n"\
    "@ATTRIBUTE t2-1st-downs-allowed-per-game continuous\n"\
    "@ATTRIBUTE t2-3rd-down-conversions-allowed-pct continuous\n"\
    "@ATTRIBUTE t2-4th-down-conversions-allowed-pct continuous\n"\
    "@ATTRIBUTE t2-sacks-per-game continuous\n"\
    "@ATTRIBUTE t2-forced-fumble-recoveries-per-game continuous\n"\
    "@ATTRIBUTE t2-interceptions-per-game continuous\n"\
    "@ATTRIBUTE t2-defensive-touchdowns-per-game continuous\n"\
    "@ATTRIBUTE t2-kicker-field-goal-pct continuous\n"\
    "@ATTRIBUTE t2-kicker-pat-pct continuous\n"\
    "@ATTRIBUTE t2-avg-yardage-per-punt continuous\n"\
    "@ATTRIBUTE t2-percent-blocked-punts continuous\n"\
    "@ATTRIBUTE t2-opponent-avg-yardage-per-punt continuous\n"\
    "@ATTRIBUTE t2-opponent-percent-blocked-punts continuous\n"\
    "@ATTRIBUTE t2-kick-return-yards-per-return continuous\n"\
    "@ATTRIBUTE t2-kick-return-touchdowns-per-return continuous\n"\
    "@ATTRIBUTE t2-opponent-kick-return-yards-per-return continuous\n"\
    "@ATTRIBUTE t2-opponent-kick-return-touchdowns-per-return continuous\n"\
    "@ATTRIBUTE t2-punt-return-yards-per-return continuous\n"\
    "@ATTRIBUTE t2-punt-return-touchdowns-per-return continuous\n"\
    "@ATTRIBUTE t2-opponent-punt-return-yards-per-return continuous\n"\
    "@ATTRIBUTE t2-opponent-punt-return-touchdowns-per-return continuous\n"\
    "@ATTRIBUTE t2-stadium-altitude continuous\n"\
    "@ATTRIBUTE t2-stadium-type {open,dome}\n"\
    "@ATTRIBUTE t2-stadium-field-type {grass, turf}\n"\
    "@ATTRIBUTE game-stadium-altitude continuous\n"\
    "@ATTRIBUTE game-stadium-type {open,dome}\n"\
    "@ATTRIBUTE game-stadium-field-type {grass,turf}\n"\
    "@ATTRIBUTE game-result {t1,t2}\n"\
    "\n"
    # "@ATTRIBUTE t1-stadium-latitude continuous\n"\
    # "@ATTRIBUTE t1-stadium-longitude continuous\n"\
    # "@ATTRIBUTE game-stadium-latitude continuous\n"\
    # "@ATTRIBUTE game-stadium-longitude continuous\n"\
    # "@ATTRIBUTE t2-stadium-latitude continuous\n"\
    # "@ATTRIBUTE t2-stadium-longitude continuous\n"\      
  def get_arff_query_from_db(self,query,conn):
    return pd.read_sql_query(query,conn)

  def create_arff_db(self,dataFrames):
    conn = sqlite3.connect('nflplayoffs.db')
    for key, val in dataFrames.items():
      val.to_sql(key,conn,if_exists="append")
    teamseason = pd.read_csv("csv-files/teamseason.csv")
    teamseason.to_sql("team_season",conn,if_exists="replace")
    venue = pd.read_csv("csv-files/venues.csv")
    venue.to_sql("venue",conn,if_exists="replace")

  def write_arff_file(self):
    conn = sqlite3.connect('nflplayoffs.db')
    query = ''
    query += open("data-processing/sqlquery.sql",'rU').read()
    pdData = pd.DataFrame(self.get_arff_query_from_db(query,conn))
    pdData.round(3)
    arff_data = StringIO()
    pdData.to_csv(arff_data,index=False,header=False)
    file_name = "nflplayoffs.arff"
    with open(file_name,'w', newline='') as file:
      file.write(self.ARFF_HEADER)
      file.write("@Data\n")
      file.write(arff_data.getvalue())