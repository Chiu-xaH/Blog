package org.chiuxah.blog.ui.uitls

import org.chiuxah.blog.logic.network.bean.UserBean
import org.chiuxah.blog.logic.uitls.DateTimeManager

object UserManager {
    // 临时掌控用户信息，生命周期随应用关闭而销毁
    var userinfo = UserBean(-1,"游客","/upload/image/guest.png", create_time = DateTimeManager.DateTime_yyyy_MM_dd_HH_mm)
}