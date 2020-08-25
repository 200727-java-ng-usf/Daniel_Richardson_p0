import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.regex.Pattern;

public class NumberInputValidationTest {

    @Test
    public void test(){

        double value = 9.166456789;
        value = Math.floor(value * 100) / 100;
        System.out.println(value);

    }



}
