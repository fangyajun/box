package com.kuose.box.admin.goods.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * <p>
 * 商品属性标签表
 * </p>
 *
 * @author fangyajun
 * @since 2019-08-23
 */
@Data
public class BoxGoodsAttributeLabel extends Model<BoxGoodsAttributeLabel> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 父节点，0,表示第一节点
     */
    @ApiModelProperty(value="父节点，0,表示头节点",example = "父节点，0:表示头节点")
    private Integer parentId;

    /**
     * 商品参数名称
     */
    @ApiModelProperty(value="商品参数名称",  example = "衣长")
    private String attributeName;

    /**
     * 商品参数编码
     */
    @ApiModelProperty(value="商品参数编码，新增不用传")
    private String attributeCode;

    @ApiModelProperty(value="头标签的类别")
    private String headNodeCategory;

    /**
     * 属性的标记
     */
    @ApiModelProperty(value="属性标记，特殊情况下需要填写，如需要单纯查出来的时候根据填写的attributeFlag查询", example = "一般不需要填写，需要单纯查出来的时候根据填写的attributeFlag查询,如:衣服的材质")
    private String attributeFlag;

    @ApiModelProperty(value="标签的排序")
    private Integer attributeSort;


    /**
     * 属性类别
     *
     */
    @ApiModelProperty(value="属性的类别，-1:无选择，如叶子节点，0:单选，1:多选，2:填空", example = "属性的类别，-1:无选择，如叶子节点，0:单选，1:多选，2:填空")
    private Integer type;

    /**
     * 创建时间
     */
    @ApiModelProperty(value="创建时间", hidden = true)
    private Long addTime;

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

    /**
     * 子节点
     */
    @ApiModelProperty(value="子节点", hidden = true)
    @TableField(exist = false)
    private List<BoxGoodsAttributeLabel> children;

}
