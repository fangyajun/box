package com.kuose.box.admin.storage.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.kuose.box.admin.storage.entity.BoxStorage;

/**
 * <p>
 * 文件存储表 服务类
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-22
 */
public interface BoxStorageService extends IService<BoxStorage> {

    IPage<BoxStorage> listBoxStoragePage(Page<BoxStorage> boxStoragePage, String key, String name);
}
