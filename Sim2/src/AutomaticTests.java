import java.util.Random;
import java.util.stream.IntStream;

/*
    Automatic AdderX tests.
    Author: Sap Mallick
*/
public class AutomaticTests
{
    //Configuration:

    public static final int TEST_COUNT = 20; //Number of tests to run per bit length
    public static final int MAX_BIT_LENGTH = 16; //Tests will run from 2 to this number (max: 31; ints are 32 bits)

    public static final Random random = new Random();

    public static void main(String[] args)
    {
        for(int i = 2; i <= MAX_BIT_LENGTH; i++)
            testAdder(i);
    }

    private static void testAdder(int bits)
    {
        System.out.printf("--------- Tests: %s-Bit Adder ---------%n", bits);

        int numFailed = 0;
        for(int i = 0; i < TEST_COUNT; i++)
        {
            Sim2_AdderX adder = new Sim2_AdderX(bits);

            //Initialize inputs
            for(int k = 0; k < bits; k++)
            {
                adder.a[k].set(random.nextBoolean());
                adder.b[k].set(random.nextBoolean());
            }

            adder.execute();

            //Compute expected and actual outputs
            int a = toDecimalSigned(adder.a);
            int b = toDecimalSigned(adder.b);

            int expected = a + b;
            int actual = toDecimalSigned(adder.sum);

            int minLimit = (int)-Math.pow(2, bits - 1);
            int maxLimit = (int)Math.pow(2, bits - 1) - 1;
            boolean expectedOverflow = expected < minLimit || expected > maxLimit;
            boolean actualOverflow = adder.overflow.get();

            //If overflow is expected, calculate the overflowed value (to input bits)
            if(expectedOverflow)
            {
                RussWire[] expectedWire = new RussWire[bits];
                for(int k = 0; k < bits; k++)
                {
                    expectedWire[k] = new RussWire();
                    expectedWire[k].set((expected >> k & 0x1) == 1);
                }
                expected = toDecimalSigned(expectedWire);
            }

            boolean failed = expected != actual || expectedOverflow != actualOverflow;

            if(failed)
            {
                System.out.printf("Failed | Sum: Expected {%s + %s = %s} | Actual {%s}; Overflow: Expected {%s} | Actual {%s} %n", a, b, expected, actual, expectedOverflow, actualOverflow);
                numFailed++;
            }
        }

        System.out.println(numFailed == 0 ? "Passed all %s test cases.".formatted(TEST_COUNT) : "%s / %s tests have failed, see above for details.".formatted(numFailed, TEST_COUNT));
        System.out.printf("---------- End: %s-Bit Adder ----------%n", bits);
    }

    private static int toDecimalSigned(RussWire[] bits)
    {
        return (int)IntStream.range(0, bits.length).filter(i -> bits[i].get()).mapToDouble(i -> Math.pow(2, i) * (i == bits.length - 1 ? -1 : 1)).sum();
    }
}
