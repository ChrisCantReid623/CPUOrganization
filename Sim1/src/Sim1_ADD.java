/* Simulates a physical device that performs (signed) addition on
 * a 32-bit input.
 *
 * Author: Christopher Reid
 */

public class Sim1_ADD
{
	public void execute()
	{	
		boolean carryIn = false;
		for (int i=0; i<32; i++) {

			// 0 bits in column 
			if (!a[i].get() && !b[i].get() && !carryIn) {
				carryIn = false;
				sum[i].set(false);
			}
			// 3 bits in column - odd sum, carryIn of 1
			else if (a[i].get() && b[i].get() && carryIn) {
				carryIn = true;
				sum[i].set(true);
			}
			// 1 bit in column 
			else if (a[i].get() != b[i].get() != carryIn) {
				carryIn = false;
				sum[i].set(true);
			}
			
			// 2 bits in column - even sum, carryIn of 1
			else {
				carryIn = true;
				sum[i].set(false);
			}
		}

		// Set from MSB
		if (carryIn) {
			carryOut.set(true);
		}
		
		// Overflow
		// sum(neg, neg) = pos
		if ((a[31].get() && b[31].get()) && !sum[31].get()) {
			overflow.set(true);

		// sum(pos, pos) = neg
		} else if ((!a[31].get() && !b[31].get()) && sum[31].get()) {
			overflow.set(true);
		}
		else {
			overflow.set(false);
		}
	}

	// ------ 
	// It should not be necessary to change anything below this line,
	// although I'm not making a formal requirement that you cannot.
	// ------ 

	// inputs
	public RussWire[] a,b;

	// outputs
	public RussWire[] sum;
	public RussWire   carryOut, overflow;

	public Sim1_ADD()
	{
		/* Instructor's Note:
		 *
		 * In Java, to allocate an array of objects, you need two
		 * steps: you first allocate the array (which is full of null
		 * references), and then a loop which allocates a whole bunch
		 * of individual objects (one at a time), and stores those
		 * objects into the slots of the array.
		 */

		a   = new RussWire[32];
		b   = new RussWire[32];
		sum = new RussWire[32];

		for (int i=0; i<32; i++)
		{
			a  [i] = new RussWire();
			b  [i] = new RussWire();
			sum[i] = new RussWire();
		}

		carryOut = new RussWire();
		overflow = new RussWire();
	}
}