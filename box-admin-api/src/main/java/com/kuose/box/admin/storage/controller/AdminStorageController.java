package com.kuose.box.admin.storage.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.kuose.box.admin.storage.entity.BoxStorage;
import com.kuose.box.admin.storage.service.BoxStorageService;
import com.kuose.box.admin.storage.service.StorageService;
import com.kuose.box.common.config.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.IOException;

/**
 * @author 魔舞清华
 */
@Api(tags = {"对象存储，图片上传"})
@RestController
@RequestMapping("/admin/storage")
@Validated
public class AdminStorageController {
    private final Log logger = LogFactory.getLog(AdminStorageController.class);

    @Autowired
    private StorageService storageService;
    @Autowired
    private BoxStorageService boxStorageService;

    @ApiOperation(value="查询对象存储列表")
//    @RequiresPermissions("admin:storage:list")
//    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "查询")
    @GetMapping("/list")
    public Object list(String key, String name, @RequestParam(defaultValue = "1") Integer page, @RequestParam(defaultValue = "10") Integer limit) {
        Page<BoxStorage> boxStoragePage = new Page<>();
        boxStoragePage.setSize(limit);
        boxStoragePage.setCurrent(page);
        boxStoragePage.setDesc("add_time");

        IPage<BoxStorage> boxStorageIPage = boxStorageService.listBoxStoragePage(boxStoragePage, key, name);
        return Result.success().setData("boxStorageIPage", boxStorageIPage);
    }

    @ApiOperation(value="图片上传接口")
//    @RequiresPermissions("admin:storage:create")
//    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "上传")
    @PostMapping("/create")
    public Result create(@RequestParam("file") MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        BoxStorage boxStorage = storageService.store(file.getInputStream(), file.getSize(), file.getContentType(), originalFilename);
        return Result.success().setData("boxStorage", boxStorage);
    }

    @ApiOperation(value="对象存储详情")
//    @RequiresPermissions("admin:storage:read")
//    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "详情")
    @PostMapping("/read")
    public Result read(@NotNull Integer id) {
        BoxStorage boxStorage = boxStorageService.getById(id);
        if (boxStorage == null) {
            return Result.failure("参数值不对");
        }
        return Result.success().setData("boxStorage", boxStorage);
    }

    @ApiOperation(value="修改存储对象")
//    @RequiresPermissions("admin:storage:update")
//    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "编辑")
    @PostMapping("/update")
    public Result update(@RequestBody BoxStorage boxStorage) {
        if (boxStorage.getId() == null) {
            return Result.failure("缺少必须参数");
        }
        if (!boxStorageService.updateById(boxStorage)) {
            return Result.failure("更新数据失败");
        }
        return Result.success();
    }

    @ApiOperation(value="根据storageKey删除图片对象")
//    @RequiresPermissions("admin:storage:delete")
//    @RequiresPermissionsDesc(menu = {"系统管理", "对象存储"}, button = "删除")
    @PostMapping("/delete")
    public Object delete(@RequestBody BoxStorage boxStorage) {
        String key = boxStorage.getStorageKey();
        if (StringUtils.isEmpty(key)) {
            return Result.failure("缺少参数");
        }
        boxStorageService.remove(new QueryWrapper<BoxStorage>().eq("key", key));
        storageService.delete(key);
        return Result.success();
    }
}
