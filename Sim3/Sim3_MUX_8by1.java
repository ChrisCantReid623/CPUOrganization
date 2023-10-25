/*
 * Author: Christopher Reid
 * CSC 252 Sim 3
 * 
 * Simulates a 8 by 1 multiplexor component within a CPU. 
 */

public class Sim3_MUX_8by1 {

	// Inputs
	public RussWire[] control, in;

	// Outputs
	public RussWire out;

	// Constructor
	public Sim3_MUX_8by1() {
		control = new RussWire[3];
		in = new RussWire[8];
		out = new RussWire();

		for (int i = 0; i < 3; i++) {
			control[i] = new RussWire();
		}
		for (int i = 0; i < 8; i++) {
			in[i] = new RussWire();
		}
	}

	public void execute() {
		// Convert control array to binary value
		Boolean control_bitA = control[2].get();
		Boolean control_bitB = control[1].get();
		Boolean control_bitC = control[0].get();

		// Set the out RussWire based on the controlValue
		out.set((!control_bitA && !control_bitB && !control_bitC && in[0].get())
				|| (!control_bitA && !control_bitB && control_bitC && in[1].get())
				|| (!control_bitA && control_bitB && control_bitC && in[2].get())
				|| (!control_bitA && control_bitB && control_bitC && in[3].get())
				|| (control_bitA && !control_bitB && !control_bitC && in[4].get())
				|| (control_bitA && !control_bitB && control_bitC && in[5].get())
				|| (control_bitA && control_bitB && !control_bitC && in[6].get())
				|| (control_bitA && control_bitB && control_bitC && in[7].get()));
	}
}