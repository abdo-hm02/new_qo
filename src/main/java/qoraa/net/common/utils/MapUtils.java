package qoraa.net.common.utils;

import lombok.experimental.UtilityClass;

import java.util.Map;

/**
 * Provides utility methods for {@link Map} instances.
 **/
@UtilityClass
public final class MapUtils {

    /**
     * Null-safe check if the specified map is not empty. Null returns false.
     *
     * @param map
     *            the map to check, may be null
     * @return true if non-null and non-empty
     */
    public static <K, V> boolean isNotEmpty(Map<K, V> map) {
	return !isNullOrEmpty(map);
    }

    /**
     * Null-safe check if the specified {@code map} is empty. Null returns true.
     *
     * @param map
     *            the map to check, may be null
     * @return true if null or empty
     */
    public static <K, V> boolean isNullOrEmpty(Map<K, V> map) {
	return map == null || map.isEmpty();
    }

}
