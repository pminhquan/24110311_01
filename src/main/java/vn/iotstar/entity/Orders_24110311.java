package vn.iotstar.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "Orders")
public class Orders_24110311 implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private int orderId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private User_24110311 user;

    @Column(name = "recipient_name", nullable = false, length = 255, columnDefinition = "NVARCHAR(255)")
    private String recipientName;

    @Column(name = "recipient_phone", nullable = false, length = 20)
    private String recipientPhone;

    @Column(name = "shipping_address", nullable = false, columnDefinition = "NVARCHAR(MAX)")
    private String shippingAddress;

    @Column(name = "payment_method", nullable = false, length = 50)
    private String paymentMethod = "COD";

    @Column(name = "status", nullable = false, length = 50)
    private String status = "NEW";

    @Column(name = "total_amount", nullable = false, precision = 18, scale = 2)
    private BigDecimal totalAmount;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderDetail_24110311> orderDetails = new ArrayList<>();

    public Orders_24110311() {
    }

    public Orders_24110311(User_24110311 user, String recipientName, String recipientPhone, String shippingAddress, String paymentMethod, String status, BigDecimal totalAmount, LocalDateTime orderDate) {
        this.user = user;
        this.recipientName = recipientName;
        this.recipientPhone = recipientPhone;
        this.shippingAddress = shippingAddress;
        this.paymentMethod = paymentMethod;
        this.status = status;
        this.totalAmount = totalAmount;
        this.orderDate = orderDate;
    }

    public Orders_24110311(User_24110311 user, String recipientName, String recipientPhone, String shippingAddress, String paymentMethod, String status, double totalAmount, LocalDateTime orderDate) {
        this(user, recipientName, recipientPhone, shippingAddress, paymentMethod, status, BigDecimal.valueOf(totalAmount), orderDate);
    }

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }

    public User_24110311 getUser() {
        return user;
    }

    public void setUser(User_24110311 user) {
        this.user = user;
    }

    public String getRecipientName() {
        return recipientName;
    }

    public void setRecipientName(String recipientName) {
        this.recipientName = recipientName;
    }

    public String getRecipientPhone() {
        return recipientPhone;
    }

    public void setRecipientPhone(String recipientPhone) {
        this.recipientPhone = recipientPhone;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = BigDecimal.valueOf(totalAmount);
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(LocalDateTime orderDate) {
        this.orderDate = orderDate;
    }

    public List<OrderDetail_24110311> getOrderDetails() {
        return orderDetails;
    }

    public void setOrderDetails(List<OrderDetail_24110311> orderDetails) {
        this.orderDetails = orderDetails;
    }

    public String getStatusVietnamese() {
        return vn.iotstar.util.OrderStatusUtil_24110311.getLabel(this.status);
    }

    public String getStatusBadgeClass() {
        return vn.iotstar.util.OrderStatusUtil_24110311.getBadgeClass(this.status);
    }

    public String getFormattedOrderDate() {
        if (this.orderDate == null) {
            return "";
        }
        return this.orderDate.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
    }
}

