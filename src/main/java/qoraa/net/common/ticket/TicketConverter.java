package qoraa.net.common.ticket;

import org.springframework.util.StringUtils;
import qoraa.net.common.crypt.EncryptionException;
import qoraa.net.common.crypt.IdEncrypter;

public class TicketConverter {
    public static <T> T fromDtoToInternal(String ticket, Class<T> type) {
        try {
            Long id = IdEncrypter.decryptGenericId(ticket);
            return type.cast(id);
        } catch (Exception e) {
            throw new EncryptionException("Failed to decrypt ticket", e);
        }
    }

    public static String fromInternalToDto(Long id) {
        return IdEncrypter.encryptGenericId(id);
    }
}
