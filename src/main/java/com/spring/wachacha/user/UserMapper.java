package com.spring.wachacha.user;

import com.spring.wachacha.movie.model.MovieFavEntity;
import com.spring.wachacha.user.model.UserDTO;
import com.spring.wachacha.user.model.UserDomain;
import com.spring.wachacha.user.model.UserEntity;
import org.apache.catalina.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface UserMapper {

    int join(UserEntity user);
    UserEntity selUser(UserEntity param);
    int updateAuth(UserEntity userEntity);
    int editPw(UserEntity userEntity);

    /* follow */
    int insFollow(UserDTO param);
    List<UserDomain> selFollower(UserEntity param);
    List<UserDomain> selFollowing(UserEntity param);
    int delFollow(UserDTO param);

    /* movie fav */
    int insMyMovie(MovieFavEntity param);
    List<MovieFavEntity> selMyMovie(UserEntity param);
    int delMyMovie(MovieFavEntity param);
}
