package com.hs3.home.bean;

import com.hs3.json.JsonDateTimeSerializer;
import org.codehaus.jackson.map.annotate.JsonSerialize;

import java.util.Date;

/**
 * @author jason.wang
 */
public class LotterySaleTimePojo {
    private String seasonId;
    private String lotteryId;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date beginTime;
    @JsonSerialize(using = JsonDateTimeSerializer.class)
    private Date endTime;
    private Integer restSeconds;
    private String lastSeasonId;

    public String getSeasonId() {
        return seasonId;
    }

    public void setSeasonId(String seasonId) {
        this.seasonId = seasonId;
    }

    public String getLotteryId() {
        return lotteryId;
    }

    public void setLotteryId(String lotteryId) {
        this.lotteryId = lotteryId;
    }

    public Date getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(Date beginTime) {
        this.beginTime = beginTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Integer getRestSeconds() {
        return restSeconds;
    }

    public void setRestSeconds(Integer restSeconds) {
        this.restSeconds = restSeconds;
    }

	public String getLastSeasonId() {
		return lastSeasonId;
	}

	public void setLastSeasonId(String lastSeasonId) {
		this.lastSeasonId = lastSeasonId;
	}


}
