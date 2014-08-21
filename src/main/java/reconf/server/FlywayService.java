/*
 *    Copyright 2013-2014 ReConf Team
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */
package reconf.server;

import java.util.*;
import java.util.regex.*;
import javax.annotation.*;
import javax.sql.*;
import org.apache.commons.lang3.*;
import org.flywaydb.core.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.context.annotation.*;
import com.google.common.collect.*;

/*
 * Flyway must be called manually
 * SpringSecurity starts BEFORE Flyway
 * This causes errors during because SpringSecurity tables are created after SpringSecurity startup
 */
@Configuration
public class FlywayService {

    @Value("${spring.datasource.url}") String dataSourceUrl;
    @Autowired DataSource dataSource;

    private static final Set<String> clobIncompatible = Sets.newHashSet("mysql", "postgresql", "sqlserver");

    @PostConstruct
    public void setUpDB() {
        int version = 0;
        try {
            Flyway flyway = new Flyway();
            flyway.setInitOnMigrate(true);
            flyway.setSqlMigrationPrefix("V");
            flyway.setSqlMigrationSuffix(".sql");
            flyway.setInitVersion("1");
            flyway.setLocations("sql/common", getDbMigration(dataSourceUrl));
            flyway.setDataSource(dataSource);
            flyway.migrate();

            version = Integer.parseInt(flyway.info().current().getVersion().toString());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        if (version != ReConfConstants.DB_VERSION) {
            throw new RuntimeException("error creating tables. check logs");
        }
    }

    private String getDbMigration(String dataSourceUrl) {
        Pattern pattern = Pattern.compile("jdbc:([^:]+):.*");
        Matcher matcher = pattern.matcher(StringUtils.lowerCase(dataSourceUrl));
        if (!matcher.matches()) {
            throw new RuntimeException("spring.datasource.url does not match a known database");
        }
        String database = StringUtils.lowerCase(matcher.group(1));
        if (clobIncompatible.contains(database)) {
            return "sql/" + database;
        }
        return "sql/" + "clob";
    }

}
