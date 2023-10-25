/*
 * Simulates a half adder.
 * 
 * Author: Christopher Reid
*/
public class Sim2_HalfAdder {
	
	public RussWire a,b;		// inputs
	public RussWire sum, carry;	// outputs
	
	public AND and;				// AND Gate
	public XOR xor;				// XOR GATE

	// Constructor
	public Sim2_HalfAdder() {
		a 			= new RussWire();
		b 			= new RussWire();
		sum			= new RussWire();
		carry		= new RussWire();
		
		xor			= new XOR();
		and 		= new AND();
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
