/*
 * Author: Christopher Reid
 * CSC 252 Sim 3
 * 
 * Simulates a complete ALU component of a processor. 
 */

public class Sim3_ALU {

	// Inputs
	public RussWire[] aluOp;
	public RussWire[] a, b;
	public RussWire bNegate;

	// Outputs
	public RussWire[] result;

	public Sim3_ALUElement[] aluElements;

	boolean control_bitA;
	boolean control_bitB;
	boolean control_bitC;

	// Constructor
	public Sim3_ALU(int x) {
		aluOp = new RussWire[3];
		a = new RussWire[x];
		b = new RussWire[x];
		bNegate = new RussWire();
		result = new RussWire[x];

		for (int i = 0; i < 3; i++) {
			aluOp[i] = new RussWire();
		}

		for (int i = 0; i < x; i++) {
			a[i] = new RussWire();
			b[i] = new RussWire();
			result[i] = new RussWire();
		}

		aluElements = new Sim3_ALUElement[x];
		for (int i = 0; i < x; i++) {
			aluElements[i] = new Sim3_ALUElement();
		}
	}

	public void execute() {
		// Initial (carryIn) becomes true if subtracting (bNegate = true)
		boolean previousCarryOut = bNegate.get();

		for (int i = 0; i < result.length; i++) {

			// Set aluOp and bInvert
			aluElements[i].aluOp = aluOp;
			aluElements[i].bInvert = bNegate;

			// Set carryIn for the current ALU element
			aluElements[i].carryIn.set((previousCarryOut));

			// Set a and b for the current ALU element
			aluElements[i].a.set(a[i].get());
			aluElements[i].b.set(b[i].get());

			// Execute pass1
			aluElements[i].execute_pass1();

			// Update previousCarryOut for the next element
			previousCarryOut = aluElements[i].carryOut.get();
		}

		// Less value of ALU LSB source = MSB of ALU addResults
		aluElements[0].less.set(aluElements[aluElements.length - 1].addResult.get());

		// Set less value for remaining ALU element
		for (int i = 1; i < result.length; i++) {
			aluElements[i].less.set(false);
		}

		for (int i = 0; i < result.length; i++) {
			// Execute pass2
			aluElements[i].execute_pass2();

			// Set the result for the current ALU element
			result[i].set(aluElements[i].result.get());
		}
	}
}