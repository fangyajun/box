/**
 * Copyright 2018 bejson.com
 */
package com.kuose.box.wx.express.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

/**
 * Auto-generated: 2018-07-19 22:27:22
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class ExpressInfo {

    @JsonProperty("LogisticCode")
    private String logisticCode;
    @JsonProperty("ShipperCode")
    private String shipperCode;
    @JsonProperty("Traces")
    private List<Traces> traces;
    @JsonProperty("State")
    private String state;
    @JsonProperty("EBusinessID")
    private String eBusinessID;
    @JsonProperty("Success")
    private boolean success;
    @JsonProperty("Reason")
    private String reason;

    private String shipperName;

}