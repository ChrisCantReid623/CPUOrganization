/*
 * Testcase for Sim2.
 * 
 * Author: Anthony
 * 
 * This testcase fully brute forces all of the inputs and outputs for the HalfAdder and FullAdder class
 * This testcase only partially brute forces the adderX class
 * This testcase tries to use non-bitwise ways to verify each output
 * 
 * The only two outputs from this class are
 *      a) "All tests passed."
 *      b) "Failed Test Case!\n" + failedCase
 * 
 * If you are going to run this 
 */

public class Test_BruteForce {
    // This String is the output for a failed test
    private static String failedCase;

    /*
     * Runs all 3 tests.
     *      halfAdderTests()
     *      fullAdderTests()
     *      adderXTests(6)
     * If a test fails, it short-circuits and prints the failed case
     */
    public static void main(String[] args)
    {
        if (halfAdderTests() && fullAdderTests() && adderXTests(6))
        {
            System.out.println("All tests passed.");
            return;
        }
        
        System.out.println("Failed Test Case!\n" + failedCase);
    }

    /*
     * Runs adderX tests with the number of gates from 2 to maxGates
     * I would not recommended to go above maxGates=7
     */
    public static boolean adderXTests(int maxGates)
    {
        for (int i = 2; i < maxGates+1; i++)
        {
            if (!adderXTest(i))
            {
                return false;
            }
        }
        return true;
    }

    /*
     * Runs a numGates-bus adder with 0-2^numGates as input A
     */
    private static boolean adderXTest(int numGates)
    {
        for (int i = 0; i < Math.pow(2, numGates); i++)
        {
            if (!adderXTester(numGates, i))
            {
                return false;
            }
        }
        return true;
    }

    /*
     * Runs a numGates-bus adder with 0-2^numGates as inputB
     */
    private static boolean adderXTester(int numGates, int a)
    {
        for (int i = 0; i < Math.pow(2, numGates); i++)
        {
            if (!adderXVerify(numGates, a, i))
            {
                return false;
            }
        }
        return true;
    }

    /*
     * Verifies whether this A and B gets the proper outputs from your code
     */
    private static boolean adderXVerify(int numGates, int a, int b)
    {
        // Running your code
        Sim2_AdderX ax = new Sim2_AdderX(numGates);
        for (int j = 0; j < numGates; j++)
        {
            ax.a[j].set(getBit(a, j));
            ax.b[j].set(getBit(b, j));
        }
        ax.execute();

        // Checking your sum, carryOut, and overflow
        
        // Used to identify this case with numGates, A, and B
        String identifier = "Gates: " + numGates + ". A: " + convertToString(numGates, a) + ". B: " + convertToString(numGates, b);
        // Used to verify sum and carryOut
        String sum = Integer.toBinaryString(a + b);


        // Short circuits if one fails
        if (
            verifySum(identifier, numGates, getSum(ax.sum), sum) &&
            verifyCarryOut(identifier, numGates, ax.carryOut.get(), sum) &&
            verifyOverflow(identifier, numGates, ax.overflow.get(), a, b)
        )
            return true;

        return false;
    }

    /*
     * Gets the bit at index in num
     * Uses bit-masking
     */
    private static boolean getBit(int num, int index)
    {
        return (0x1 & (num >> index)) == 1;
    }

    /*
     * Returns the string representation of the RussWire array as a binary string
     */
    private static String getSum(RussWire[] sum)
    {
        String a = "";
        for (RussWire b : sum)
            a = (b.get() ? "1" : "0") + a;
        return a;
    }

    /*
     * Verifies your sum
     */
    private static boolean verifySum(String identifier, int numGates, String yourSum, String sumString)
    {
        // zero-extension if needed
        String sumString1 = (sumString.length() < numGates ? "0".repeat(numGates - sumString.length()) : "") + sumString;

        // Strings start at MSB, rather than LSB, so the order is swapped
        int j = sumString1.length();
        int k = yourSum.length()-1;

        for (int i = 0; i < numGates; i++)
        {
            if (yourSum.charAt(k-i) != sumString1.charAt(j-1-i))
            {
                failedCase = identifier +
                    "\n\tExpected Sum: " + sumString1.substring(j-numGates, j) +
                    ". Your Sum: " + yourSum
                ;
                return false;
            }
        }
        return true;
    }
    
    /*
     * Verifies your carryOut
     */
    private static boolean verifyCarryOut(String identifier, int numGates, boolean yourCarryOut, String sumString)
    {
        // Basically if the sum I get has more bits than numGates, then there must be carryOut
        boolean carryOutTest = (sumString.length() > numGates) == yourCarryOut;

        if (!carryOutTest)
            failedCase = identifier + 
                "\n\tExpected CarryOut: " + getInt((sumString.length() > numGates)) + ". Your CarryOut: " + getInt(yourCarryOut)
                ;

        return carryOutTest;
    }

    /*
     * Verifies your overflow
     */
    private static boolean verifyOverflow(String identifier, int numGates, boolean yourOverflow, int a, int b)
    {
        double min_value = Math.pow(2, numGates-1) * -1; // 2^(numGates-1) * -1 EX: numGates=2 => -2
        double max_value = Math.pow(2, numGates-1)-1; // 2^(numGates-1)-1       EX: numGates=2 => 1

        // Gets the 2s Complement of A and B (which were unsigned)
        double x = convert(min_value, max_value, a); 
        double y = convert(min_value, max_value, b);

        double sum = x + y;

        // Basically, if the sum is in the range [min, max], then it has not overflowed
        // if the sum is not in the range, then it has overflowed
        boolean myOverflow = !(sum >= min_value && sum <= max_value);

        boolean overflowTest = yourOverflow == myOverflow;

        if (!overflowTest)
            failedCase = identifier + 
                "\n\tExpected Overflow: " + myOverflow + ". Your Overflow: " + yourOverflow
                ;

        return overflowTest;
    }

    /*
     * Converts an unsigned num into its 2s Complement between min and max
     */
    private static double convert(double min, double max, int num)
    {
        if (num > max)
            return 2 * min + num;
        if (num < min)
            return 2 * max + num;
        return num;
    }

    /*
     * Converts an unsigned num into its 2s Complement between min and max as a binary String
     */
    private static String convertToString(int numGates, int num)
    {
        int min_value = (int)Math.pow(2, numGates-1) * -1;
        int max_value = (int)Math.pow(2, numGates-1)-1;

        // Uses a mux to determine the value
        int val =
            (((num > max_value) ? 1 : 0) * (2 * min_value + num)) +
            (((num < min_value) ? 1 : 0) * (2 * max_value + num)) +
            (((num <= max_value) ? 1 : 0) * num)
        ;

        String convertedVal = Integer.toBinaryString(val);
        if (convertedVal.length() < numGates)
            convertedVal = "0".repeat(numGates - convertedVal.length()) + convertedVal;
        int firstIndex = convertedVal.length();

        return convertedVal.substring(firstIndex-numGates, firstIndex);
    }

    /*
     * Runs 8 FullAdder tests with hard-coded test cases
     * Short circuits and prints the failed test case
     */
    public static boolean fullAdderTests()
    {
        if (
            faTester("FullAdder Test 0", false, false, false, false, false) &&
            faTester("FullAdder Test 1", false, false, true, true, false) &&
            faTester("FullAdder Test 2", false, true, false, true, false) &&
            faTester("FullAdder Test 3", false, true, true, false, true) &&
            faTester("FullAdder Test 4", true, false, false, true, false) &&
            faTester("FullAdder Test 5", true, false, true, false, true) &&
            faTester("FullAdder Test 6", true, true, false, false, true) &&
            faTester("FullAdder Test 7", true, true, true, true, true)
        )
            return true;

        return false;
    }

    /*
     * Verifies whether your values match the test case's
     */
    private static boolean faTester(String testID, boolean a, boolean b, boolean carryIn, boolean sum, boolean carryOut)
    {
        // Running your code
        Sim2_FullAdder fa = new Sim2_FullAdder();
        fa.a.set(a); fa.b.set(b); fa.carryIn.set(carryIn);
        fa.execute();

        // Testing your values        
        boolean faTest = fa.sum.get() == sum && fa.carryOut.get() == carryOut;

        if (!faTest)
            failedCase = testID + ": " + getInt(a) + " + " + getInt(b) + " + " + getInt(carryIn) +
                "\n\tExpected Sum: " + getInt(sum) + ". Your Sum: " + getInt(fa.sum.get()) +
                "\n\tExpected CarryOut: " + getInt(carryOut) + ". Your CarryOut: " + getInt(fa.carryOut.get())
            ;

        return faTest;
    }

    /*
     * Runs 4 HalfAdder tests with hard-coded test cases
     * Short circuits and prints teh failed test case
     */
    public static boolean halfAdderTests()
    {
        if (
            halfAdderTester("HalfAdder Test 0", false, false, false, false) &&
            halfAdderTester("HalfAdder Test 1", false, true, true, false) &&
            halfAdderTester("HalfAdder Test 2", true, false, true, false) &&
            halfAdderTester("HalfAdder Test 3", true, true, false, true)
        )
            return true;

        return false;
    }

    // Verifies whether your values match the test case's
    private static boolean halfAdderTester(String testID, boolean a, boolean b, boolean sum, boolean carry)
    {
        // Running your code
        Sim2_HalfAdder ha = new Sim2_HalfAdder();
        ha.a.set(a); ha.b.set(b);
        ha.execute();

        // Testing your values
        boolean haTest = ha.sum.get() == sum && ha.carry.get() == carry;

        if (!haTest)
            failedCase = testID + ": " + getInt(a) + " + " + getInt(b) +
                "\n\tExpected Sum: " + getInt(sum) + ".\tYour Sum: " + getInt(ha.sum.get()) +
                "\n\tExpected Carry: " + getInt(carry) + ".\tYour Carry: " + getInt(ha.carry.get())
            ;
        return haTest;
    }

    private static String getInt(boolean x)
    {
        return x ? "1" : "0";
    }
}