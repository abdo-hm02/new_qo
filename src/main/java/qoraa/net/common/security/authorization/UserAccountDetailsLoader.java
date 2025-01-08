package qoraa.net.common.security.authorization;

public interface UserAccountDetailsLoader {
    UserAccountDetails load(String email);
}
