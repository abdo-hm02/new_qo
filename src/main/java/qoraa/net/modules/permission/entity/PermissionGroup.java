package qoraa.net.modules.permission.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;
import java.util.stream.Stream;

import static java.util.function.UnaryOperator.identity;
import static java.util.stream.Collectors.toMap;

@AllArgsConstructor
public enum PermissionGroup {
    PERMISSION("Permission"),
    USER("User"),
    USER_GROUP("UserGroup"),
    PROJECT_BOOK("ProjectBook"),
    PROJECT_PARTICIPANT("ProjectParticipant"),
    READING_PROGRESS("ReadingProgress"),
    READING_PROJECT("ReadingProject"),
    READING_SUMMARY("ReadingSummary");

    private final String text;

    public String getText() {
        return text;
    }

    public static PermissionGroup getByText(String text) {
        PermissionGroup group = CACHE.get(text);
        if (group == null) {
            throw new IllegalArgumentException("Permission group '%s' does not exist!".formatted(text));
        }
        return group;
    }

    private static final Map<String, PermissionGroup> CACHE = Stream.of(values())
            .collect(toMap(PermissionGroup::getText, identity()));
}
