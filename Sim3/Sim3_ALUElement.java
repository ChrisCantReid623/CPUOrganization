/*
 * Author: Christopher Reid
 * CSC 252 Sim 3
 * 
 * Simulates a single ALU component of a processor. 
 */

public class Sim3_ALUElement {

	// Inputs
	public RussWire[] aluOp;
	public RussWire bInvert, a, b, carryIn, less;

	// Outputs
	public RussWire result, addResult, carryOut;

	// MUX Results
	boolean andResult;
	boolean orResult;
	boolean xorResult;
	Sim2_FullAdder fullAdder;

	boolean control_bitA;
	boolean control_bitB;
	boolean control_bitC;

	// Constructor
	public Sim3_ALUElement() {
		aluOp = new RussWire[3];

		for (int i = 0; i < 3; i++) {
			aluOp[i] = new RussWire();
		}

		bInvert = new RussWire();
		a = new RussWire();
		b = new RussWire();
		carryIn = new RussWire();
		less = new RussWire();

		result = new RussWire();
		addResult = new RussWire();
		carryOut = new RussWire();
		fullAdder = new Sim2_FullAdder();

	}

	public void execute_pass1() {

		boolean bInverted = bInvert.get() ^ b.get();

		// Compute AND, OR, and XOR values
		andResult = a.get() & bInverted;
		orResult = a.get() | bInverted;
		xorResult = a.get() ^ bInverted;

		// Set inputs for the full adder
		fullAdder.a.set(a.get());
		fullAdder.b.set(bInverted);

		fullAdder.carryIn.set(carryIn.get());

		// Execute the full adder
		fullAdder.execute();

		// Set the addResult based on the sum output of the full adder
		addResult.set(fullAdder.sum.get());

		// Set the carryOut based on the carryOut output of the full adder
		carryOut.set(fullAdder.carryOut.get());
	}

	public void execute_pass2() {
		control_bitA = aluOp[2].get();
		control_bitB = aluOp[1].get();
		control_bitC = aluOp[0].get();
		// Compute the result based on aluOp values and the 'less' input
		boolean computedResult = ((!control_bitA && !control_bitB && !control_bitC && andResult)
				|| (!control_bitA && !control_bitB && control_bitC && orResult)
				|| (!control_bitA && control_bitB && !control_bitC && addResult.get())
				|| (!control_bitA && control_bitB && control_bitC && less.get())
				|| (control_bitA && !control_bitB && !control_bitC && xorResult));

		// Set the result based on aluOp and 'less' input
		result.set(computedResult);
	}
}