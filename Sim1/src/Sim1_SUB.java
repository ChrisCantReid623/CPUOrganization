/* Simulates a physical device that performs (signed) subtraction on
 * a 32-bit input.
 *
 * Author: Christopher Reid
 */

public class Sim1_SUB
{
	public void execute()
	{
		for (int i=0; i<32;i++) {
			negated.in[i].set(a[i].get());
		}
		negated.execute();

		for (int i=0; i<32;i++) {
			adder.a[i].set(negated.out[i].get());
			adder.b[i].set(b[i].get());
		}
		
		adder.execute();

		for (int i=0;i<32;i++) {
			sum[i].set(adder.sum[i].get());
		}
	}

	// --------------------
	// Don't change the following standard variables...
	// --------------------

	// inputs
	public RussWire[] a,b;

	// output
	public RussWire[] sum;


	public Sim1_2sComplement negated;
	public Sim1_ADD adder;

	public Sim1_SUB()
	{
		a   = new RussWire[32];
		b   = new RussWire[32];
		sum = new RussWire[32];

		for (int i=0; i<32; i++)
		{
			a  [i] = new RussWire();
			b  [i] = new RussWire();
			sum[i] = new RussWire();
		}

		negated = new Sim1_2sComplement();
		adder = new Sim1_ADD();
	}
}

