/*
 * Simulates a full adder by adding two half adders.
 * 
 * Author: Christopher Reid
*/
public class Sim2_FullAdder {

	// inputs
	public RussWire a, b, carryIn;
	// outputs
	public RussWire sum, carryOut;

	// OR Gate
	public OR or;
	// halfAdders
	public Sim2_HalfAdder halfAdd1, halfAdd2;

	// Constructor
	public Sim2_FullAdder() {
		// Create Wires
		a = new RussWire();
		b = new RussWire();
		carryIn = new RussWire();
		sum = new RussWire();
		carryOut = new RussWire();

		// Create Gates
		or = new OR();
		halfAdd1 = new Sim2_HalfAdder();
		halfAdd2 = new Sim2_HalfAdder();
	}

	public void execute() {
		// Set inputs for the half-adders
		halfAdd1.a.set(a.get());
		halfAdd1.b.set(b.get());
		halfAdd1.execute();

		// Set inputs for second half-adder
		halfAdd2.a.set(carryIn.get());
		halfAdd2.b.set(halfAdd1.sum.get());
		halfAdd2.execute();

		// Set outputs for the full adder
		sum.set(halfAdd2.sum.get());

		// OR operation for carryOut
		or.a.set(halfAdd1.carry.get());
		or.b.set(halfAdd2.carry.get());
		or.execute();
		carryOut.set(or.out.get());
	}

}
