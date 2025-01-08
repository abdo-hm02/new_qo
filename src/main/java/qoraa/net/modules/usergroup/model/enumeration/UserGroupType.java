package qoraa.net.modules.usergroup.model.enumeration;

public enum UserGroupType {
    SUPER_ADMIN("Super admin"),
    SYSTEM_ADMIN("System admin"),
    READER("Reader");

    private final String description;

    UserGroupType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
