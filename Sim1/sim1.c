/* Implementation of a 32-bit adder in C.
 *
 * Author: Christopher Reid
 */


#include "sim1.h"

void execute_add(Sim1Data *obj)
{	
	// Read MSB
	int msb_a = (obj->a >> 31) & 0x1;
	int msb_b = (obj->b >> 31) & 0x1;
	obj->aNonNeg = !msb_a;
	obj->bNonNeg = !msb_b;

	int carry = 0;
	int sumBit = 0;

	// Sim 2's complement
	if (obj->isSubtraction == 1) {
		carry = 1;
	}

	for (int i = 0; i < 32; i++) {
		int bit_a = (obj->a >> i) & 0x1;
		int bit_b = (obj->b >> i) & 0x1;

		// Bit negation upon subtration
		if (obj->isSubtraction == 1) {
			bit_b = bit_b ^ 1;
		}

		// 0 bits in column
		if (bit_a == 0 && bit_b == 0 && carry == 0) {
			carry = 0;
			sumBit = 0;
		}
		// 3 bits in column 
		else if (bit_a == 1 && bit_b == 1 && carry == 1) {
			carry = 1;
			sumBit = 1;
		}
		// 1 bit in column
		else if ((bit_a == 1) ^ (bit_b == 1) ^ (carry == 1)) {
			carry = 0;
			sumBit = 1;
		}
		// 2 bits in column
		else {
			carry = 1;
			sumBit = 0;
		}

		obj->sum |= (sumBit << i);

		if(i == 31) {
			msb_b = bit_b;
		}
	}
	
	// Read MSB
	int msb_sum = (obj->sum >> 31) & 0x1;
	obj->sumNonNeg = !msb_sum;

	// Carry Out
	// Set from column 31
	if (carry == 1) {
		obj->carryOut = 1;
	}

	// Overflow
	// sum(neg, neg) = pos
	if ((msb_a == 1) && (msb_b  == 1) && (msb_sum == 0)) {
		obj->overflow = 1;
	} 
	// sum(pos, pos) = neg
	else if ((msb_a == 0) && (msb_b==0) && (msb_sum == 1)) {
		obj->overflow = 1;
	} else {
		obj->overflow = 0;
	}
}