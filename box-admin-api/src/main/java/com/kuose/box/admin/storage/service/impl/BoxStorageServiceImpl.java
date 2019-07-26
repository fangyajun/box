package com.kuose.box.admin.storage.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.kuose.box.admin.storage.dao.BoxStorageMapper;
import com.kuose.box.admin.storage.entity.BoxStorage;
import com.kuose.box.admin.storage.service.BoxStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 文件存储表 服务实现类
 * </p>
 *
 * @author fangyajun
 * @since 2019-07-22
 */
@Service
public class BoxStorageServiceImpl extends ServiceImpl<BoxStorageMapper, BoxStorage> implements BoxStorageService {

    @Autowired
    private BoxStorageMapper boxStorageMapper;

    @Override
    public IPage<BoxStorage> listBoxStoragePage(Page<BoxStorage> boxStoragePage, String key, String name) {
        return boxStorageMapper.selectPage(boxStoragePage, new QueryWrapper<BoxStorage>().like("storage_key", key).like("storage_name", name));
    }
}
