package com.weixin

import com.sys.User

class WeiXinUser {
    static belongsTo = [user: User]
    String openId;

    String nickName;

    String province;

    String city;

    String country;

    String avatarUrl;

    static constraints = {
    }
}
