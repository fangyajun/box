package com.kuose.source.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

/**
 * <p>
 * 
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-28
 */
@Data
@TableName("ripreport_ProductInformation")
public class RipreportProductinformation extends Model<RipreportProductinformation> {

private static final long serialVersionUID=1L;


    @TableId(value = "autoid", type = IdType.AUTO)
    private Integer id;

    /**
     * 商品编号/货号
     */
    @TableField("productno")
    private String goodsNo;

    /**
     * 商品名称
     */
    @TableField("thirdCategoryStr")
    private String name;

    /**
     * 商品品牌
     */
    @TableField("firstCategory")
    private String brand;

    /**
     * 商品所属类目ID
     */
    @TableField("secondCategory")
    private String categoryCode;

    /**
     * 商品宣传图片列表，采用JSON数组格式
     */
    @TableField("img")
    private String img;

    /**
     * 商品天猫链接
     */
    @TableField("productUrl")
    private String tmallUrl;

    /**
     * 季节
     */
    @TableField("quarter")
    private String quarter;

    /**
     * 年份
     */
    @TableField("year")
    private String year;


    /**
     * 专柜价格
     */
    @TableField("webcost")
    private BigDecimal counterPrice;

    /**
     * 吊牌价格
     */
    @TableField("tagcost")
    private BigDecimal tagPrice;

    /**
     * 零售价格
     */
    @TableField("hdcost")
    private BigDecimal retailPrice;

    /**
     * 上架时间
     */
    @TableField("requireDate")
    private String registerDate;






////////////////////////////////////////////////////////////////

//    @TableId(value = "autoid", type = IdType.AUTO)
//    private Integer autoid;

    @TableField(value = "sizeTableAutoid", select = false)
    private Integer sizeTableAutoid;

//    private String productno;

//    @TableField("firstCategory")
//    private String firstCategory;

//    @TableField("secondCategory")
//    private String secondCategory;

    @TableField(value = "thirdCategory",select = false)
    private String thirdCategory;

    @TableField(value = "bidsReceived", select = false)
    private String bidsReceived;

//    private Double webcost;

    private Double ggcost;

    private Double jhscost;

//    private Double hdcost;

    private Double bzcost;

//    private Double tagcost;

    private Double incost;

    private String remark;

    private Integer flag;

    private Double retailcost;

    @TableField(value = "companyCode", select = false)
    private String companyCode;

    @TableField(value = "companyName", select = false)
    private String companyName;

    @TableField(value = "companyAutoid", select = false)
    private Integer companyAutoid;

    private Integer suites;

    @TableField(value = "secondCategoryStr", select = false)
    private String secondCategoryStr;

//    @TableField("thirdCategoryStr")
//    private String thirdCategoryStr;

    @TableField(value = "layoutBigType2Key", select = false)
    private String layoutBigType2Key;

    @TableField(value = "layoutBigType2Name", select = false)
    private String layoutBigType2Name;

    @TableField(value = "gbCode", select = false)
    private String gbCode;

    @TableField(value = "standardPrice", select = false)
    private Double standardPrice;

    @TableField(value = "dataFrom", select = false)
    private String dataFrom;

    @TableField(value = "promotionPrice", select = false)
    private Double promotionPrice;

    @TableField(value = "brandAutoid", select = false)
    private Integer brandAutoid;

    @TableField(value = "layoutTypeManageAutoid", select = false)
    private Integer layoutTypeManageAutoid;

//    @TableField("requireDate")
//    private String requireDate;

    @TableField(value = "vipRequireDate", select = false)
    private String vipRequireDate;

    @TableField(value = "newBand", select = false)
    private String newBand;

    @TableField(value = "lifeCycle", select = false)
    private String lifeCycle;

    @TableField(value = "platformProperty", select = false)
    private String platformProperty;

//    @TableField("productUrl")
//    private String productUrl;

    private Double vipcost;

    private Integer status;

//    private String img;

//    private String year;

//    private String quarter;

    @TableField(value = "planSeries", select = false)
    private String planSeries;

    @TableField(value = "productGroupName", select = false)
    private String productGroupName;

    @TableField(value = "teamNumber", select = false)
    private String teamNumber;

    @TableField(value = "saleStatus", select = false)
    private String saleStatus;

    @TableField(value = "lifeCycle1", select = false)
    private String lifeCycle1;

    private String msreplTranVersion;

    @TableField(value = "confirmDate", select = false)
    private String confirmDate;

    @TableField(value = "productName", select = false)
    private String productName;

    @TableField(value = "createPerson", select = false)
    private String createPerson;

    @TableField(value = "createTime", select = false)
    private String createTime;

    @TableField(value = "importFile", select = false)
    private String importFile;

    @TableField(value = "daiMaiState", select = false)
    private Integer daiMaiState;

    private Integer success;

    private Integer failure;

    private String result;

    private String sex;

    private Double sjcost;

    @TableField(value = "styleTowards", select = false)
    private String styleTowards;

    @TableField(value = "lastUpdateDatetime", select = false)
    private Date lastUpdateDatetime;

    @TableField(value = "sizeGroupId", select = false)
    private Integer sizeGroupId;

    @TableField(value = "sizeGroupType", select = false)
    private String sizeGroupType;

    @TableField(value = "kisSuccess", select = false)
    private Integer kisSuccess;

    @TableField(value = "kisFailure", select = false)
    private Integer kisFailure;

    @TableField(value = "kisResult", select = false)
    private String kisResult;

}
