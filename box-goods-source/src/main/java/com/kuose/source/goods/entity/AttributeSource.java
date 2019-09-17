package com.kuose.source.goods.entity;

import lombok.Data;

/**
 * @author fangyajun
 * @description
 * @since 2019/9/11
 */
@Data
public class AttributeSource {
    private String attributeGroupName;
    private String attributeGroupType;
    private String attributeName;
    private Integer attributeOrder;
    private Integer selectWay;
    private String attributeValueName;
    private Integer attributeValueOrder;
}
