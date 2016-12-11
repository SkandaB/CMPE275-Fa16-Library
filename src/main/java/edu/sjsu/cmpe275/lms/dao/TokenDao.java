package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.entity.UserVerfToken;

public interface TokenDao {

    /**
     * @param token
     * @return
     */
    UserVerfToken findToken(String token);

    /**
     * @param userVerfToken
     */
    void storeToken(UserVerfToken userVerfToken);
}
