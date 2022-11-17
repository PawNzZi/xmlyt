package cn.lingyikz.soundbook.soundbook.modle.v2;

import lombok.Data;

/**
 *
 *
 * @author Roy
 * @date 2022-11-16 16:38:20
 */

@Data
public class User  {

    private Long id;

    /**
     * 昵称
     */
    private String nickname;

    /**
     * 密码
     */
    private String password;

    /**
     * 邮箱
     */
    private String email;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 手机号码
     */
    private String phone;

    /**
     * 状态
     */
    private Integer status;

    /**
     * 等级
     */
    private Integer level;

}
