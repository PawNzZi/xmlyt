package cn.lingyikz.soundbook.soundbook.modle;

import java.io.Serializable;
import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Album implements Serializable {


    private Integer code;
    private String message;
    private DataDTO data;

    @NoArgsConstructor
    @Data
    public static class DataDTO implements Serializable{
        private Integer pageNum;
        private Integer pageSize;
        private Integer size;
        private Object orderBy;
        private Integer startRow;
        private Integer endRow;
        private Integer total;
        private Integer pages;
        private List<ListDTO> list;
        private Integer prePage;
        private Integer nextPage;
        private Boolean isFirstPage;
        private Boolean isLastPage;
        private Boolean hasPreviousPage;
        private Boolean hasNextPage;
        private Integer navigatePages;
        private List<Integer> navigatepageNums;
        private Integer navigateFirstPage;
        private Integer navigateLastPage;
        private Integer firstPage;
        private Integer lastPage;

        @NoArgsConstructor
        @Data
        public static class ListDTO implements Serializable{
            private Integer id;
            private String name;
            private String description;
            private Object categories;
            private Object tag;
            private Object author;
            private Object thumb;
            private Integer weight;
        }
    }
}
