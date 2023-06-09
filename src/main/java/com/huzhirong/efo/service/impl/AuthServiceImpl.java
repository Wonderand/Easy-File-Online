package com.huzhirong.efo.service.impl;

import com.huzhirong.efo.config.SettingConfig;
import com.huzhirong.efo.dao.AuthDAO;
import com.huzhirong.efo.entity.Auth;
import com.huzhirong.efo.model.AuthRecord;
import com.huzhirong.efo.modules.constant.ConfigConsts;
import com.huzhirong.efo.util.BeanUtils;
import com.huzhirong.efo.util.ServiceUtils;
import com.huzhirong.efo.service.IAuthService;
import com.zhazhapan.modules.constant.ValueConsts;
import com.zhazhapan.util.Checker;
import com.zhazhapan.util.Formatter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthServiceImpl implements IAuthService {

    private final AuthDAO authDAO;

    @Autowired
    public AuthServiceImpl(AuthDAO authDAO) {this.authDAO = authDAO;}

    @Override
    public boolean addAuth(String files, String users, String auths) {
        if (Checker.isNotEmpty(files) && Checker.isNotEmpty(users) && Checker.isNotEmpty(auths)) {
            String[] file = files.split(ValueConsts.COMMA_SIGN);
            String[] user = users.split(ValueConsts.COMMA_SIGN);
            for (String f : file) {
                long fileId = Formatter.stringToLong(f);
                for (String u : user) {
                    int userId = Formatter.stringToInt(u);
                    if (Checker.isNull(authDAO.exists(userId, fileId))) {
                        Auth auth = new Auth(userId, fileId);
                        auth.setAuth(BeanUtils.getAuth(auths));
                        authDAO.insertAuth(auth);
                    }
                }
            }
        }
        return true;
    }

    @Override
    public boolean batchDelete(String ids) {
        return Checker.isNotEmpty(ids) && authDAO.batchDelete(ids);
    }

    @Override
    public boolean updateAuth(long id, String auths) {
        int[] auth = BeanUtils.getAuth(auths);
        return authDAO.updateAuthById(id, auth[0], auth[1], auth[2], auth[3], auth[4]);
    }

    @Override
    public List<AuthRecord> listAuth(String usernameOrEmail, String fileName, int offset) {
        long fileId = ServiceUtils.getFileId(fileName);
        int userId = ServiceUtils.getUserId(usernameOrEmail);
        return authDAO.listAuthBy(ValueConsts.ZERO_INT, userId, fileId, fileName, offset);
    }

    @Override
    public AuthRecord getByFileIdAndUserId(long fileId, int userId) {
        List<AuthRecord> authRecords = authDAO.listAuthBy(ValueConsts.ZERO_INT, userId, fileId, ValueConsts
                .EMPTY_STRING, ValueConsts.ZERO_INT);
        if (Checker.isNotEmpty(authRecords)) {
            return authRecords.get(0);
        }
        return null;
    }

    @Override
    public boolean insertDefaultAuth(int userId, long fileId) {
        int[] defaultAuth = SettingConfig.getAuth(ConfigConsts.AUTH_DEFAULT_OF_SETTING);
        Auth auth = new Auth(userId, fileId);
        auth.setAuth(defaultAuth[0], defaultAuth[1], defaultAuth[2], defaultAuth[3], defaultAuth[4]);
        return insertAuth(auth);
    }

    @Override
    public boolean insertAuth(Auth auth) {
        return authDAO.insertAuth(auth);
    }

    @Override
    public boolean removeByFileId(long fileId) {
        return authDAO.removeAuthByFileId(fileId);
    }
}
