package cn.lingyikz.soundbook.soundbook.modle.v2;

import lombok.Data;

@Data
public class UserInfo {
    private Boolean success;
    private Integer code;
    private String message;
    private User data;


}
