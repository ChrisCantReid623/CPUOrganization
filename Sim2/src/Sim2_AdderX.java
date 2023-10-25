/*
 * Simulates a multi-bit adder by using multiple full adders..
 * 
 * Author: Christopher Reid
*/
public class Sim2_AdderX {
	
	public RussWire[] 		a,b;					// inputs
	public RussWire[] 		sum;					// outputs
	public RussWire   		carryOut, overflow;
	public Sim2_FullAdder[] fullAdders;
	public boolean[] 		carry;
	
	// Constructor
	public Sim2_AdderX(int x) {
		a 			= 	new RussWire[x];
		b 			= 	new RussWire[x];
		sum 		= 	new RussWire[x];
		carryOut	= 	new RussWire();
		overflow	= 	new RussWire();
		fullAdders  =	new Sim2_FullAdder[x];
		carry 		=	new boolean[a.length + 1];
		
		// Initialize arrays of RussWire objects
        for (int i = 0; i < x; i++) {
            a[i] 			=	new RussWire();
            b[i]			= 	new RussWire();
            sum[i] 			= 	new RussWire();
            fullAdders[i] 	= 	new Sim2_FullAdder();
        }
	}
	
	public void execute() {
	    // Calculate the sum and carry bits for each bit
	    for (int i = 0; i < a.length; i++) {
	        fullAdders[i].a.set(a[i].get());
	        fullAdders[i].b.set(b[i].get());

	        // Set inputs for the full adder
	        fullAdders[i].carryIn.set(carry[i]);
	        fullAdders[i].execute();

	        // Set the sum for this bit
	        sum[i].set(fullAdders[i].sum.get());

	        // Update carry for the next iteration
	        carry[i + 1] = fullAdders[i].carryOut.get();
	    }
	    
	    // Set the carryOut for the last bit
	    carryOut.set(carry[a.length]);

	    // Calculate overflow using XOR of last two carry bits
	    overflow.set(carry[a.length] ^ carry[a.length - 1]);
	}
}
