package com.huzhirong.efo.module.mapper;

import com.huzhirong.efo.dao.FileDAO;
import com.huzhirong.efo.entity.File;
import com.huzhirong.efo.model.FileRecord;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface FileMapper {

    List<FileRecord> getAll();
}
