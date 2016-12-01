package edu.sjsu.cmpe275.lms.dao;

import edu.sjsu.cmpe275.lms.entity.UserVerfToken;

/**
 * Created by SkandaBhargav on 11/30/16.
 */
public interface TokenDao {
    UserVerfToken findToken(String token);

    void storeToken(UserVerfToken userVerfToken);
}
