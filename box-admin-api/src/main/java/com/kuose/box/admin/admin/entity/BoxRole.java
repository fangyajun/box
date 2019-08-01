package com.kuose.box.admin.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-22
 */
@Data
public class BoxRole extends Model<BoxRole> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    /**
     * 角色名称
     */
    @ApiModelProperty(value="角色名称")
    private String roleName;

    /**
     * 标记
     */
    @ApiModelProperty(value="角色英文名称")
    private String signName;

    /**
     * 角色描述
     */
    @ApiModelProperty(value="角色描述")
    private String roleDesc;

    /**
     * 是否启用
     */
    @ApiModelProperty(value="是否启用")
    private Boolean enabled;

    /**
     * 创建时间
     */
    private Long addTime;

    /**
     * 更新时间
     */
    private Long updateTime;

    /**
     * 逻辑删除
     */
    private Integer deleted;

}
