package finup.feather.bean;

public enum FilterEnum {
    basicInfo,submitTask,queryTagResult,asyncTagResult,doUtcSync
}

class testEnum{
    public static void main(String[] args) {
      System.out.println(FilterEnum.values());
        System.out.println(FilterEnum.basicInfo);
        for(FilterEnum ef : FilterEnum.values()){
            if(!"aa/submitTask".contains(ef.toString()))
            System.out.println("aa--"+ef.toString());
        }
    }
        }