package com.kuose.box.admin.admin.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * <p>
 * 管理员表
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-22
 */
@ApiModel(value="管理员用户信息")
public class BoxAdmin extends Model<BoxAdmin> {

private static final long serialVersionUID=1L;

    @TableId(value = "id", type = IdType.AUTO)
    @ApiModelProperty(value="id,添加时不需要，修改和删除的时必传", dataType = "Integer")
    private Integer id;

    /**
     * 管理员名称
     */
    @ApiModelProperty(value="用户名", example = "admin123")
    private String username;

    /**
     * 管理员密码
     */
    @ApiModelProperty(value="密码",  example = "admin123")
    private String password;

    /**
     * 最近一次登录IP地址
     */
    @ApiModelProperty(hidden = true)
    private String lastLoginIp;

    /**
     * 最近一次登录时间
     */
    @ApiModelProperty(hidden = true)
    private Long lastLoginTime;

    /**
     * 头像图片
     */
    @ApiModelProperty(value="头像图片")
    private String avatar;

    /**
     * 创建时间
     */
    @ApiModelProperty(hidden = true)
    private Long addTime;

    /**
     * 更新时间
     */
    @ApiModelProperty(hidden = true)
    private Long updateTime;

    /**
     * 逻辑删除
     */
    @ApiModelProperty(hidden = true)
    private Boolean deleted;

    /**
     * 角色列表
     */
    @ApiModelProperty(hidden = true)
    private Integer[] roleIds;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Long getLastLoginTime() {
        return lastLoginTime;
    }

    public Integer[] getRoleIds() {
        return roleIds;
    }

    public void setRoleIds(Integer[] roleIds) {
        this.roleIds = roleIds;
    }

    public void setLastLoginTime(Long lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getLastLoginIp() {
        return lastLoginIp;
    }

    public void setLastLoginIp(String lastLoginIp) {
        this.lastLoginIp = lastLoginIp;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
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
        return "BoxAdmin{" +
        "id=" + id +
        ", username=" + username +
        ", password=" + password +
        ", lastLoginIp=" + lastLoginIp +
        ", lastLoginTime=" + lastLoginTime +
        ", avatar=" + avatar +
        ", addTime=" + addTime +
        ", updateTime=" + updateTime +
        ", deleted=" + deleted +
        ", roleIds=" + roleIds +
        "}";
    }

}
