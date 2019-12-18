SELECT
    -- games.SeasonID, games.Team1Name, games.Team2Name,
    -- ts1.VenueName, ts2.VenueName, games.VenueName,
    -- (CASE WHEN gv.VenueName != ts2.VenueName AND gv.VenueName != ts1.VenueName THEN 0.5
    --     ELSE (CASE WHEN gv.VenueName = ts1.VenueName THEN 1.0 ELSE 0.0 END) END) as t1_team_type,
    -- ts1.PlayoffSeed as t1_playoff_seed,
    tst1.Wins/(tst1.Wins+tst1.Losses) as t1_win_loss_ratio,
    tso1.Pts as t1_points_per_game,
    tsd1.Pts as t1_points_allowed_per_game,
    td1.Takeaways - to1.Turnovers as t1_turnover_ratio,
    -- to1.Rank as t1_offensive_rank,
    -- td1.Rank as t1_defensive_rank,
    -- tkpo1.Rank as t1_kicking_and_punting_rank,
    -- tkpro1.Rank as t1_kicking_and_punting_return_rank,
    tst1.StrengthOfSchedule as t1_relative_strength_of_schedule,
    to1.Penalties/ts1.GamesPlayed as t1_penalties_per_game,
    to1.PenaltyYards/ts1.GamesPlayed as t1_penalty_yards_per_game,
    tro1.Yards/ts1.GamesPlayed as t1_rush_yards_per_game,
    tro1.Yards/tro1.Attempts as t1_rush_yards_per_attempt,
    tso1.RushTD/ts1.GamesPlayed as t1_rush_touchdowns_per_game,
    tpo1.Yards/ts1.GamesPlayed as t1_pass_yards_per_game,
    tpo1.Yards/tpo1.Attempts as t1_pass_yards_per_attempt,
    tso1.RecTD/ts1.GamesPlayed as t1_receiving_touchdowns_per_game,
    to1.FirstDowns/ts1.GamesPlayed as t1_1st_downs_per_game,
    tco1."3DPCT" as t1_3rd_down_conversions_pct,
    tco1."4DPCT" as t1_4th_down_conversions_pct,
    tpo1.Sacks/ts1.GamesPlayed as t1_sacks_allowed_per_game,
    tro1.Fumbles/ts1.GamesPlayed as t1_fumbles_per_game,
    to1.FumblesLost/ts1.GamesPlayed as t1_fumbles_lost_per_game,
    tpo1.Interceptions/ts1.GamesPlayed t1_interceptions_thrown_per_game,
    -- tpo1.QBPasserRating as t1_starting_qb_passer_rating,
    trd1.Yards/ts1.GamesPlayed as t1_rush_yards_allowed_per_game,
    trd1.Yards/trd1.Attempts as t1_rush_yards_per_attempt_allowed,
    tsd1.RushTD/ts1.GamesPlayed as t1_rush_touchdowns_allowed_per_game,
    tpd1.Yards/ts1.GamesPlayed as t1_pass_yards_allowed_per_game,
    tpd1.Yards/tpd1.Attempts as t1_pass_yards_allowed_per_attempt_allowed,
    tso1.RecTD/ts1.GamesPlayed as t1_receiving_touchdowns_allowed_per_game,
    td1.FirstDowns/ts1.GamesPlayed as t1_1st_downs_allowed_per_game,
    tcd1."3DPCT" as t1_3rd_down_conversions_allowed_pct,
    tcd1."4DPCT" as t1_4th_down_conversions_allowed_pct,
    tpd1.Sacks/ts1.GamesPlayed as t1_sacks_per_game,
--     0 as t1_forced_fumbles_per_game,
    td1.FumblesLost/ts1.GamesPlayed as t1_forced_fumble_recoveries_per_game,
    tpd1.Interceptions/ts1.GamesPlayed as t1_interceptions_per_game,
    tso1.Safety+tso1.FumbleTD+tso1.IntTD as t1_defensive_touchdowns_per_game,
    tkpo1.FGM/tkpo1.FGA as t1_kicker_field_goal_pct,
    tkpo1.XPM/tkpo1.XPA as t1_kicker_pat_pct,
    tkpo1.PuntYards/tkpo1.Punts as t1_avg_yardage_per_punt,
    tkpo1.PuntBlock/tkpo1.Punts as t1_percent_blocked_punts,
    tkpd1.PuntYards/tkpd1.Punts as t1_opponent_avg_yardage_per_punt,
    tkpd1.PuntBlock/tkpd1.Punts as t1_opponent_percent_blocked_punts,
    tkpro1.KickReturnYards/tkpro1.KickReturns as t1_kick_return_yards_per_return,
    tso1.KickReturnTD/tkpro1.KickReturns as t1_kick_return_touchdowns_per_return,
    tkprd1.KickReturnYards/tkprd1.KickReturns as t1_opponent_kick_return_yards_per_return,
    tsd1.KickReturnTD/tkprd1.KickReturns as t1_opponent_kick_return_touchdowns_per_return,
    tkpro1.PuntReturnYards/tkpro1.PuntReturns as t1_punt_return_yards_per_return,
    tso1.PuntReturnTD/tkpro1.PuntReturns as t1_punt_return_touchdowns_per_return,
    tkprd1.PuntReturnYards/tkprd1.PuntReturns as t1_opponent_punt_return_yards_per_return,
    tsd1.PuntReturnTD/tkprd1.PuntReturns as t1_opponent_punt_return_touchdowns_per_return,
    -- tv1.Altitude as t1_stadium_altitude,
    -- tv1.Latitude as t1_stadium_latitude,
    -- tv1.Longitude as t1_stadium_longitude,
    -- tv1."Stadium-Type" as t1_stadium_type,
    -- tv1."Field-Type" as t1_field_type,
    -- (CASE WHEN gv.VenueName != ts2.VenueName AND gv.VenueName != ts1.VenueName THEN 0.5
    --     ELSE (CASE WHEN gv.VenueName = ts2.VenueName THEN 1.0 ELSE 0.0 END) END) as t2_team_type,
    -- ts2.PlayoffSeed as t2_playoff_seed,
    tst2.Wins/(tst2.Wins+tst2.Losses) as t2_win_loss_ratio,
    tso2.Pts as t2_points_per_game,
    tsd2.Pts as t2_points_allowed_per_game,
    td2.Takeaways - to2.Turnovers as t2_turnover_ratio,
    -- to2.Rank as t2_offensive_rank,
    -- td2.Rank as t2_defensive_rank,
    -- tkpo2.Rank as t2_kicking_and_punting_rank,
    -- tkpro2.Rank as t2_kicking_and_punting_return_rank,
    tst2.StrengthOfSchedule as  t2_relative_strength_of_schedule,
    to2.Penalties/ts2.GamesPlayed as t2_penalties_per_game,
    to2.PenaltyYards/ts2.GamesPlayed as t2_penalty_yards_per_game,
    tro2.Yards/ts2.GamesPlayed as t2_rush_yards_per_game,
    tro2.Yards/tro2.Attempts as t2_rush_yards_per_attempt,
    tso2.RushTD/ts2.GamesPlayed as t2_rush_touchdowns_per_game,
    tpo2.Yards/ts2.GamesPlayed as t2_pass_yards_per_game,
    tpo2.Yards/tpo2.Attempts as t2_pass_yards_per_attempt,
    tso2.RecTD/ts2.GamesPlayed as t2_receiving_touchdowns_per_game,
    to2.FirstDowns/ts2.GamesPlayed as t2_1st_downs_per_game,
    tco2."3DPCT" as t2_3rd_down_conversions_pct,
    tco2."4DPCT" as t2_4th_down_conversions_pct,
    tpo2.Sacks/ts2.GamesPlayed as t2_sacks_allowed_per_game,
    tro2.Fumbles/ts2.GamesPlayed as t2_fumbles_per_game,
    to2.FumblesLost/ts2.GamesPlayed as t2_fumbles_lost_per_game,
    tpo2.Interceptions/ts2.GamesPlayed t2_interceptions_thrown_per_game,
    -- tpo2.QBPasserRating as t2_starting_qb_passer_rating,
    trd2.Yards/ts2.GamesPlayed as t2_rush_yards_allowed_per_game,
    trd2.Yards/trd2.Attempts as t2_rush_yards_per_attempt_allowed,
    tsd2.RushTD/ts2.GamesPlayed as t2_rush_touchdowns_allowed_per_game,
    tpd2.Yards/ts2.GamesPlayed as t2_pass_yards_allowed_per_game,
    tpd2.Yards/tpd2.Attempts as t2_pass_yards_allowed_per_attempt_allowed,
    tso2.RecTD/ts2.GamesPlayed as t2_receiving_touchdowns_allowed_per_game,
    td2.FirstDowns/ts2.GamesPlayed as t2_1st_downs_allowed_per_game,
    tcd2."3DPCT" as t2_3rd_down_conversions_allowed_pct,
    tcd2."4DPCT" as t2_4th_down_conversions_allowed_pct,
    tpd2.Sacks/ts2.GamesPlayed as t2_sacks_per_game,
    -- 0 as t2_forced_fumbles_per_game,
    td2.FumblesLost/ts2.GamesPlayed as t2_forced_fumble_recoveries_per_game,
    tpd2.Interceptions/ts2.GamesPlayed as t2_interceptions_per_game,
    tso2.Safety+tso2.FumbleTD+tso2.IntTD as t2_defensive_touchdowns_per_game,
    tkpo2.FGM/tkpo2.FGA as t2_kicker_field_goal_pct,
    tkpo2.XPM/tkpo2.XPA as t2_kicker_pat_pct,
    tkpo2.PuntYards/tkpo2.Punts as t2_avg_yardage_per_punt,
    tkpo2.PuntBlock/tkpo2.Punts as t2_percent_blocked_punts,
    tkpd2.PuntYards/tkpd2.Punts as t2_opponent_avg_yardage_per_punt,
    tkpd2.PuntBlock/tkpd2.Punts as t2_opponent_percent_blocked_punts,
    tkpro2.KickReturnYards/tkpro2.KickReturns as t2_kick_return_yards_per_return,
    tso2.KickReturnTD/tkpro2.KickReturns as t2_kick_return_touchdowns_per_return,
    tkprd2.KickReturnYards/tkprd2.KickReturns as t2_opponent_kick_return_yards_per_return,
    tsd2.KickReturnTD/tkprd2.KickReturns as t2_opponent_kick_return_touchdowns_per_return,
    tkpro2.PuntReturnYards/tkpro2.PuntReturns as t2_punt_return_yards_per_return,
    tso2.PuntReturnTD/tkpro2.PuntReturns as t2_punt_return_touchdowns_per_return,
    tkprd2.PuntReturnYards/tkprd2.PuntReturns as t2_opponent_punt_return_yards_per_return,
    tsd2.PuntReturnTD/tkprd2.PuntReturns as t2_opponent_punt_return_touchdowns_per_return,
    -- tv2.Altitude as t2_stadium_altitude,
    -- tv2.Latitude as t2_stadium_latitude,
    -- tv2.Longitude as t2_stadium_longitude,
    -- tv2."Stadium-Type" as t2_stadium_type,
    -- tv2."Field-Type" as t2_field_type,
    -- gv.Altitude as game_stadium_altitude,
    -- gv.Latitude as game_stadium_latitude,
    -- gv.Longitude as game_stadium_longitude,
    -- gv."Stadium-Type" as game_stadium_type,
    -- gv."Field-Type" as game_stadium_field_type,
    (CASE WHEN games.Team1Score > games.Team2Score THEN 't1' ELSE 't2' END) as game_result
    FROM games,
         team_season ts1, team_season ts2,
         venue tv1, venue tv2,
         team_stats tst1, team_stats tst2,
         team_off to1, team_off to2,
         team_passing_off tpo1, team_passing_off tpo2,
         team_rushing_off tro1, team_rushing_off tro2,
         team_kick_punt_returns_off tkpro1, team_kick_punt_returns_off tkpro2,
         team_kick_punt_off tkpo1, team_kick_punt_off tkpo2,
         team_conversions_off tco1, team_conversions_off tco2,
         team_scoring_off tso1, team_scoring_off tso2,
         team_def td1, team_def td2,
         team_passing_def tpd1, team_passing_def tpd2,
         team_rushing_def trd1, team_rushing_def trd2,
         team_kick_punt_returns_def tkprd1, team_kick_punt_returns_def tkprd2,
         team_kick_punt_def tkpd1, team_kick_punt_def tkpd2,
         team_conversions_def tcd1, team_conversions_def tcd2,
         team_scoring_def tsd1, team_scoring_def tsd2,
         venue gv
        WHERE games.SeasonID = ts1.SeasonID AND games.SeasonID = ts2.SeasonID
            AND games.Team1Name = ts1.TeamName AND games.Team2Name = ts2.TeamName
            AND games.VenueName = gv.VenueName
            AND ts1.VenueName = tv1.VenueName AND ts2.VenueName = tv2.VenueName
            AND ts1.SeasonID = tst1.SeasonID AND ts2.SeasonID = tst2.SeasonID
            AND ts1.TeamName = tst1.TeamName AND ts2.TeamName = tst2.TeamName
            AND ts1.SeasonID = to1.SeasonID AND ts2.SeasonID = to2.SeasonID
            AND ts1.TeamName = to1.TeamName AND ts2.TeamName = to2.TeamName
            AND ts1.SeasonID = tpo1.SeasonID AND ts2.SeasonID = tpo2.SeasonID
            AND ts1.TeamName = tpo1.TeamName AND ts2.TeamName = tpo2.TeamName
            AND ts1.SeasonID = tro1.SeasonID AND ts2.SeasonID = tro2.SeasonID
            AND ts1.TeamName = tro1.TeamName AND ts2.TeamName = tro2.TeamName
            AND ts1.SeasonID = tkpro1.SeasonID AND ts2.SeasonID = tkpro2.SeasonID
            AND ts1.TeamName = tkpro1.TeamName AND ts2.TeamName = tkpro2.TeamName
            AND ts1.SeasonID = tkpo1.SeasonID AND ts2.SeasonID = tkpo2.SeasonID
            AND ts1.TeamName = tkpo1.TeamName AND ts2.TeamName = tkpo2.TeamName
            AND ts1.SeasonID = tco1.SeasonID AND ts2.SeasonID = tco2.SeasonID
            AND ts1.TeamName = tco1.TeamName AND ts2.TeamName = tco2.TeamName
            AND ts1.SeasonID = tso1.SeasonID AND ts2.SeasonID = tso2.SeasonID
            AND ts1.TeamName = tso1.TeamName AND ts2.TeamName = tso2.TeamName
            AND ts1.SeasonID = td1.SeasonID AND ts2.SeasonID = td2.SeasonID
            AND ts1.TeamName = td1.TeamName AND ts2.TeamName = td2.TeamName
            AND ts1.SeasonID = tpd1.SeasonID AND ts2.SeasonID = tpd2.SeasonID
            AND ts1.TeamName = tpd1.TeamName AND ts2.TeamName = tpd2.TeamName
            AND ts1.SeasonID = trd1.SeasonID AND ts2.SeasonID = trd2.SeasonID
            AND ts1.TeamName = trd1.TeamName AND ts2.TeamName = trd2.TeamName
            AND ts1.SeasonID = tkprd1.SeasonID AND ts2.SeasonID = tkprd2.SeasonID
            AND ts1.TeamName = tkprd1.TeamName AND ts2.TeamName = tkprd2.TeamName
            AND ts1.SeasonID = tkpd1.SeasonID AND ts2.SeasonID = tkpd2.SeasonID
            AND ts1.TeamName = tkpd1.TeamName AND ts2.TeamName = tkpd2.TeamName
            AND ts1.SeasonID = tcd1.SeasonID AND ts2.SeasonID = tcd2.SeasonID
            AND ts1.TeamName = tcd1.TeamName AND ts2.TeamName = tcd2.TeamName
            AND ts1.SeasonID = tsd1.SeasonID AND ts2.SeasonID = tsd2.SeasonID
            AND ts1.TeamName = tsd1.TeamName AND ts2.TeamName = tsd2.TeamName