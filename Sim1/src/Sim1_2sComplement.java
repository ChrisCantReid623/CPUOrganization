/* Simulates a physical device that performs 2's complement on a 32-bit input.
 *
 * Author: Christopher Reid
 */

public class Sim1_2sComplement
{
	public void execute()
	{
		for (int i=0; i<32; i++)
		{
			// Bitwise negation - input bits
			negate[i].in.set(in[i].get());
			negate[i].execute();

			// Load adder a,b
			adder.a[i].set(negate[i].out.get());
			adder.b[i].set(one[i].get());
		}
		adder.execute();
	
		// Set output bits
		for (int i=0; i<32; i++) {
			out[i].set(adder.sum[i].get());
		}
	}

	// you shouldn't change these standard variables...
	public RussWire[] in;
	public RussWire[] out;

	public Sim1_NOT[] negate;
	public RussWire[] one;
	public Sim1_ADD adder;

	public Sim1_2sComplement()
	{
		in = new RussWire[32];
		out = new RussWire[32];
		negate = new Sim1_NOT[32];
		one = new RussWire[32];
		
		for (int i=0; i<32; i++)
		{
			in     [i] = new RussWire();
			out    [i] = new RussWire();
			negate [i] = new Sim1_NOT();
			one    [i] = new RussWire();
		}

		// 32bit value of 1
		one[0].set(true);
		for (int i=1; i<32; i++)
		{
			one[i].set(false);
		}
	
		adder = new Sim1_ADD();
	}
}