package pkg223071132_project;

public class User {
    private int id;
    private String fullName;
    private String email;
    private String gender;
    private String role;
    private String address;
    private String phone;

    public User(int id, String fullName, String email, String gender, String role, String address, String phone) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.gender = gender;
        this.role = role;
        this.address = address;
        this.phone = phone;
    }

    public int getId() { return id; }
    public String getFullName() { return fullName; }
    public String getEmail() { return email; }
    public String getGender() { return gender; }
    public String getRole() { return role; }
    public String getAddress() { return address; }
    public String getPhone() { return phone; }
}
