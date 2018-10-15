package com.hs3.home.bean;

import org.codehaus.jackson.map.annotate.JsonSerialize;

/**
 * @author jason.wang
 * 前台Lottery展示
 */
@JsonSerialize(include = JsonSerialize.Inclusion.NON_NULL)
public class LotteryPojo {
    private String id;
    private String name;
    private String image;
    private String content;
    private String groupName;
    private String groupId;
    private Integer weight;

    public static int compareByWeight(LotteryPojo lhs, LotteryPojo rhs) {
        if (lhs.getWeight().equals(rhs.getWeight())) {
            return 0;
        } else {
            return rhs.getWeight().compareTo(lhs.getWeight());
        }
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
