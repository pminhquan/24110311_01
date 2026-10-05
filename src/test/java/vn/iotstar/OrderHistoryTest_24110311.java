package vn.iotstar;

import vn.iotstar.entity.Orders_24110311;
import vn.iotstar.util.OrderStatusUtil_24110311;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class OrderHistoryTest_24110311 {

    public static void main(String[] args) {
        System.out.println(">>> Running Order History and Status Filtering Unit Checks...");

        testCanonicalStatusesAndLabels();
        testStatusValidationAndNormalization();
        testOrdersHelperMethods();

        System.out.println(">>> All Order History self-checks PASSED successfully!");
    }

    private static void testCanonicalStatusesAndLabels() {
        List<String> statuses = OrderStatusUtil_24110311.getAllStatuses();
        assert statuses.size() == 8 : "Expected exactly 8 canonical statuses, found: " + statuses.size();

        Map<String, String> expectedLabels = Map.of(
                "NEW", "Đơn hàng mới",
                "CONFIRMED", "Đã xác nhận",
                "PREPARING", "Chuẩn bị hàng",
                "SHIPPING", "Vận chuyển",
                "DELIVERING", "Giao hàng",
                "DELIVERED", "Đã giao",
                "CANCELLED", "Đơn hàng hủy",
                "RETURNED", "Đơn hàng hoàn"
        );

        for (Map.Entry<String, String> entry : expectedLabels.entrySet()) {
            assert OrderStatusUtil_24110311.isValidStatus(entry.getKey()) : "Missing status: " + entry.getKey();
            assert entry.getValue().equals(OrderStatusUtil_24110311.getLabel(entry.getKey())) :
                    "Mismatch for label: " + entry.getKey() + " expected " + entry.getValue() + " got " + OrderStatusUtil_24110311.getLabel(entry.getKey());
        }

        System.out.println("  [PASS] testCanonicalStatusesAndLabels (all 8 statuses and Vietnamese labels verified)");
    }

    private static void testStatusValidationAndNormalization() {
        // Valid status in various forms
        assert OrderStatusUtil_24110311.isValidStatus("new") : "Case-insensitive check failed for 'new'";
        assert "NEW".equals(OrderStatusUtil_24110311.normalizeStatus(" new ")) : "Whitespace/case normalization failed";
        assert "DELIVERED".equals(OrderStatusUtil_24110311.normalizeStatus("Delivered")) : "Normalization failed for Delivered";

        // Invalid and null statuses
        assert !OrderStatusUtil_24110311.isValidStatus(null) : "Null should be invalid";
        assert !OrderStatusUtil_24110311.isValidStatus("") : "Empty string should be invalid";
        assert !OrderStatusUtil_24110311.isValidStatus("UNKNOWN_STATUS") : "Unknown status should be invalid";
        assert OrderStatusUtil_24110311.normalizeStatus(null) == null : "Null normalization should return null";
        assert OrderStatusUtil_24110311.normalizeStatus("INVALID") == null : "Invalid normalization should return null";

        System.out.println("  [PASS] testStatusValidationAndNormalization");
    }

    private static void testOrdersHelperMethods() {
        Orders_24110311 order = new Orders_24110311();
        order.setStatus("PREPARING");
        LocalDateTime testDate = LocalDateTime.of(2026, 10, 5, 14, 30, 0);
        order.setOrderDate(testDate);

        assert "Chuẩn bị hàng".equals(order.getStatusVietnamese()) : "Expected 'Chuẩn bị hàng', got: " + order.getStatusVietnamese();
        assert order.getStatusBadgeClass() != null && !order.getStatusBadgeClass().isEmpty() : "Badge class must not be empty";
        assert "05/10/2026 14:30:00".equals(order.getFormattedOrderDate()) : "Date format mismatch: " + order.getFormattedOrderDate();

        System.out.println("  [PASS] testOrdersHelperMethods");
    }
}
