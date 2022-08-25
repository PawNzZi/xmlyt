package cn.lingyikz.soundbook.soundbook.pojo;

import java.io.Serializable;

import lombok.Data;

@Data
public class ItemHome implements Serializable {

    private String src;
    private String bookName;
    private String bookDescription ;
    private String id ;
}
