package Source;

public class Customer
{
    private int ID;
    private String name;
    private String address;
    private String postalCode;
    private String phoneNumber;
    private int firstDivisionID;

    /**
     * @param ID
     * @param name
     * @param address
     * @param postalCode
     * @param phoneNumber
     * @param firstDivisionID
     */
    public Customer(int ID, String name, String address, String postalCode, String phoneNumber, int firstDivisionID) {
        this.ID = ID;
        this.name = name;
        this.postalCode = postalCode;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.firstDivisionID = firstDivisionID;
    }

    /**
     * @param ID
     */
    public void setID(int ID) {
        this.ID = ID;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @param postalCode
     */
    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    /**
     * @param address
     */
    public void setAddress(String address) {
        this.address = address;
    }

    /**
     * @param phoneNumber
     */
    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    /**
     * @param firstDivisionID
     */
    public void setFirstDivisionID(int firstDivisionID) {
        this.firstDivisionID = firstDivisionID;
    }

    /**
     * @return the customers ID
     */
    public int getID() {
        return ID;
    }

    /**
     * @return the customers name
     */
    public String getName() {
        return name;
    }

    /**
     * @return the customers postal code
     */
    public String getPostalCode() {
        return postalCode;
    }

    /**
     * @return the customers address
     */
    public String getAddress() {
        return address;
    }

    /**
     * @return the customers phone number
     */
    public String getPhoneNumber() {
        return phoneNumber;
    }

    /**
     * @return the customers first division ID
     */
    public int getFirstDivisionID() {
        return firstDivisionID;
    }

}
