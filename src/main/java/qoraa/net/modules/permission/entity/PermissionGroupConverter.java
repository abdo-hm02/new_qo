package qoraa.net.modules.permission.entity;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
class PermissionGroupConverter implements AttributeConverter<PermissionGroup, String> {

    @Override
    public String convertToDatabaseColumn(PermissionGroup group) {
        return group.getText();
    }

    @Override
    public PermissionGroup convertToEntityAttribute(String dbData) {
        if (dbData == null) {
            // 'dbData' might be null on load for the roles created without permissions
            return null;
        }

        return PermissionGroup.getByText(dbData);
    }
}
