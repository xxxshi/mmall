package com.mmall.dao;

import com.mmall.pojo.User;
import org.apache.ibatis.annotations.Param;

public interface UserMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(User record);

    int insertSelective(User record);

    User selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(User record);

    int updateByPrimaryKey(User record);

    int checkUsername(@Param("username")String username);

    User selectLogin(@Param("username")String username, @Param("password")String password);

    int checkEmail(String email);

    String selectQuestion(String username);

    int checkAnswer(@Param("username")String username,@Param("question")String question,@Param("answer")String answer);

    int updatePasswordByUsername(@Param("username")String username, @Param("passwordNew")String passwordNew);

    int checkPasswordById(@Param("userId")Integer userId,@Param("password")String password);

    /**
     * 不是当前用户的email，查询结果大于0，则此email已被使用
     * @param userId
     * @param email
     * @return
     */
    int checkEmailById(@Param("userId") Integer userId,@Param("email") String email);

}