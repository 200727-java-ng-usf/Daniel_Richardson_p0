import com.revature.banking.services.UtilService;
import org.junit.Assert;
import org.junit.Test;

public class NumberInputValidationTest {
    private UtilService sut = UtilService.getInstance();


    @Test
    public void largeNumber(){
        double value = 9.166456789;
        sut.decimalRounding(value);
        Assert.assertEquals(9.16, value, .2);
    }
    @Test
    public void lowRound(){
        double value = 9.111111;
        sut.decimalRounding(value);
        Assert.assertEquals(9.11, value, .2);
    }
    @Test
    public void highRound(){
        double value = 9.1999;
        sut.decimalRounding(value);
        Assert.assertEquals(9.19, value, .2);
    }

}
