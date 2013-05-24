package org.springframework.social.connect.jdbc;


import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.encrypt.TextEncryptor;
import org.springframework.social.connect.ConnectionFactoryLocator;

/**
 * On Android we'll simply use the SQL Lite variant, but for now, to emulate Android, I need to work with this.
 * <p/>
 * There's no reason to leave the JdbcConnectionRepository as package private. We should make it public
 * so that it can be used independent of the {@link JdbcUsersConnectionRepository}, which handles
 * <EM>multiple</EM> users, but as we're assuming an Android client, we'll never need more than one client.
 * <p/>
 *
 * @author Josh Long
 */
public class CrmConnectionRepository extends JdbcConnectionRepository {
    public CrmConnectionRepository(String userId, JdbcTemplate jdbcTemplate, ConnectionFactoryLocator connectionFactoryLocator, TextEncryptor textEncryptor, String tablePrefix) {
        super(userId, jdbcTemplate, connectionFactoryLocator, textEncryptor, tablePrefix);
    }
}
