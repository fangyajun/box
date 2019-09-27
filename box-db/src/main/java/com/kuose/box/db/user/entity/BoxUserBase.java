package com.kuose.box.db.user.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * box_user_base
 * </p>
 *
 * @author fangyajun
 * @since 2019-09-09
 */
@Data
@TableName(resultMap = "BaseResultMap")
public class BoxUserBase extends Model<BoxUserBase> {

private static final long serialVersionUID=1L;

    /**
     * 主键id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 用户id
     */
    @ApiModelProperty(value="用户id")
    private Integer userId;

    /**
     * 身高
     */
    @ApiModelProperty(value="身高",example = "165")
    private String height;

    /**
     * 体重
     */
    @ApiModelProperty(value="体重",example = "50")
    private String weight;

    /**
     * 上装尺寸
     */
    @ApiModelProperty(value="上装尺寸,传编号",example = "102")
    private String topSize;

    /**
     * 连衣裙尺寸
     */
    @ApiModelProperty(value="连衣裙尺寸,传编号",example = "102")
    private String dressSize;

    /**
     * 下装尺寸
     */
    @ApiModelProperty(value="下装尺寸,传编号",example = "102")
    private String bottomsSize;

    /**
     * 牛仔裤尺寸
     */
    @ApiModelProperty(value="牛仔裤尺寸,传编号",example = "102")
    private String jeansSize;

    /**
     * 胸围
     */
    @ApiModelProperty(value="胸围,传编号")
    private String chestSize;

    /**
     * 运动鞋尺寸
     */
    @ApiModelProperty(value="运动鞋尺寸,传编号")
    private String sneakerSize;

    /**
     * 可接受上装价格区间,用-连接，如:50-300,价格不限用-1表示
     */
    @ApiModelProperty(value="可接受上装价格区间,用-连接，如:50-300,价格不限用-1表示",example = "50-200")
    private String topPrice;

    /**
     * 可接受连衣裙价格区间,用-连接，如:50-300,价格不限用-1表示
     */
    @ApiModelProperty(value="可接受连衣裙价格区间,用-连接，如:50-300,价格不限用-1表示",example = "100-200")
    private String dressPrice;

    /**
     * 可接受下装价格区间,用-连接，如:50-300,价格不限用-1表示
     */
    @ApiModelProperty(value="可接受下装价格区间,用-连接，如:50-300,价格不限用-1表示",example = "50-150")
    private String bottomsPrice;

    /**
     * 可接受配饰价格区间,用-连接，如:50-300,价格不限用-1表示
     */
    @ApiModelProperty(value="可接受配饰价格区间,用-连接，如:50-300,价格不限用-1表示")
    private String accessoryPrice;

    /**
     * 可接受外套价格区间,用-连接，如:50-300,价格不限用-1表示
     */
    @ApiModelProperty(value="可接受外套价格区间,用-连接，如:50-300,价格不限用-1表示",example = "150-300")
    private String overcoatPrice;

    /**
     * 可接受运动鞋价格区间,用-连接，如:50-300,价格不限用-1表示
     */
    @ApiModelProperty(value="可接受运动鞋价格区间,用-连接，如:50-300,价格不限用-1表示")
    private String sneakerPrice;

    /**
     * 避免的颜色，多个用数组表示
     */
    @ApiModelProperty(value="避免的颜色，多个用数组表示",example = "['101','131']")
    @TableField(el = "avoidColor,jdbcType=VARCHAR,typeHandler=com.kuose.box.db.mybatis.JsonStringArrayTypeHandler")
    private String[] avoidColor;

    /**
     * 避免的材质，多个用数组表示
     */
    @ApiModelProperty(value="避免的材质，多个用数组表示")
    @TableField(el = "avoidTexture,jdbcType=VARCHAR,typeHandler=com.kuose.box.db.mybatis.JsonStringArrayTypeHandler")
    private String[] avoidTexture;

    /**
     * 避免的类别，多个用数组表示
     *
     */
    @ApiModelProperty(value="避免的类别，多个用数组表示")
    @TableField(el = "avoidCategory,jdbcType=VARCHAR,typeHandler=com.kuose.box.db.mybatis.JsonStringArrayTypeHandler")
    private String[] avoidCategory;

    /**
     * 避免的花纹图案，多个用数组表示
     */
    @ApiModelProperty(value="避免的花纹图案，多个用数组表示")
    @TableField(el = "avoidFigure,jdbcType=VARCHAR,typeHandler=com.kuose.box.db.mybatis.JsonStringArrayTypeHandler")
    private String[] avoidFigure;

    /**
     * 职业
     */
    @ApiModelProperty(value="职业")
    private String profession;

    /**
     * 是否有穿正装要求：0 未知， 1有， 2 无
     */
    @ApiModelProperty(value="是否有穿正装要求：0 未知， 1有， 2 无")
    private Integer formalWear;

    /**
     * 用户全身照
     */
    @ApiModelProperty(value="用户全身照")
    private String userImg;

    /**
     * 用户留言
     */
    @ApiModelProperty(value="用户留言")
    private String leaveWord;

    /**
     * 收盒频率：0:主动索要， 1: 2到3周一次， 2:每月一次，3：2月一次
     */
    @ApiModelProperty(value="收盒频率：0:主动索要， 1: 2到3周一次， 2:每月一次，3：2月一次")
    private Integer frequency;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="用户设定的收盒实际", hidden = true)
    private Long expectTime;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间", hidden = true)
    private Long createTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(value="更新时间", hidden = true)
    private Long updateTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(value="逻辑删除", hidden = true)
    private Integer deleted;

}
