CREATE TABLE t_user(
    iuser INT UNSIGNED PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR(50) NOT NULL,
    provider VARCHAR(10) DEFAULT 'local',
    pw VARCHAR(100),
    nm VARCHAR(50) NOT NULL,
    grade VARCHAR(50) DEFAULT NULL,
    authCd CHAR(6) COMMENT '회원가입 인증코드, null이면 인증받은 상태, 값이 있으면 인증해야 되는 상태',
    tel CHAR(13) COMMENT '연락처',
    mainProfile VARCHAR(50),
    regdt DATETIME DEFAULT NOW()
);

CREATE TABLE t_user_follow (
    from_iuser INT UNSIGNED,
    to_iuser INT UNSIGNED,
    PRIMARY KEY (from_iuser, to_iuser)
);

CREATE TABLE t_movie_fav (
    iuser INT UNSIGNED,
    title VARCHAR(30) NOT NULL,
    image VARCHAR(60),
    PRIMARY KEY (iuser, title)
);