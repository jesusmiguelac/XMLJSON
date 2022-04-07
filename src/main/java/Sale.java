public class Sale {

    int id;
    String firstName;
    String lastName;

    double sales;
    String state;

    String department;

    public Sale(int id, String firstName, String lastName, double sales, String state, String department) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.sales = sales;
        this.state = state;
        this.department = department;
    }

    public Sale(){

    }

    @Override
    public String toString() {
        return "Sale{" +
                "id=" + id +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", sales=" + sales +
                ", state='" + state + '\'' +
                ", department='" + department + '\'' +
                '}';
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public double getSales() {
        return sales;
    }

    public void setSales(double sales) {
        this.sales = sales;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getDepartment() {
        return department;
    }

    public void setDepartment(String department) {
        this.department = department;
    }
}
