package com.kuose.box.admin.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 角色表
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-22
 */
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
    private Boolean deleted;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public String getSignName() {
        return signName;
    }

    public void setSignName(String signName) {
        this.signName = signName;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getRoleDesc() {
        return roleDesc;
    }

    public void setRoleDesc(String roleDesc) {
        this.roleDesc = roleDesc;
    }

    public Long getAddTime() {
        return addTime;
    }

    public void setAddTime(Long addTime) {
        this.addTime = addTime;
    }

    public Long getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Long updateTime) {
        this.updateTime = updateTime;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

    @Override
    public String toString() {
        return "BoxRole{" +
        "id=" + id +
        ", enabled=" + enabled +
        ", addTime=" + addTime +
        ", updateTime=" + updateTime +
        ", deleted=" + deleted +
        "}";
    }
}
