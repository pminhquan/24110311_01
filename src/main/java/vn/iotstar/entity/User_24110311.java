package vn.iotstar.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "[User]")
public class User_24110311 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Column(name = "email", length = 255, nullable = false, unique = true)
    private String email;

    @Column(name = "fullname", length = 255, columnDefinition = "NVARCHAR(255)")
    private String fullname;

    @Column(name = "phone", length = 20)
    private String phone;

    @Column(name = "passwd", length = 255, nullable = false)
    private String passwd;

    @Column(name = "signup_date")
    private LocalDate signup_date;

    @Column(name = "last_login")
    private LocalDateTime last_login;

    @Column(name = "is_admin", nullable = false)
    private boolean is_admin;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Rating_24110311> ratings = new ArrayList<>();

    public User_24110311() {
    }

    public User_24110311(String email, String fullname, String phone, String passwd, LocalDate signup_date, LocalDateTime last_login, boolean is_admin) {
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.passwd = passwd;
        this.signup_date = signup_date;
        this.last_login = last_login;
        this.is_admin = is_admin;
    }

    public User_24110311(int id, String email, String fullname, String phone, String passwd, LocalDate signup_date, LocalDateTime last_login, boolean is_admin) {
        this.id = id;
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.passwd = passwd;
        this.signup_date = signup_date;
        this.last_login = last_login;
        this.is_admin = is_admin;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPasswd() {
        return passwd;
    }

    public void setPasswd(String passwd) {
        this.passwd = passwd;
    }

    public LocalDate getSignup_date() {
        return signup_date;
    }

    public void setSignup_date(LocalDate signup_date) {
        this.signup_date = signup_date;
    }

    public LocalDate getSignupDate() {
        return signup_date;
    }

    public void setSignupDate(LocalDate signupDate) {
        this.signup_date = signupDate;
    }

    public LocalDateTime getLast_login() {
        return last_login;
    }

    public void setLast_login(LocalDateTime last_login) {
        this.last_login = last_login;
    }

    public LocalDateTime getLastLogin() {
        return last_login;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.last_login = lastLogin;
    }

    public boolean isIs_admin() {
        return is_admin;
    }

    public boolean is_admin() {
        return is_admin;
    }

    public boolean isAdmin() {
        return is_admin;
    }

    public void setIs_admin(boolean is_admin) {
        this.is_admin = is_admin;
    }

    public void setIsAdmin(boolean isAdmin) {
        this.is_admin = isAdmin;
    }

    public List<Rating_24110311> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating_24110311> ratings) {
        this.ratings = ratings;
    }

    @Override
    public String toString() {
        return "User_24110311{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", fullname='" + fullname + '\'' +
                ", phone='" + phone + '\'' +
                ", signup_date=" + signup_date +
                ", last_login=" + last_login +
                ", is_admin=" + is_admin +
                '}';
    }
}
