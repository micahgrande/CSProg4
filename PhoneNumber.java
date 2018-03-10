/*  Micah Joseph Grande
    cssc0900
*/

public class PhoneNumber implements Comparable<PhoneNumber> {
    String areaCode, prefix, number;
    String phoneNumber;
   
    // Constructor.  Creates a new PhoneNumber instance.  The parameter
    // is a phone number in the form xxx-xxx-xxxx, which is area code -
    // prefix - number.  The phone number must be validated, and an
    // IllegalArgumentException thrown if it is invalid.
    public PhoneNumber(String n) {
    	verify(n);
    	String[] wholeNumber = n.split("-");
    	phoneNumber = n;
    	areaCode = wholeNumber[0];
    	prefix = wholeNumber[1];
    	number = wholeNumber[2];
    }
    
    // Follows the specifications of the Comparable Interface.  
    public int compareTo(PhoneNumber n) {
    	return phoneNumber.compareTo(n.phoneNumber);
    }
       
    // Returns an int representing the hashCode of the PhoneNumber.
    public int hashCode() {
    	byte b[] = phoneNumber.getBytes();
    	long hashVal = 0;
    	for(int i = number.length(); i >= 0; i--)
    		hashVal = (hashVal<<5) + b[i];
    	return (int) hashVal;
    	
    }
   
    // Private method to validate the Phone Number.  Should be called
    // from the constructor.   
    private void verify(String n) {
    	if(n.matches("\\d{3}[-\\.\\s]\\d{3}[-\\.\\s]\\d{4}"))
    		return;
    		throw new IllegalArgumentException();
    }    
       
    // Returns the area code of the Phone Number.
    public String getAreaCode() {
    	return areaCode;
    }
       
    // Returns the prefix of the Phone Number.
    public String getPrefix() {
    	return prefix;
    }
       
    // Returns the the last four digits of the number.
    public String getNumber() {
    	return number;
    }

    // Returns the Phone Number.       
    public String toString() {
    	return phoneNumber;
    }
}
