package com.huzhirong.efo.dao.sqlprovider;

import org.apache.ibatis.jdbc.SQL;


public class CommonSqlProvider {

    private CommonSqlProvider() {}

    public static String updateAuthById(String table) {
        return new SQL() {{
            UPDATE(table);
            SET("is_downloadable=#{isDownloadable}");
            SET("is_uploadable=#{isUploadable}");
            SET("is_deletable=#{isDeletable}");
            SET("is_updatable=#{isUpdatable}");
            SET("is_visible=#{isVisible}");
            WHERE("id=#{id}");
        }}.toString();
    }
}
