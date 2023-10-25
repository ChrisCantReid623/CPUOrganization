/*
 * Simulates a half adder.
 * 
 * Author: Christopher Reid
*/
public class Sim2_HalfAdder {

	// inputs
	public RussWire a, b;
	// outputs
	public RussWire sum, carry;

	// AND Gate
	public AND and;
	// XOR GATE
	public XOR xor;

	// Constructor
	public Sim2_HalfAdder() {
		a = new RussWire();
		b = new RussWire();
		sum = new RussWire();
		carry = new RussWire();

		xor = new XOR();
		and = new AND();
	}

	public void execute() {
		// XOR operation for sum
		xor.a.set(a.get());
		xor.b.set(b.get());
		xor.execute();
		sum.set(xor.out.get());

		// AND operation for carry
		and.a.set(a.get());
		and.b.set(b.get());
		and.execute();
		carry.set(and.out.get());
	}

}
