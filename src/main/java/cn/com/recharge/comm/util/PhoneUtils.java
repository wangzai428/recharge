package cn.com.recharge.comm.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PhoneUtils {

    public static String URL = "https://phone.265o.com/?num={phone}";

    private static final String[] PHONES_HEADER_ARRAY = {"134", "135", "136", "137", "138", "139", "147", "148", "150", "151", "152", "157", "158", "159", "172", "178", "182", "183", "184", "187", "188", "195", "198", "130", "131", "132", "145", "146", "155", "156", "166", "171", "175", "176", "185", "186", "192", "165", "167", "170", "171"};

    public static List<String> PHONES_HEADER = new ArrayList<>(Arrays.asList(PHONES_HEADER_ARRAY));

}
