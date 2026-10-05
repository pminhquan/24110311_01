package vn.iotstar.util;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public final class OrderStatusUtil_24110311 {

    public static final String STATUS_NEW = "NEW";
    public static final String STATUS_CONFIRMED = "CONFIRMED";
    public static final String STATUS_PREPARING = "PREPARING";
    public static final String STATUS_SHIPPING = "SHIPPING";
    public static final String STATUS_DELIVERING = "DELIVERING";
    public static final String STATUS_DELIVERED = "DELIVERED";
    public static final String STATUS_CANCELLED = "CANCELLED";
    public static final String STATUS_RETURNED = "RETURNED";

    private static final Map<String, String> STATUS_MAP;
    private static final Map<String, String> BADGE_CLASS_MAP;
    private static final List<String> ALL_STATUSES;

    static {
        Map<String, String> map = new LinkedHashMap<>();
        map.put(STATUS_NEW, "Đơn hàng mới");
        map.put(STATUS_CONFIRMED, "Đã xác nhận");
        map.put(STATUS_PREPARING, "Chuẩn bị hàng");
        map.put(STATUS_SHIPPING, "Vận chuyển");
        map.put(STATUS_DELIVERING, "Giao hàng");
        map.put(STATUS_DELIVERED, "Đã giao");
        map.put(STATUS_CANCELLED, "Đơn hàng hủy");
        map.put(STATUS_RETURNED, "Đơn hàng hoàn");
        STATUS_MAP = Collections.unmodifiableMap(map);
        ALL_STATUSES = List.copyOf(map.keySet());

        Map<String, String> badge = new LinkedHashMap<>();
        badge.put(STATUS_NEW, "bg-primary");
        badge.put(STATUS_CONFIRMED, "bg-info text-dark");
        badge.put(STATUS_PREPARING, "bg-warning text-dark");
        badge.put(STATUS_SHIPPING, "bg-primary");
        badge.put(STATUS_DELIVERING, "bg-warning text-dark");
        badge.put(STATUS_DELIVERED, "bg-success");
        badge.put(STATUS_CANCELLED, "bg-danger");
        badge.put(STATUS_RETURNED, "bg-secondary");
        BADGE_CLASS_MAP = Collections.unmodifiableMap(badge);
    }

    private OrderStatusUtil_24110311() {
    }

    public static boolean isValidStatus(String status) {
        if (status == null) {
            return false;
        }
        return STATUS_MAP.containsKey(status.trim().toUpperCase());
    }

    public static String normalizeStatus(String status) {
        if (status == null) {
            return null;
        }
        String upper = status.trim().toUpperCase();
        return STATUS_MAP.containsKey(upper) ? upper : null;
    }

    public static String getLabel(String status) {
        if (status == null) {
            return "";
        }
        return STATUS_MAP.getOrDefault(status.trim().toUpperCase(), status);
    }

    public static String getBadgeClass(String status) {
        if (status == null) {
            return "bg-secondary";
        }
        return BADGE_CLASS_MAP.getOrDefault(status.trim().toUpperCase(), "bg-secondary");
    }

    public static List<String> getAllStatuses() {
        return ALL_STATUSES;
    }

    public static Map<String, String> getStatusMap() {
        return STATUS_MAP;
    }
}
